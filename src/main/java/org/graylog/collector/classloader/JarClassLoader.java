package org.graylog.collector.classloader;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.Map;

/**
 * Created by kira on 17/8/5.
 */
public class JarClassLoader {

    private static Logger logger = LoggerFactory.getLogger(JarClassLoader.class);

    private static String userDir = System.getProperty("user.dir");

    private Map<String, URL[]> jarUrls = null;

    public JarClassLoader() {
        if (jarUrls == null) {
            jarUrls = getClassLoadJarUrls();
            if (jarUrls != null && jarUrls.size() > 0) {
                Thread.currentThread().setContextClassLoader(null);
            }
        }
    }

    public ClassLoader getClassLoaderByPluginName(String name) {
        URL[] urls = jarUrls.get(name);
        ClassLoader classLoader = this.getClass().getClassLoader();
        if (urls == null || urls.length == 0) {
            logger.warn("{}:load by AppclassLoader", name);
            return classLoader;
        }
        return new URLClassLoader(urls, classLoader);
    }

    private Map<String, URL[]> getClassLoadJarUrls() {
        Map<String, URL[]> result = Maps.newConcurrentMap();
        try {
            logger.warn("userDir:{}", userDir);
            String plugin = String.format("%s/plugin/", userDir);
            File pluginFile = new File(plugin);
            if (!pluginFile.exists()) {
                return null;
            }
            Map<String, URL[]> inputs = getClassLoadJarUrls(pluginFile);
            result.putAll(inputs);
            logger.warn("getClassLoadJarUrls:{}", result);
        } catch (Exception e) {
            logger.error("getClassLoadJarUrls error:{}", e.getMessage());
        }
        return result;
    }

    private Map<String, URL[]> getClassLoadJarUrls(File dir) throws IOException {
        String dirName = dir.getName();
        Map<String, URL[]> jurls = Maps.newConcurrentMap();
        File[] files = dir.listFiles();
        if (files != null && files.length > 0) {
            for (File f : files) {
                String jarName = f.getName();
                if (f.isFile() && jarName.endsWith(".jar")) {
                    jarName = jarName.split("-")[0].toLowerCase();
                    String[] jns = jarName.split("\\.");
                    jurls.put(String.format("%s:%s", dirName, jns.length == 0 ? jarName : jns[jns.length - 1]), new URL[]{f.toURI().toURL()});
                }
            }
        }
        return jurls;
    }
}