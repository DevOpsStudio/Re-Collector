package org.graylog.collector.heartbeat;


import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import org.joda.time.DateTime;

@AutoValue
@JsonAutoDetect
public abstract class CollectorLogFile {
    @JsonProperty("path")
    public abstract String path();

    @JsonProperty("mod_time")
    public abstract DateTime modTime();

    @JsonProperty("size")
    public abstract long size();

    @JsonProperty("is_dir")
    public abstract boolean isDir();

    @JsonCreator
    public static CollectorLogFile create(@JsonProperty("path") String path,
                                          @JsonProperty("mod_time") DateTime modTime,
                                          @JsonProperty("size") long size,
                                          @JsonProperty("is_dir") boolean isDir) {
        return new AutoValue_CollectorLogFile(path, modTime, size, isDir);
    }
}
