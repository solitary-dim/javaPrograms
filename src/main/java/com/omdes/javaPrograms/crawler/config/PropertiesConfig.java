package com.omdes.javaPrograms.crawler.config;

import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.util.Properties;

import static com.omdes.javaPrograms.crawler.config.BaseConfig.ENCODING;

/**
 * Created with IntelliJ IDEA.
 * User: Solitary.Wang
 * Date: 2017/7/4
 * Time: 18:53
 */
public final class PropertiesConfig {
    private static volatile PropertiesConfig propertiesConfig;

    private PropertiesConfig() {}

    public static PropertiesConfig getInstance() {
        if (null == propertiesConfig) {
            synchronized(PropertiesConfig.class) {
                if (null == propertiesConfig) {
                    propertiesConfig = new PropertiesConfig();
                }
            }
        }
        return propertiesConfig;
    }

    /**
     * 配置文件名
     */
    private static final String FILE_NAME = "crawler.properties";

    /**
     * 属性文件对象.
     */
    private Properties properties;

    private static final String IMAGE_PATH = "download.image.path";
    private String imagePath;

    private static final String CONNECT_TIMEOUT = "http.connect.timeout";
    private int timeout;

    private static final String PROXY_USE = "proxy.use";
    private boolean proxyUse;

    private static final String PROXY_HOST = "proxy.host";
    private String proxyHost;

    private static final String PROXY_PORT = "proxy.port";
    private String proxyPort;

    private static final String MYSQL_DRIVER_NAME = "mysql.jdbc.driver.name";
    private String mysqlDriverName;

    private static final String MYSQL_URL = "mysql.jdbc.url";
    private String mysqlUrl;

    private static final String MYSQL_USERNAME = "mysql.jdbc.username";
    private String mysqlUsername;

    private static final String MYSQL_PASSWORD = "mysql.jdbc.password";
    private String mysqlPassword;

    private static final String MYSQL_BATCH_MAX = "mysql.jdbc.batch.max";
    private int mysqlBatchMax;

    private static final String MYSQL_TABLE_NAME = "mysql.jdbc.table.name";
    private String mysqlTableName;

    public Properties getProperties() {
        return properties;
    }

    public String getImagePath() {
        return imagePath;
    }

    public int getTimeout() {
        return timeout;
    }

    public boolean isProxyUse() {
        return proxyUse;
    }

    public String getProxyHost() {
        return proxyHost;
    }

    public String getProxyPort() {
        return proxyPort;
    }

    public String getMysqlDriverName() {
        return mysqlDriverName;
    }

    public String getMysqlUrl() {
        return mysqlUrl;
    }

    public String getMysqlUsername() {
        return mysqlUsername;
    }

    public String getMysqlPassword() {
        return mysqlPassword;
    }

    public int getMysqlBatchMax() {
        return mysqlBatchMax;
    }

    public String getMysqlTableName() {
        return mysqlTableName;
    }

    public void loadProperties(Properties pro) {
        String value = null;
        System.out.println("配置项：");

        value = pro.getProperty(IMAGE_PATH);
        if (StringUtils.isNotEmpty(value)) {
            this.imagePath = value.trim();
            System.out.println("图片下载路径==>" + IMAGE_PATH + "==>" + value);
        }

        value = pro.getProperty(CONNECT_TIMEOUT);
        if (StringUtils.isNotEmpty(value)) {
            this.timeout = Integer.parseInt(value.trim());
            System.out.println("HTTP连接超时时间==>" + CONNECT_TIMEOUT + "==>" + value);
        }

        value = pro.getProperty(PROXY_USE);
        if (StringUtils.isNotEmpty(value)) {
            this.proxyUse = "true".equals(value.trim());
            System.out.println("是否使用代理服务器==>" + PROXY_USE + "==>" + value);
        }

        value = pro.getProperty(PROXY_HOST);
        if (StringUtils.isNotEmpty(value)) {
            this.proxyHost = value.trim();
            System.out.println("代理服务器地址==>" + PROXY_HOST + "==>" + value);
        }

        value = pro.getProperty(PROXY_PORT);
        if (StringUtils.isNotEmpty(value)) {
            this.proxyPort = value.trim();
            System.out.println("代理服务器端口==>" + PROXY_PORT + "==>" + value);
        }

        value = pro.getProperty(MYSQL_DRIVER_NAME);
        if (StringUtils.isNotEmpty(value)) {
            this.mysqlDriverName = value.trim();
            System.out.println("数据库驱动名称==>" + MYSQL_DRIVER_NAME + "==>" + value);
        }

        value = pro.getProperty(MYSQL_URL);
        if (StringUtils.isNotEmpty(value)) {
            this.mysqlUrl = value.trim();
            System.out.println("数据库地址==>" + MYSQL_URL + "==>" + value);
        }

        value = pro.getProperty(MYSQL_USERNAME);
        if (StringUtils.isNotEmpty(value)) {
            this.mysqlUsername = value.trim();
            System.out.println("数据库用户名==>" + MYSQL_USERNAME + "==>" + value);
        }

        value = pro.getProperty(MYSQL_PASSWORD);
        if (StringUtils.isNotEmpty(value)) {
            this.mysqlPassword = value.trim();
            System.out.println("数据库密码==>" + MYSQL_PASSWORD + "==>" + value);
        }

        value = pro.getProperty(MYSQL_BATCH_MAX);
        if (StringUtils.isNotEmpty(value)) {
            this.mysqlBatchMax = Integer.parseInt(value.trim());
            System.out.println("批量操作数==>" + MYSQL_BATCH_MAX + "==>" + value);
        }

        value = pro.getProperty(MYSQL_TABLE_NAME);
        if (StringUtils.isNotEmpty(value)) {
            this.mysqlTableName = value.trim();
            System.out.println("操作表名==>" + MYSQL_TABLE_NAME + "==>" + value);
        }
        System.out.println("已加载");
    }

    public void loadPropertiesFromPath(String rootPath) {
        if (StringUtils.isNotEmpty(rootPath)) {
            File file = new File(rootPath + File.separator + FILE_NAME);
            InputStream in = null;
            if (file.exists()) {
                try {
                    in = new FileInputStream(file);
                    final BufferedReader bf = new BufferedReader(new InputStreamReader(in, ENCODING));
                    properties = new Properties();
                    properties.load(bf);
                    loadProperties(properties);
                    bf.close();
                } catch (UnsupportedEncodingException e) {
                    System.out.println("UnsupportedEncodingException!" + e);
                } catch (FileNotFoundException e) {
                    System.out.println("FileNotFoundException!" + e);
                } catch (IOException e) {
                    System.out.println("IOException!" + e);
                } finally {
                    if (null != in) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            System.out.println("IOException!" + e);
                        }
                    }
                }
            } else {
                //由于此时可能还没有完成LOG的加载，因此采用标准输出来打印日志信息
                System.out.println(rootPath + FILE_NAME + "不存在,加载参数失败");
            }
        } else {
            loadPropertiesFromSrc();
        }

    }

    /**
     * 从classpath路径下加载配置参数
     */
    public void loadPropertiesFromSrc() {
        InputStream in = BaseConfig.class.getClassLoader().getResourceAsStream(FILE_NAME);
        if (null != in) {
            try {
                final BufferedReader bf = new BufferedReader(new InputStreamReader(in, ENCODING));
                properties = new Properties();
                properties.load(bf);
                loadProperties(properties);
                bf.close();
            } catch (UnsupportedEncodingException e) {
                System.out.println("UnsupportedEncodingException!" + e);
            } catch (IOException e) {
                System.out.println("IOException!" + e);
            } finally {
                try {
                    in.close();
                } catch (IOException e) {
                    System.out.println("IOException!" + e);
                }
            }
        } else {
            System.out.println(FILE_NAME + "属性文件未能找到!");
        }
    }
}
