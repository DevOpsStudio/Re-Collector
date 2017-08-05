package org.graylog.collector.classloader;

import org.graylog.collector.inputs.db.DatabaseInput;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.net.URL;
import java.net.URLClassLoader;
import java.sql.Driver;
import java.util.logging.Logger;

/**
 * Created by kira on 17/8/5.
 */
public class DriverLoader {

    static final org.slf4j.Logger LOG = LoggerFactory.getLogger(DatabaseInput.class);

    /**
     * 加载对应路径jar包里的对应驱动
     *
     * @param fname 对应路径  如: lib4/ojdbc14.jar
     * @param dname 驱动名  如: oracle.jdbc.driver.OracleDriver
     * @return 加载到的驱动    java.sql.Driver
     * @throws Exception
     * @author tangxr
     */
    public static Driver getDriverLoaderByName(String fname, String dname) throws Exception {
        if (null == fname || "".equals(fname)) {
            LOG.error("对应的驱动路径不存在,请确认.");
            return null;
        }
        if (null == dname || "".equals(dname)) {
            LOG.error("对应的驱动类的名字不存在.");
            return null;
        }
        File file = new File(fname);
        if (!file.exists()) {
            LOG.error("对应的驱动jar不存在.");
            return null;
        }
        URLClassLoader loader = new URLClassLoader(new URL[]{file.toURI().toURL()});
        loader.clearAssertionStatus();

        return (Driver) loader.loadClass(dname).newInstance();

    }
}
