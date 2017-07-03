package com.omdes.javaPrograms.crawler;

/**
 * Created with IntelliJ IDEA.
 * User: Solitary.Wang
 * Date: 2017/6/23
 * Time: 21:31
 */
public final class Config {

    protected static final String REQUEST_METHOD_GET = "GET";
    protected static final String REQUEST_METHOD_POST = "POST";
    protected static final String ENCODING = "UTF-8";

    //HTTP请求放回状态码
    protected static final int HTTP_RESPONSE_CODE_SUCCESS = 200;

    //是否使用代理服务器
    protected static final Boolean USE_PROXY = true;
    //代理服务器地址
    protected static final String PROXY_HOST = "61.172.249.96";
    //代理服务器端口
    protected static final String PROXY_PORT = "80";

    protected static final char LEFT_SLASH = '/';
    protected static final String DOUBLE_LEFT_SLASH = "//";
    protected static final String HTTP = "http:";
    protected static final String HTTPS = "https:";

    protected static final String IMAGE_PATH = "D:/logs";
    protected static final int TIME_OUT = 5*60*1000;

    //MySQL
    protected static final String DRIVER_NAME = "com.mysql.jdbc.Driver";
    protected static final String SQL_URL = "jdbc:mysql://localhost:3306/sep?useUnicode=true&characterEncoding=UTF-8";
    protected static final String SQL_USER = "root";
    protected static final String SQL_PASSWORD = "root";
    protected static final int BATCH_MAX = 100;
}
