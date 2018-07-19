/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package checklog4japi2x;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Future;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.apache.logging.log4j.core.Filter;
import org.apache.logging.log4j.core.Layout;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.appender.AbstractAppender;
import org.apache.logging.log4j.core.appender.AppenderLoggingException;
import org.apache.logging.log4j.core.config.Property;
import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.apache.logging.log4j.core.config.plugins.PluginAttribute;
import org.apache.logging.log4j.core.config.plugins.PluginElement;
import org.apache.logging.log4j.core.config.plugins.PluginFactory;
import org.apache.logging.log4j.core.layout.SerializedLayout;
import org.apache.logging.log4j.core.util.Booleans;

/**
 *
 * @author cpu11165-local
 */
@SuppressWarnings("unused")
@Plugin(name = "Kafka", category = "Core", elementType = "appender", printObject = true)
public class KafkaAppender extends AbstractAppender {

    private KafkaProducer<String, String> producer = null;
    private String topic;
    private boolean syncsend;

    protected KafkaAppender(String name, Filter filter, Layout<? extends Serializable> layout, boolean ignoreExceptions, KafkaProducer<String, String> producer, String topic, boolean syncsend) {
        super(name, filter, layout, ignoreExceptions);
        this.producer = producer;
        this.topic = topic;
        this.syncsend = syncsend;
    }

    @PluginFactory
    public static KafkaAppender createAppender(@PluginAttribute("name") final String name,
            @PluginElement("Filter") final Filter filter,
            @PluginAttribute("ignoreExceptions") final String ignore,
            @PluginAttribute("topic") final String topic,
            @PluginAttribute("enable") String enable,
            @PluginAttribute("syncsend") String syncsend,
            @PluginElement("Layout") Layout<? extends Serializable> layout,
            @PluginElement("Properties") final Property[] properties) {
        boolean ignoreExceptions = Booleans.parseBoolean(ignore, true);
        boolean enableKafka = Booleans.parseBoolean(enable, true);
        boolean sync = Booleans.parseBoolean(syncsend, false);

        KafkaProducer<String, String> producer = null;
        Map<String, Object> props = new HashMap<String, Object>();
        for (Property property : properties) {
            props.put(property.getName(), property.getValue());
        }
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");

        if (enableKafka) {
            producer = new KafkaProducer<String, String>(props);
        }

        if (layout == null) {
            layout = SerializedLayout.createLayout();
        }

        return new KafkaAppender(name, filter, layout, ignoreExceptions, producer, topic, sync);

    }

    @Override
    public final void stop() {
        super.stop();
        if (producer != null) {
            producer.close();;
        }
    }

    public void append(LogEvent event) {
        try {
            if (producer != null) {
                Future<RecordMetadata> result = producer.send(new ProducerRecord<String, String>(topic, getLayout().toSerializable(event).toString()));
                if (syncsend) {
                    result.get();
                }
            }
        } catch (final Exception e) {
            LOGGER.error("Unable to write to kafka for appender [{}].", this.getName(), e);
            throw new AppenderLoggingException("Unable to write to kafka in appender: " + e.getMessage(), e);
        } finally {
        }
    }

}
