/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kafka;

import crawler_data.ThreadCrawlZingMp3;
import crawler_data.ZingMP3Crawler;
import java.io.IOException;
import java.util.Arrays;
import java.util.Properties;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.json.simple.parser.ParseException;

/**
 *
 * @author cpu11165-local
 */
public class ConsumerKafka {
    private static Properties prop;
    private static String topicName;
    
    static{
        prop = ConsumerProperties.getConsumerProperties();
    }
    
    public static void main(String[] args) throws IOException, ParseException{
        
        ZingMP3Crawler crawler = new ZingMP3Crawler();
        
        initConsumerKafka(ConsumerProperties.getConsumerProperties(), "song_lookup");
        
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(prop);
        
        consumer.subscribe(Arrays.asList(topicName));
        
        while(true){
            ConsumerRecords<String, String> records = consumer.poll(100);
            for(ConsumerRecord<String, String> record : records){
                System.out.printf("offset = %d, key = %s, value = %s\n", 
                    record.offset(), record.key(), record.value());
                crawler.crawlSongBySearchName(record.value());
            }
        }
    }
    
    public static void initConsumerKafka(Properties aprop, String aTopicName){
        prop = aprop;
        topicName = aTopicName;
    }
    
}
