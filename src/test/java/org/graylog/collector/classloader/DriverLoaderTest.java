package org.graylog.collector.classloader;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.sql.*;
import java.util.Calendar;
import java.util.Properties;

/**
 * Created by kira on 17/8/5.
 */
public class DriverLoaderTest {
    @Rule
    public ExpectedException throwing = ExpectedException.none();
    Connection conn = null;
    Statement stat = null;

    @Before
    public void testLoadSqliteJar() {
        try {
            Driver driver = DriverLoader.getDriverLoaderByName("plugin/sqlite-jdbc-3.19.3.jar", "org.sqlite.JDBC");
            Properties properties = new Properties();
            conn = driver.connect("jdbc:sqlite:test.db", properties);
            stat = conn.createStatement();
            stat.executeUpdate("drop table if exists sample;");
            stat.executeUpdate("create table sample (`name`, times);");
            PreparedStatement ps = conn.prepareStatement("insert into sample values (?,?)");
            ps.setString(1, "sample");
            Calendar calendar = Calendar.getInstance();
            ps.setDate(2, new Date(calendar.getTime().getTime()));
            ps.executeUpdate();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void getLastTimeRecord() throws Exception {
        String lastSql = "select times from sample order by times desc limit 1";
        ResultSet result = stat.executeQuery(lastSql);
        if (result.next()) {
            String times = String.valueOf(result.getObject("times"));
            result.close();
            PreparedStatement ps = conn.prepareStatement("insert into sample values (?,?)");
            ps.setString(1, "nextsample");
            Calendar calendar = Calendar.getInstance();
            ps.setDate(2, new Date(calendar.getTime().getTime()));
            ps.executeUpdate();

            String sql = "select * from sample where times>{id_field}";
            result = stat.executeQuery(sql.replace("{id_field}", times));
            while (result.next()){
                System.out.println(result.getString("times"));
            }

        } else {
            throw new Exception("Faild to loadResult");
        }
    }
}
