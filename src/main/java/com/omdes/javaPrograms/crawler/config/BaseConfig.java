package com.omdes.javaPrograms.crawler.config;

/**
 * Created with IntelliJ IDEA.
 * User: Solitary.Wang
 * Date: 2017/6/23
 * Time: 21:31
 */
public final class BaseConfig {

    public static final String REQUEST_METHOD_GET = "GET";
    public static final String REQUEST_METHOD_POST = "POST";
    public static final String ENCODING = "UTF-8";

    public static final String NOTES_PAGE = "Page";
    public static final String NOTES_IMAGE = "Image";
    public static final int TYPE_NULL = 0;
    public static final int TYPE_PAGE = 1;
    public static final int TYPE_IMAGE = 2;

    //HTTP请求放回状态码
    public static final int HTTP_RESPONSE_CODE_SUCCESS = 200;

    public static final char AND_CHAR = '&';
    public static final char LEFT_SLASH = '/';
    public static final char QUESTION_MARK_CHAR = '?';
    public static final String AND_STRING = "&";
    public static final String QUESTION_MARK_STRING = "?";
    public static final String DOUBLE_LEFT_SLASH = "//";
    public static final String HTTP = "http:";
    public static final String HTTPS = "https:";
    public static final String PHP = ".php";
    public static final String DATA = "data:";
    public static final String DOUBLE_WELL_NUMBER = "##";

    public static final int QUEUE_MAX = 1000;
}
