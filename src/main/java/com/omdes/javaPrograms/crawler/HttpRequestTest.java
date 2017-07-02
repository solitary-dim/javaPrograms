package com.omdes.javaPrograms.crawler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.*;

/**
 * Created with IntelliJ IDEA.
 * User: Solitary.Wang
 * Date: 2017/7/2
 * Time: 9:07
 */
public final class HttpRequestTest {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpRequestTest.class);

    public static void main(String[] args) {
        LOGGER.info("send request to baidu");

        //设置系统变量
        System.setProperty("http.proxySet", "true");
        System.setProperty("http.proxyHost", Config.PROXY_HOST);
        System.setProperty("http.proxyPort", "" + Config.PROXY_PORT);
        // 针对https也开启代理
        System.setProperty("https.proxyHost", Config.PROXY_HOST);
        System.setProperty("https.proxyPort", "" + Config.PROXY_PORT);

        StringBuilder content = new StringBuilder();
        //开始请求
        try {
            URL url = new URL("http://www.baidu.com");
            URLConnection conn = url.openConnection();
            HttpURLConnection httpCon = (HttpURLConnection) conn;
            httpCon.setFollowRedirects(true);
            httpCon.setRequestMethod("GET");

            BufferedReader input = new BufferedReader(new InputStreamReader(httpCon.getInputStream(), Config.ENCODING));
            String line;
            while ((line = input.readLine()) != null) {
                content.append(line).append("\n");
            }
        } catch (UnsupportedEncodingException e) {
            LOGGER.error("UnsupportedEncodingException!" , e);
        } catch (ProtocolException e) {
            LOGGER.error("ProtocolException!", e);
        } catch (MalformedURLException e) {
            LOGGER.error("MalformedURLException!", e);
        } catch (IOException e) {
            LOGGER.error("IOException!", e);
        }
        LOGGER.info("==============End==================");
    }
}
