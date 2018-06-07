/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kafka;

import java.util.Properties;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;

/**
 *
 * @author cpu11165-local
 */
public class ProducerKafka {
    private static Properties prop;
    private static Producer<String, String> producer;
    
    static{
        prop = ProducerProperties.getProducerProperties();
        producer = new KafkaProducer<>(prop);
    }
    
    public static void initProducerKafka(Properties aprop){
        prop = aprop;
        producer = new KafkaProducer<>(prop);
    }
    
    public static void send(String topicName, String key, String value){
        ProducerRecord<String, String> pr = new ProducerRecord<>(topicName,
            key, value);
        if(producer!= null){
            producer.send(pr);
        }
    }
    
    public static void close(){
        if(producer != null){
            producer.close();
        }
    }
    
}
