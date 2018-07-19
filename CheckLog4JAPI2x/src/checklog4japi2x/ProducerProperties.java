/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checklog4japi2x;

import java.util.Properties;

/**
 *
 * @author cpu11165-local
 */
public class ProducerProperties {
    
    //Assign localhost id
    private static final String BOOTSTRAP_SERVERS = "localhost:9092";
    
    //Set acknowledgements for producer requests.  
    private static final String ACKS = "all";
    
    //If the request fails, the producer can automatically retry,
    private static final int RETRIES = 0;
    
    //Specify buffer size in config
    private static final int BATCH_SIZE = 16384;
    
    //Reduce the no of requests less than 0   
    private static final int LINGER_MS = 1;
    
    //The buffer.memory controls the total amount of memory available to the producer for buffering.
    private static final int BUFFER_MEMORY = 33554432;
    
    
    private static final String KEY_SERIALIZER = "org.apache.kafka.common.serialization.StringSerializer";
    private static final String VALUE_SERIALIZER = "org.apache.kafka.common.serialization.StringSerializer";
    
    
    public static Properties getProducerProperties(){
        Properties prop = new Properties();
        prop.put("bootstrap.servers", BOOTSTRAP_SERVERS);
        prop.put("acks", ACKS);
        prop.put("retries", RETRIES);
        prop.put("batch.size", BATCH_SIZE);
        prop.put("linger.ms", LINGER_MS);
        prop.put("buffer.memory", BUFFER_MEMORY);
        prop.put("key.serializer", KEY_SERIALIZER);
        prop.put("value.serializer", VALUE_SERIALIZER);
        return prop;
    }
}
