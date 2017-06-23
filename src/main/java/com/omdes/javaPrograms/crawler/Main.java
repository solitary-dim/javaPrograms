package com.omdes.javaPrograms.crawler;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created with IntelliJ IDEA.
 * User: Solitary.Wang
 * Date: 2017/6/23
 * Time: 20:55
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
 *
 */
public final class Main {
    private static Set<String> urlSet = new HashSet<>();
    private static Set<String> visitedUrl = new HashSet<>();
    private static Set<String> unVisitedUrl = new HashSet<>();

    private static Integer deep = 0;

    public static void main(String[] args) {
        new CrawlerImpl().crawlerFromUrl("http://www.baidu.com");

        /*final List<String> list = new ArrayList<>();
        list.add(url);
        getUrl(url, list);
        LOGGER.info("====================End============================");
        for (String s: list) {
            LOGGER.info(s);
        }*/
    }

    private static void getUrl(String url, final List<String> list) {
        deep++;
        StringBuilder result = new StringBuilder("");
        BufferedReader input = null;
        try {
            URL reqUrl = new URL(url);
            URLConnection connection = reqUrl.openConnection();
            connection.connect();
            input = new BufferedReader(new InputStreamReader(connection.getInputStream(), "UTF-8"));
            String line;
            while ((line = input.readLine()) != null) {
                result.append(line);
            }
        } catch (MalformedURLException e) {
            System.out.println("发送GET请求出现异常！"+ e);
        } catch (IOException e) {
            System.out.println("IOException!"+ e);
        } finally {
            try {
                if (null != input) {
                    input.close();
                }
            } catch (IOException e) {
                System.out.println("IOException!"+ e);
            }
        }
        visitedUrl.add(url);
        String body = result.toString();
        body = body.replace(">&nbsp; <", "><");
        body = body.replace("> <", "><");
        body = body.replace("><", ">\r\n<");
        // ouput html body content
        //LOGGER.info(body);
        String[] str = result.toString().split("href=");
        System.out.println("========================================" + deep + "=======================================");
        for (String s: str) {
            if (s.contains(">")) {
                s = s.substring(0, s.indexOf(">"));
            }
            if (s.contains("http://") && s.indexOf("\"") != 0 &&
                    !s.contains("javascript:void(0)") && !s.contains("<!DOCTYPE") &&
                    s.lastIndexOf(".css") != (s.length() -4)) {
                s = s.trim();
                System.out.println(s);
                String tempUrl;
                if (s.contains(" ")) {
                    tempUrl = s.substring(0, s.indexOf(" "));
                } else {
                    tempUrl = s;
                }
                tempUrl = tempUrl.replace("\"", "").replace("\'", "");
                if (!tempUrl.isEmpty()) {
                    urlSet.add(tempUrl);
                }
            }
        }
        System.out.println("----------------------------------------" + deep + "--------------------------------------------");
        /*for (String s: urlSet) {
            LOGGER.info(s);
        }*/
        if (!urlSet.isEmpty()) {
            for (String s: urlSet) {
                if (!visitedUrl.contains(s)) {
                    list.add(s);
                    System.out.println(s);
                    getUrl(s, list);
                    //deep--;
                }
                //TODO 去除已访问过URL方法有问题，需重写
                //urlSet.remove(s);
            }
        }
    }
}
