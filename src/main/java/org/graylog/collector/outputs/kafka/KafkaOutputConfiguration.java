package org.graylog.collector.outputs.kafka;

import com.google.inject.assistedinject.Assisted;
import com.typesafe.config.Config;
import org.graylog.collector.config.ConfigurationUtils;
import org.graylog.collector.outputs.OutputConfiguration;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.Range;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kira on 17/8/2.
 */
public class KafkaOutputConfiguration extends OutputConfiguration {

    private final KafkaOutput.Factory outputFactory;

    public String getHost() {
        return host;
    }

    public void setHost(String host) {
        this.host = host;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public interface Factory extends OutputConfiguration.Factory<KafkaOutputConfiguration> {
        @Override
        KafkaOutputConfiguration create(String id, Config config);
    }

    @NotBlank
    private String host;

    @NotNull
    @Range(min = 1024, max = 65535)
    private int port;

    @NotBlank
    private String topic;

    @Inject
    public KafkaOutputConfiguration(@Assisted String id,
                                    @Assisted Config config,
                                    KafkaOutput.Factory outputFactory) {
        super(id, config);
        this.outputFactory = outputFactory;
        if (config.hasPath("host")) {
            this.host = config.getString("host");
        }
        if (config.hasPath("topic")) {
            this.topic = config.getString("topic");
        }
        if (config.hasPath("port")) {
            this.port = config.getInt("port");
        }
    }

    @Override
    public KafkaOutput createOutput() {
        return outputFactory.create(this);
    }

    @Override
    public Map<String, String> toStringValues() {
        return Collections.unmodifiableMap(new HashMap<String, String>(super.toStringValues()) {
            {
                put("host", getHost());
                put("port", String.valueOf(getPort()));
                put("topic", String.valueOf(getTopic()));
            }
        });
    }

    @Override
    public String toString() {
        return ConfigurationUtils.toString(this);
    }
}
