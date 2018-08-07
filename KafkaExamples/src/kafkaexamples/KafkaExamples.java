/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package kafkaexamples;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import java.util.Properties;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.kafka.common.serialization.LongSerializer;
import org.apache.kafka.common.serialization.StringSerializer;



/**
 *
 * @author cpu11165-local
 */
public class KafkaExamples {

    private final static String TOPIC = "test-new";
    private final static String BOOTSTRAP_SERVERS =
            "localhost:9092";
    /**
     * @param args the command line arguments
     */
        
    private static Producer<Long, String> createProducer() {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG,
                                            BOOTSTRAP_SERVERS);
        props.put(ProducerConfig.CLIENT_ID_CONFIG, "KafkaExamples");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG,
                                        LongSerializer.class.getName());
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG,
                                    StringSerializer.class.getName());
        return new KafkaProducer<>(props);
    }

    static void runProducer(final int sendMessageCount) throws Exception {
        final Producer<Long, String> producer = createProducer();
        long time = System.currentTimeMillis();
        System.out.println(time);  
        try {
          for (long index = time; index < time + sendMessageCount; index++) {
              final ProducerRecord<Long, String> record =
                      new ProducerRecord<>(TOPIC, index,
                                  "Hello Mom " + index);

              RecordMetadata metadata = producer.send(record).get();

              long elapsedTime = System.currentTimeMillis() - time;
              System.out.printf("sent record(key=%s value=%s) " +
                              "meta(partition=%d, offset=%d) time=%d\n",
                      record.key(), record.value(), metadata.partition(),
                      metadata.offset(), elapsedTime);

          }
      } finally {
          producer.flush();
          producer.close();
      }
    }
    
    public static void main(String... args) throws Exception {
        if (args.length == 0) {
            runProducer(5);
        } else {
            runProducer(Integer.parseInt(args[0]));
        }
    }
    
}
