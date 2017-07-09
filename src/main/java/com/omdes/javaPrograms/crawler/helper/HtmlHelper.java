package com.omdes.javaPrograms.crawler.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.UnsupportedEncodingException;

import static com.omdes.javaPrograms.crawler.config.BaseConfig.ENCODING;
import static com.omdes.javaPrograms.crawler.config.BaseConfig.DOUBLE_LEFT_SLASH;

/**
 * Created with IntelliJ IDEA.
 * User: solitary.wang
 * Date: 2017/7/7
 * Time: 13:11
 */
public final class HtmlHelper {
    private static final Logger LOGGER = LoggerFactory.getLogger(HtmlHelper.class);

    private static final String DOT = ".";
    private static final String DOUBLE_DOT = "..";
    private static final String LEFT_SLASH = "/";
    private static final String AND_STRING = "&";
    private static final String QUESTION_MARK_STRING = "?";
    private static final String HTTP = "http:";
    private static final String HTTPS = "https:";
    private static final String PHP = ".php";
    private static final String DLL = ".dll";
    private static final String BS = ".bs";
    private static final String DATA = "data:";
    private static final String DOUBLE_WELL_NUMBER = "##";

    //替换四个字节的字符 如：\xF0\x9F\x98\x84\xF0\x9F
    public static String replaceFourChar(String str) {
        try {
            byte[] conbyte = str.getBytes(ENCODING);
            for (int i = 0; i < conbyte.length; i++) {
                if ((conbyte[i] & 0xF8) == 0xF0) {
                    for (int j = 0; j < 4; j++) {
                        conbyte[i + j] = 0x30;
                    }
                    i += 3;
                }
            }
            str = new String(conbyte);
            return str.replaceAll("0000", "");
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("编码异常！", e);
        }
        return null;
    }

    //对图片的src进行处理
    public static String correctImgSrc(String link) {
        //去除##
        if (link.contains(DOUBLE_WELL_NUMBER)) {
            //排除掉src="##"的这种情况
            return null;
        }
        //去除data：
        if (link.contains(DATA)) {
            //todo
                    /*link = "view-source:" + link;
                    //判断是否是已经下载过的图片url
                    if (!imgVisitedUrl.contains(link)) {
                        imgUnvisitedUrl.add(link);
                    }*/
            return null;
        }
        //去除../g.hiphotos.baidu.com/d1160924ab18972b41b1746deccd7b899f510a89.jpg
        if (link.contains(DOUBLE_DOT + LEFT_SLASH)) {
            return null;
        }
        if (link.lastIndexOf(LEFT_SLASH) == (link.length() - 1)) {
            link = link.substring(0, link.length() - 1);
        }
        if (!link.contains(DOUBLE_LEFT_SLASH)) {
            link = DOUBLE_LEFT_SLASH + link;
        }
        //去除http://charts.edgar-online.com/ext/charts.dll?2...
        if (!link.contains(HTTP) && !link.contains(HTTPS) && !link.contains(DLL) && !link.contains(BS)) {
            link = HTTP + link;
        }
        //去除http:///wolfman/static/common/images/transparent.gif
        if (link.contains(DOUBLE_LEFT_SLASH + LEFT_SLASH)) {
            return null;
        }
        //去除http://timg.baidu.com/timg
        String temp = link.substring(link.lastIndexOf(LEFT_SLASH), link.length());
        if (!temp.contains(DOT)) {
            return null;
        }
        //去除问号及后面内容
        if (link.contains(QUESTION_MARK_STRING)) {
            link = link.substring(0, link.indexOf(QUESTION_MARK_STRING));
        }
        if (link.contains(AND_STRING) || link.contains(PHP)) {
            return null;
        }
        return link;
    }
}
