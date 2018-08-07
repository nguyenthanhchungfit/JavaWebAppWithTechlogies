/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checklog4japi2x;

import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;


/**
 *
 * @author cpu11165-local
 */
public class LookupSongConsumer {

    private static Properties prop;
    private static String topicName;

    static {
        prop = ConsumerProperties.getConsumerProperties();
    }

    public static void main(String[] args) throws IOException {
        System.out.println("*** Check code");

        //TEST
        initConsumerKafka(ConsumerProperties.getConsumerProperties(), "devglan-test");
        //initConsumerKafka(ConsumerProperties.getConsumerProperties(), "TEST");

        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(prop);

        consumer.subscribe(Arrays.asList(topicName));

        while (true) {
            ConsumerRecords<String, String> records = consumer.poll(100);
            for (ConsumerRecord<String, String> record : records) {
                System.out.printf("offset = %d, key = %s, value = %s\n",
                        record.offset(), record.key(), record.value());
            }
        }
    }


    public static void initConsumerKafka(Properties aprop, String aTopicName) {
        prop = aprop;
        topicName = aTopicName;
    }

}
