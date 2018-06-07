/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kafka;

import java.util.Properties;

/**
 *
 * @author cpu11165-local
 */
public class ConsumerProperties {
    private static final String BOOTSTRAP_SERVERS = "localhost:9092";
    private static final String GROUP_ID = "mp3";
    private static final String ENABLE_AUTO_COMMIT = "true";
    private static final String AUTO_COMMIT_INTERVAL_MS = "1000";
    private static final String SESSION_TIMEOUT_MS = "30000";
    private static final String KEY_DESERIALIZER = "org.apache.kafka.common.serialization.StringDeserializer";
    private static final String VALUE_DESERIALZER = "org.apache.kafka.common.serialization.StringDeserializer";
    
    public static Properties getConsumerProperties(){
        Properties prop = new Properties();
        prop.put("bootstrap.servers", BOOTSTRAP_SERVERS);
        prop.put("group.id", GROUP_ID);
        prop.put("enable.auto.commit", ENABLE_AUTO_COMMIT);
        prop.put("auto.commit.interval.ms", AUTO_COMMIT_INTERVAL_MS);
        prop.put("session.timeout.ms", SESSION_TIMEOUT_MS);
        prop.put("key.deserializer", KEY_DESERIALIZER);
        prop.put("value.deserializer", VALUE_DESERIALZER);
        
        return prop;
    }
}
