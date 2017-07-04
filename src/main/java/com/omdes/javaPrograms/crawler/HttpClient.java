package com.omdes.javaPrograms.crawler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.net.ssl.HttpsURLConnection;
import java.io.*;
import java.net.*;

/**
 * Created with IntelliJ IDEA.
 * User: solitary.wang
 * Date: 2017/6/12
 * Time: 8:59
 */
public final class HttpClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpClient.class);

    private URL url;
    private int connectedTimeOut;
    private int readTimeOut;
    private String result;

    private PropertiesConfig config = PropertiesConfig.getInstance();

    /**
     * 获取通信结果
     *
     * @return
     */
    public String getResult() {
        return result;
    }

    /**
     * 设置通信结果
     *
     * @param result
     */
    public void setResult(String result) {
        this.result = result;
    }

    /**
     * 构造函数
     *
     * @param url              http请求url
     * @param connectedTimeOut 连接超时时间
     * @param readTimeOut      读取超时时间
     */
    public HttpClient(String url, int connectedTimeOut, int readTimeOut) {
        LOGGER.info("page url: " + url);
        try {
            this.url = new URL(url);
            this.connectedTimeOut = connectedTimeOut;
            this.readTimeOut = readTimeOut;
        } catch (MalformedURLException e) {
            LOGGER.error("Create URL Error! ", e);
        }
    }

    /**
     * 创建Http连接
     *
     * @return
     */
    private HttpURLConnection createConnection(String method) {
        if (config.isProxyUse()) {
            LOGGER.info("使用代理服务器进行访问！");
            // 使用代理服务器进行连接的情况 设置系统变量
            System.setProperty("http.proxySet", "true");
            System.setProperty("http.proxyHost", config.getProxyHost());
            System.setProperty("http.proxyPort", "" + config.getProxyPort());
            // 针对https也开启代理
            System.setProperty("https.proxyHost", config.getProxyHost());
            System.setProperty("https.proxyPort", "" + config.getProxyPort());
        }

        HttpURLConnection httpURLConnection = null;
        try {
            httpURLConnection = (HttpURLConnection) url.openConnection();

            if (config.isProxyUse()) {
                // 使用代理服务器进行连接的情况
                httpURLConnection.setFollowRedirects(true);
            }

            if (BaseConfig.REQUEST_METHOD_POST.equals(method)) {
                //POST请求方式需要配置
                httpURLConnection.setConnectTimeout(connectedTimeOut);
                httpURLConnection.setReadTimeout(readTimeOut);
                httpURLConnection.setDoInput(true);//可读
                httpURLConnection.setDoOutput(true);//可写
                httpURLConnection.setUseCaches(false);//取消缓存
                httpURLConnection.setAllowUserInteraction(false);

                httpURLConnection.setRequestProperty("Connection", "close");
                //httpURLConnection.setRequestProperty("User-Agent", "omdes client");
                httpURLConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/56.0.2924.87 Safari/537.36");
            }

            httpURLConnection.setRequestMethod(method == null ? BaseConfig.REQUEST_METHOD_POST : BaseConfig.REQUEST_METHOD_GET);
        } catch (IOException e) {
            LOGGER.error("Create Connection Error! ", e);
        }

        if ("https".equalsIgnoreCase(url.getProtocol())) {
            HttpsURLConnection httpsURLConnection = (HttpsURLConnection) httpURLConnection;
            httpsURLConnection.setSSLSocketFactory(new BaseHttpSSLSocketFactory());
            //解决由于服务器证书问题导致HTTPS无法访问的情况
            httpsURLConnection.setHostnameVerifier(new BaseHttpSSLSocketFactory.TrustAnyHostnameVerifier());
            return httpsURLConnection;
        }

        return httpURLConnection;
    }

    /**
     * HTTP 发送请求
     *
     * @param connection
     * @param method
     * @param message
     * @param encoding
     */
    private void requestServer(final URLConnection connection, String method, String message, String encoding) {
        PrintStream out = null;
        try {
            connection.connect();
            if (BaseConfig.REQUEST_METHOD_POST.equals(method)) {
                LOGGER.info("发送POST请求！");
                out = new PrintStream(connection.getOutputStream(), false, encoding);
                out.print(message);
                out.flush();
            }
        } catch (IOException e) {
            LOGGER.error("Send Data Failed! ", e);
        } finally {
            if (null != out) {
                out.close();
            }
        }
    }

    /**
     * HTTP 请求返回数据
     *
     * @param connection
     * @param encoding
     * @return
     * @throws IOException
     */
    private String response(final HttpURLConnection connection, String encoding) {
        InputStream in = null;
        StringBuilder sb = new StringBuilder();
        try {
            if (BaseConfig.HTTP_RESPONSE_CODE_SUCCESS == connection.getResponseCode()) {
                in = connection.getInputStream();
                sb.append(new String(read(in), encoding));
            } else {
                in = connection.getErrorStream();
                sb.append(new String(read(in), encoding));
            }
        } catch (IOException e) {
            LOGGER.error("Get Return Data Failed! ", e);
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    LOGGER.error("Close InputStream Error! ", e);
                }
            }
            if (null != connection) {
                connection.disconnect();
            }
        }
        return sb.toString();
    }

    private static byte[] read(InputStream in) throws IOException {
        if (null != in) {
            byte[] buf = new byte[1024];
            int length;
            ByteArrayOutputStream bout = new ByteArrayOutputStream();
            while ((length = in.read(buf, 0, buf.length)) > 0) {
                bout.write(buf, 0, length);
            }
            bout.flush();
            return bout.toByteArray();
        }
        return new byte[0];
    }


    //test
    public int sendData(String method, String data) throws IOException {
        HttpURLConnection httpURLConnection = createConnection(method);
        httpURLConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=" + BaseConfig.ENCODING);
        httpURLConnection.setRequestProperty("Accept", "text/html");

        this.requestServer(httpURLConnection, method, data, BaseConfig.ENCODING);
        this.result = this.response(httpURLConnection, BaseConfig.ENCODING);
        return httpURLConnection.getResponseCode();
    }

}