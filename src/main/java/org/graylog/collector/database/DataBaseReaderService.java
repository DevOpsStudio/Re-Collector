package org.graylog.collector.database;

import com.google.common.util.concurrent.AbstractService;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.graylog.collector.MessageBuilder;
import org.graylog.collector.buffer.Buffer;
import org.graylog.collector.classloader.DriverLoader;
import org.graylog.collector.inputs.db.DatabaseInput;
import org.graylog.collector.inputs.db.DatabaseInputConfiguration;
import org.joda.time.DateTime;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.Properties;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Created by kira on 17/8/5.
 */
public class DataBaseReaderService extends AbstractService {
    DatabaseInputConfiguration configuration;
    DatabaseInput databaseInput;
    static final org.slf4j.Logger LOG = LoggerFactory.getLogger(DatabaseInput.class);
    //数据库中最后的一条记录
    String lastedRecord;
    Connection con = null;
    ScheduledExecutorService scheduledExecutorService;
    final Buffer buffer;
    String collectorHostName;
    MessageBuilder messageBuilder;

    public DataBaseReaderService(DatabaseInputConfiguration configuration,
                                 Buffer buffer,
                                 String collectorHostName,
                                 DatabaseInput databaseInput,
                                 MessageBuilder messageBuilder) {
        this.configuration = configuration;
        this.databaseInput = databaseInput;
        this.buffer = buffer;
        this.collectorHostName = collectorHostName;
        this.messageBuilder = messageBuilder;
    }

    private Connection getConnection(DatabaseInputConfiguration databaseInputConfiguration) {
        try {
            if (con == null) {
                Driver driver = DriverLoader.getDriverLoaderByName(
                        "plugin/" + databaseInputConfiguration.getDbDriverPath(),
                        databaseInputConfiguration.getDbDriver());
                Properties properties = new Properties();
                con = driver.connect(databaseInputConfiguration.getDbConnectionUrl(), properties);
                return con;
            } else {
                return con;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            return null;
        }

    }

    @Override
    protected void doStart() {
        try {
            String initSql = configuration.getInitSql();
            con = getConnection(configuration);
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(initSql);

            if (rs.next()) {
                lastedRecord = String.valueOf(rs.getObject(configuration.getIdfield()));
                LOG.info("SQL:[" + configuration.getInitSql() + "]的最后一条记录是(" + lastedRecord + ")");
                rs.close();
                stmt.close();
            } else {
                if (configuration.getKeytype().equals("id")) {
                    lastedRecord = "0";
                } else {
                    lastedRecord = String.valueOf(System.currentTimeMillis());
                }
            }

            this.scheduledExecutorService = Executors.newSingleThreadScheduledExecutor(
                    new ThreadFactoryBuilder()
                            .setDaemon(false)
                            .setNameFormat("database-thread-" + databaseInput.getId() + "-%d")
                            .build()
            );

            Runnable runnable = () -> {
                try {
                    Statement statement = con.createStatement();
                    String sql = configuration.getSql().replace("{id_field}", lastedRecord);
                    ResultSet resultSet = statement.executeQuery(sql);
                    while (resultSet.next()) {
                        MessageBuilder messageBuilder = this.messageBuilder.copy()
                                .timestamp(new DateTime())
                                .message("");
                        int columnNum = resultSet.getMetaData().getColumnCount();
                        for (int i = 1; i < columnNum; i++) {
                            if (String.valueOf(resultSet.getMetaData().getColumnName(i)).equals(configuration.getIdfield())) {
                                lastedRecord = String.valueOf(resultSet.getObject(i));
                            }
                            messageBuilder.addField(resultSet.getMetaData().getColumnName(i), String.valueOf(resultSet.getObject(i)));

                        }
                        buffer.insert(messageBuilder.build());
                    }
                    resultSet.close();
                    statement.close();

                } catch (SQLException e) {
                    e.printStackTrace();
                }
            };

            scheduledExecutorService.scheduleAtFixedRate(runnable, 0, 5, TimeUnit.SECONDS);

            notifyStarted();
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }

    @Override
    protected void doStop() {

    }
}
