package org.graylog.collector.inputs.db;

import com.google.inject.assistedinject.Assisted;
import org.graylog.collector.MessageBuilder;
import org.graylog.collector.annotations.CollectorHostName;
import org.graylog.collector.buffer.Buffer;
import org.graylog.collector.config.ConfigurationUtils;
import org.graylog.collector.database.DataBaseReaderService;
import org.graylog.collector.file.ChunkReader;
import org.graylog.collector.inputs.InputService;

import javax.inject.Inject;
import java.util.Set;

/**
 * Created by kira on 17/8/4.
 */
public class DatabaseInput extends InputService {
    private final DatabaseInputConfiguration configuration;
    private final Buffer buffer;
    private String collectorHostName;
    private DataBaseReaderService dataBaseReaderService;
    private MessageBuilder messageBuilder;

    public interface Factory extends InputService.Factory<DatabaseInput, DatabaseInputConfiguration> {
        DatabaseInput create(DatabaseInputConfiguration configuration);
    }

    @Inject
    public DatabaseInput(@Assisted DatabaseInputConfiguration configuration,
                         Buffer buffer,
                         @CollectorHostName String collectorHostName) {
        this.configuration = configuration;
        this.buffer = buffer;
        this.collectorHostName = collectorHostName;
        this.messageBuilder = new MessageBuilder()
                .input(getId())
                .outputs(getOutputs())
                .source(collectorHostName)
                .fields(configuration.getMessageFields());
        this.dataBaseReaderService = new DataBaseReaderService(
                configuration, buffer, collectorHostName, this, messageBuilder
        );
    }

    @Override
    protected void doStart() {
        dataBaseReaderService.startAsync().awaitRunning();
        notifyStarted();
    }

    @Override
    protected void doStop() {
        dataBaseReaderService.stopAsync().awaitTerminated();
        notifyStopped();
    }

    @Override
    public String getId() {
        return configuration.getId();
    }

    @Override
    public Set<String> getOutputs() {
        return configuration.getOutputs();
    }

    @Override
    public void setReaderFinished(ChunkReader chunkReader) {

    }

    @Override
    public String toString() {
        return ConfigurationUtils.toString(configuration, this);
    }
}
