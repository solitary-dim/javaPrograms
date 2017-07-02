package com.omdes.javaPrograms.crawler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Solitary.Wang
 * Date: 2017/6/23
 * Time: 20:59
 *
 *                     _ooOoo_
 *                    o8888888o
 *                    88" . "88
 *                    (| -_- |)
 *                    O\  =  /O
 *                 ____/`---'\____
 *               .'  \\|     |//  `.
 *              /  \\|||  :  |||//  \
 *             /  _||||| -:- |||||-  \
 *             |   | \\\  -  /// |   |
 *             | \_|  ''\---/''  |   |
 *             \  .-\__  `-`  ___/-. /
 *           ___`. .'  /--.--\  `. . __
 *        ."" '<  `.___\_<|>_/___.'  >'"".
 *      | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 *      \  \ `-.   \_ __\ /__ _/   .-` /  /
 * ======`-.____`-.___\_____/___.-`____.-'======
 *                    `=---='
 * ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 *            佛祖保佑        永无BUG
 *  佛曰:
 *          写字楼里写字间，写字间里程序员；
 *          程序人员写程序，又拿程序换酒钱。
 *          酒醒只在网上坐，酒醉还来网下眠；
 *          酒醉酒醒日复日，网上网下年复年。
 *          但愿老死电脑间，不愿鞠躬老板前；
 *          奔驰宝马贵者趣，公交自行程序员。
 *          别人笑我忒疯癫，我笑自己命太贱；
 *          不见满街漂亮妹，哪个归得程序员？
 */
public final class CrawlerImpl {
    private static final Logger LOGGER = LoggerFactory.getLogger(CrawlerImpl.class);

    private static Set<String> visitedUrl = new HashSet<>();

    public void crawlerFromUrl(String url) {
        //this.setUrl(url);
        HttpClient httpClient = new HttpClient(url, 300*1000, 300*1000);

        try {
            httpClient.sendData("GET", null);
            LOGGER.info(httpClient.getResult());
        } catch (IOException e) {
            LOGGER.error("error!", e);
        }
    }

    private void setUrl(String url) {
        URLEntity entity = new URLEntity();
        entity.setLevel(0L);
        entity.setUrl(url);
        entity.setIsUsed(0);
        getUrl(entity);
    }

    private void getUrl(URLEntity entity) {
        Long level = entity.getLevel() + 1L;
        List<URLEntity> list = new ArrayList<>();
        StringBuilder result = new StringBuilder("");
        BufferedReader input = null;
        try {
            URL reqUrl = new URL(entity.getUrl());
            URLConnection connection = reqUrl.openConnection();
            connection.connect();
            input = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line;
            while ((line = input.readLine()) != null) {
                result.append(line);
            }
        } catch (MalformedURLException e) {
            LOGGER.error("发送GET请求出现异常！", e);
        } catch (IOException e) {
            LOGGER.error("IOException!", e);
        } finally {
            try {
                if (null != input) {
                    input.close();
                }
            } catch (IOException e) {
                LOGGER.error("IOException!", e);
            }
        }

        visitedUrl.add(entity.getUrl());
        //更新数据库设置该URL为已访问过
        entity.setIsUsed(1);
        //entity.setContent(result.toString());

        //String body = result.toString();
        //body = body.replace(">&nbsp; <", "><");
        //body = body.replace("> <", "><");
        //body = body.replace("><", ">\r\n<");
        //ouput html body content
        //LOGGER.info(body);
        String[] str = result.toString().split("href=");
        LOGGER.info("========================================" + level + "=======================================");
        for (String s : str) {
            URLEntity newEntity = new URLEntity();
            if (s.contains(">")) {
                s = s.substring(0, s.indexOf(">"));
            }
            if (s.contains("http://") && s.indexOf("\"") != 0 && !s.contains("<!DOCTYPE") &&
                    !s.contains("javascript:void(0)") && s.lastIndexOf(".css") != (s.length() - 4)) {
                s = s.trim();
                LOGGER.info(s);
                String tempUrl;
                if (s.contains(" ")) {
                    tempUrl = s.substring(0, s.indexOf(" "));
                } else {
                    tempUrl = s;
                }
                tempUrl = tempUrl.replace("\"", "").replace("\'", "");
                if (!tempUrl.isEmpty() && !tempUrl.contains(" ")) {
                    newEntity.setLevel(level);
                    newEntity.setUrl(tempUrl);
                    newEntity.setIsUsed(0);
                }
                list.add(newEntity);
            }
        }

        LOGGER.info("----------------------------------------" + level + "--------------------------------------------");
        if (!list.isEmpty()) {
            for (URLEntity urlEntity : list) {
                if (!visitedUrl.contains(urlEntity.getUrl())) {
                    LOGGER.info(urlEntity.getUrl());
                    getUrl(urlEntity);
                }
            }
        }
    }
}
