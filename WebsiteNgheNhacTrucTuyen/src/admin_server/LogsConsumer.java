/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package admin_server;

import Helpers.FormatPureString;
import contracts.ConsumerContract;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.util.Arrays;
import java.util.Properties;
import java.util.Scanner;
import kafka.ConsumerProperties;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.eclipse.jetty.websocket.client.ClientUpgradeRequest;
import org.eclipse.jetty.websocket.client.WebSocketClient;

/**
 *
 * @author Nguyen Thanh Chung
 */
public class LogsConsumer {

    private static Properties prop;
    private static String topicName;
    private static boolean isRunning = false;

    public static void main(String[] args) throws MalformedURLException, IOException, Exception {
        System.out.println("*** LogsConsumer is running");
        String dest = "ws://0.0.0.0:8003/getlogs";
        WebSocketClient client = new WebSocketClient();
        LogsClientProcessor socket = new LogsClientProcessor();
        client.start();
        URI echoUri = new URI(dest);
        ClientUpgradeRequest request = new ClientUpgradeRequest();
        System.out.println(client.connect(socket, echoUri, request));
        
        socket.getLatch().await();

        initConsumerKafka(ConsumerProperties.getConsumerProperties(), ConsumerContract.topicNameLogs);
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(prop);
        consumer.subscribe(Arrays.asList(topicName));

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                String value = record.value();
                System.out.printf("offset = %d, value = %s\n",
                        record.offset(), value);
                sendMessage(socket, value);
            }
        }

    }

    public static void run() throws Exception {
        if (!isRunning) {
            isRunning = true;
            System.out.println("*** LogsConsumer is running!!!");
            initConsumerKafka(ConsumerProperties.getConsumerProperties(), "logs_server");
            KafkaConsumer<String, String> consumer = new KafkaConsumer<>(prop);
            consumer.subscribe(Arrays.asList(topicName));

            String dest = "ws://localhost:8003/getlogs";

            WebSocketClient client = new WebSocketClient();
            LogsClientProcessor socket = new LogsClientProcessor();
            client.start();
            URI echoUri = new URI(dest);
            ClientUpgradeRequest request = new ClientUpgradeRequest();
            client.connect(socket, echoUri, request);
            socket.getLatch().await();

            while (true) {
                ConsumerRecords<String, String> records = consumer.poll(100);
                for (ConsumerRecord<String, String> record : records) {
                    String key = record.key();
                    String value = record.value();
                    sendMessage(socket, value);
                    System.out.printf("offset = %d, key = %s, value = %s\n",
                            record.offset(), key, value);
                }
            }
        } else {
            System.err.println("!!! LogsConsumer is runned!!");
        }
    }

    public static void initConsumerKafka(Properties aprop, String aTopicName) {
        prop = aprop;
        topicName = aTopicName;
    }

    private static void sendMessage(LogsClientProcessor socket, String message) {
        socket.sendMessage(message);
    }
}
