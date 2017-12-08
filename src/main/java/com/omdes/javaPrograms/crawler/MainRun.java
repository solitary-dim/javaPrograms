package com.omdes.javaPrograms.crawler;

import com.omdes.javaPrograms.crawler.config.PropertiesConfig;
import com.omdes.javaPrograms.crawler.impl.CrawlerPages;

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
public final class MainRun {

    protected static PropertiesConfig config = PropertiesConfig.getInstance();

    public static void runCrawler(String url) {
        //加载配置文件中配置
        config.loadPropertiesFromSrc();

        //启动时间监控线程
        new TimerMonitor("TimeMonitor").start();

        //开启爬虫
        //new CrawlerPages().crawlerFromUrl("http://www.hao123.com");
        //new CrawlerPages().crawlerFromUrl("http://image.baidu.com");
        //new CrawlerPages().crawlerFromUrl("http://image.baidu.com/search/index?tn=baiduimage&ipn=r&ct=201326592&cl=2&lm=-1&st=-1&sf=2&fmq=1480332039000_R_D&pv=&ic=0&nc=1&z=&se=&showtab=0&fb=0&width=&height=&face=0&istype=2&ie=utf-8&word=%E5%BE%AE%E8%B7%9D%E6%91%84%E5%BD%B1");
        new CrawlerPages().crawlerFromUrl(url);
    }
}
