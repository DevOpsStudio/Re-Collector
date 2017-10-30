package org.graylog.collector.heartbeat;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;
import java.util.List;

@AutoValue
@JsonAutoDetect
public abstract class CollectorMetrics {
    @JsonProperty("disks_75")
    @Nullable
    public abstract List<String> disks75();

    @JsonProperty("cpu_idle")
    @Nullable
    public abstract Float cpuIdle();

    @JsonProperty("load_1")
    @Nullable
    public abstract Float load1();

    @JsonCreator
    public static CollectorMetrics create(@JsonProperty("disks_75") @Nullable List<String> disks75,
                                          @JsonProperty("cpu_idle") @Nullable Float cpuIdle,
                                          @JsonProperty("load_1") @Nullable Float load1) {
        return new AutoValue_CollectorMetrics(disks75, cpuIdle, load1);
    }
}
