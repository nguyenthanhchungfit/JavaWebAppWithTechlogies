/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cache_data;

import java.util.Arrays;
import java.util.Properties;
import kafka.ConsumerProperties;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.consumer.KafkaConsumer;

/**
 *
 * @author cpu11165-local
 */
public class CacheUpdateDataConsumer {
    private static Properties prop;
    private static String topicName;
    
    static{
        prop = ConsumerProperties.getConsumerProperties();
    }
    
    public static void main(String[] args){
        
        DataCacher songCache = DataCacher.getInstance();
        
        initConsumerKafka(ConsumerProperties.getConsumerProperties(), "song_cache");
        
        KafkaConsumer<String, String> consumer = new KafkaConsumer<>(prop);
        
        consumer.subscribe(Arrays.asList(topicName));
        
        while(true){
            ConsumerRecords<String, String> records = consumer.poll(100);
            for(ConsumerRecord<String, String> record : records){
                String key = record.key();
                String value = record.value();
                System.out.printf("offset = %d, key = %s, value = %s\n", 
                    record.offset(), key, value);
                if(key.equals("update")){
                    songCache.updateCacheSongAt(key);
                }else if(key.equals("delete")){
                    songCache.deleteCacheSongAt(key);
                }
            }
        }
    }
    
    public static void initConsumerKafka(Properties aprop, String aTopicName){
        prop = aprop;
        topicName = aTopicName;
    }
}
