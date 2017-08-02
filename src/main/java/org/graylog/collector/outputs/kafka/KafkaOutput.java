package org.graylog.collector.outputs.kafka;

import com.google.common.util.concurrent.Uninterruptibles;
import com.google.inject.assistedinject.Assisted;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.graylog.collector.Message;
import org.graylog.collector.outputs.OutputService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Inject;
import java.util.Properties;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

/**
 * Created by kira on 17/8/2.
 */
public class KafkaOutput extends OutputService {
    private static final Logger LOG = LoggerFactory.getLogger(KafkaOutput.class);

    public interface Factory extends OutputService.Factory<KafkaOutput, KafkaOutputConfiguration> {
        KafkaOutput create(KafkaOutputConfiguration configuration);
    }

    private final KafkaOutputConfiguration configuration;
    private static Producer<String, String> producer;
    private final CountDownLatch transportInitialized = new CountDownLatch(1);

    static Producer<String, String> getProducer(KafkaOutputConfiguration configuration) {
        if (producer == null) {
            synchronized (KafkaOutput.class) {
                if (producer != null) {
                    return producer;
                }
                Properties props = new Properties();
                props.put("bootstrap.servers", configuration.getHost() + ":" + configuration.getPort());
                props.put("acks", "all");
                props.put("retries", 0);
                props.put("request.required.acks", "0");
                props.put("batch.size", 64);
                props.put("linger.ms", 1);
                props.put("buffer.memory", 1024);
                props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
                props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
                producer = new KafkaProducer<>(props);
            }
            return producer;
        } else {
            return producer;
        }
    }

    @Inject
    public KafkaOutput(@Assisted KafkaOutputConfiguration configuration) {
        this.configuration = configuration;
    }

    @Override
    protected void doStart() {
        getProducer(configuration);
        LOG.info("Starting Kafka transport: {}", configuration);
        transportInitialized.countDown();
        notifyStarted();
    }

    @Override
    protected void doStop() {
        LOG.debug("Stopping transport {}", configuration);
        producer = null;
        notifyStopped();
    }

    @Override
    public String getId() {
        return configuration.getId();
    }

    @Override
    public Set<String> getInputs() {
        return configuration.getInputs();
    }

    @Override
    public void write(Message message) {
        Uninterruptibles.awaitUninterruptibly(transportInitialized);
        LOG.debug("Sending message: {}", message);
        try {
            Producer<String, String> producer = getProducer(configuration);
            producer.send(new ProducerRecord<>(String.valueOf(configuration.getTopic()),
                    "message", message.getMessage()));
        } catch (Exception e) {
            LOG.error("Failed to send message", e);
        }
    }
}
