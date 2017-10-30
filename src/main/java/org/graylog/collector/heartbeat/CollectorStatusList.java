package org.graylog.collector.heartbeat;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

import java.util.HashMap;

@AutoValue
@JsonAutoDetect
public abstract class CollectorStatusList {
    @JsonProperty("status")
    public abstract int status();

    @JsonProperty("message")
    public abstract String message();

    @JsonProperty("backends")
    public abstract HashMap<String, CollectorStatus> backends();

    @JsonCreator
    public static CollectorStatusList create(@JsonProperty("status") int status,
                                             @JsonProperty("message") String message,
                                             @JsonProperty("backends") HashMap<String, CollectorStatus> backends) {
        return new AutoValue_CollectorStatusList(status, message, backends);
    }}
