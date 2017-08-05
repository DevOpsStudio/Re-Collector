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

    private final DatabaseInput.Factory inputFactory;

    @Inject
    public DatabaseInputConfiguration(@Assisted String id,
                                      @Assisted Config config,
                                      DatabaseInput.Factory inputFactory) {
        super(id, config);
        this.inputFactory = inputFactory;
        if (config.hasPath("dbDriver")) {
            this.dbDriver = config.getString("dbDriver");
        }
        if (config.hasPath("dbConnectionUrl")) {
            this.dbConnectionUrl = config.getString("dbConnectionUrl");
        }
        if (config.hasPath("sql")) {
            this.sql = config.getString("sql");
        }
        if (config.hasPath("keytype")) {
            this.keytype = config.getString("keytype");
        }
        if (config.hasPath("idfield")) {
            this.idfield = config.getString("idfield");
        }
        if (config.hasPath("dbDriverPath")) {
            this.dbDriverPath = config.getString("dbDriverPath");
        }
        if (config.hasPath("initSql")) {
            this.initSql = config.getString("initSql");
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
