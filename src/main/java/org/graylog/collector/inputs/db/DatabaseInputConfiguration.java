package org.graylog.collector.inputs.db;

import com.google.inject.assistedinject.Assisted;
import com.typesafe.config.Config;
import org.graylog.collector.config.ConfigurationUtils;
import org.graylog.collector.config.constraints.IsOneOf;
import org.graylog.collector.inputs.InputConfiguration;
import org.graylog.collector.inputs.InputService;
import org.hibernate.validator.constraints.NotBlank;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by kira on 17/8/4.
 */
public class DatabaseInputConfiguration extends InputConfiguration {


    public String getDbDriver() {
        return dbDriver;
    }

    public void setDbDriver(String dbDriver) {
        this.dbDriver = dbDriver;
    }

    public String getDbDriverPath() {
        return dbDriverPath;
    }

    public void setDbDriverPath(String dbDriverPath) {
        this.dbDriverPath = dbDriverPath;
    }

    public String getInitSql() {
        return initSql;
    }

    public void setInitSql(String initSql) {
        this.initSql = initSql;
    }

    public String getDbUser() {
        return dbUser;
    }

    public void setDbUser(String dbUser) {
        this.dbUser = dbUser;
    }

    public String getDbPassword() {
        return dbPassword;
    }

    public void setDbPassword(String dbPassword) {
        this.dbPassword = dbPassword;
    }

    public Integer getDbSyncTime() {
        return dbSyncTime;
    }

    public void setDbSyncTime(Integer dbSyncTime) {
        this.dbSyncTime = dbSyncTime;
    }

    public interface Factory extends InputConfiguration.Factory<DatabaseInputConfiguration> {
        @Override
        DatabaseInputConfiguration create(String id, Config config);
    }

    //数据库驱动名称
    @NotNull
    private String dbDriver;

    @NotNull
    private String dbConnectionUrl;

    @NotNull
    private String sql;

    //起始的SQL语句,如select id from sample limit 1 order by id desc
    @NotNull
    private String initSql;

    @IsOneOf({"id", "timestamp"})
    private String keytype;

    //动态替换的关键变量
    @NotBlank
    private String idfield;

    @NotBlank
    private String dbDriverPath;

    private String dbUser;

    private String dbPassword;

    private Integer dbSyncTime;

    private final DatabaseInput.Factory inputFactory;

    @Inject
    public DatabaseInputConfiguration(@Assisted String id,
                                      @Assisted Config config,
                                      DatabaseInput.Factory inputFactory) {
        super(id, config);
        this.inputFactory = inputFactory;
        if (config.hasPath("db-driver")) {
            this.dbDriver = config.getString("db-driver");
        }
        if (config.hasPath("db-connection-url")) {
            this.dbConnectionUrl = config.getString("db-connection-url");
        }
        if (config.hasPath("sql")) {
            this.sql = config.getString("sql");
        }
        if (config.hasPath("key-type")) {
            this.keytype = config.getString("key-type");
        }
        if (config.hasPath("id-field")) {
            this.idfield = config.getString("id-field");
        }
        if (config.hasPath("db-driver-path")) {
            this.dbDriverPath = config.getString("db-driver-path");
        }
        if (config.hasPath("init-sql")) {
            this.initSql = config.getString("init-sql");
        }
        if (config.hasPath("db-user")) {
            this.dbUser = config.getString("db-user");
        }
        if (config.hasPath("db-password")) {
            this.dbPassword = config.getString("db-password");
        }
        if (config.hasPath("db-sync-time")) {
            this.dbSyncTime = config.getInt("db-sync-time");
        } else {
            this.dbSyncTime = 1;
        }
    }

    @Override
    public InputService createInput() {
        return inputFactory.create(this);
    }

    public String getDbConnectionUrl() {
        return dbConnectionUrl;
    }

    public void setDbConnectionUrl(String dbConnectionUrl) {
        this.dbConnectionUrl = dbConnectionUrl;
    }


    public String getIdfield() {
        return idfield;
    }

    public void setIdfield(String idfield) {
        this.idfield = idfield;
    }

    public DatabaseInput.Factory getInputFactory() {
        return inputFactory;
    }

    public String getKeytype() {
        return keytype;
    }

    public void setKeytype(String keytype) {
        this.keytype = keytype;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    @Override
    public Map<String, String> toStringValues() {
        return Collections.unmodifiableMap(new HashMap<String, String>(super.toStringValues()) {
            {
                put("dbDriver", getDbDriver());
                put("dbConnectionUrl", getDbConnectionUrl());
                put("sql", getSql());
                put("keytype", String.valueOf(getKeytype()));
                put("idfield", getIdfield());
                put("dbDriverPath", getDbDriverPath());
                put("initSql", getInitSql());
            }
        });
    }

    @Override
    public String toString() {
        return ConfigurationUtils.toString(this);
    }
}
