package com.omdes.javaPrograms.crawler.impl;

import com.omdes.javaPrograms.crawler.config.PropertiesConfig;
import com.omdes.javaPrograms.crawler.entity.URLEntity;
import com.omdes.javaPrograms.crawler.helper.HtmlHelper;
import com.omdes.javaPrograms.crawler.helper.HttpClient;
import com.omdes.javaPrograms.crawler.helper.MySQLHelper;
import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.*;

import static com.omdes.javaPrograms.crawler.config.BaseConfig.*;

/**
 * Created with IntelliJ IDEA.
 * User: Solitary.Wang
 * Date: 2017/7/2
 * Time: 20:26
 */
public final class CrawlerPages {
    private static final Logger LOGGER = LoggerFactory.getLogger(CrawlerPages.class);

    private static Set<String> aUnvisitedUrl = new HashSet<>();
    private static Set<String> aVisitedUrl = new HashSet<>();
    private static Set<String> imgUnvisitedUrl = new HashSet<>();
    private static Set<String> imgVisitedUrl = new HashSet<>();

    //需要做更新操作的url的集合
    private static Set<String> updateUrl = new HashSet<>();

    private static List<URLEntity> addList = new ArrayList<>();
    private static Map<String, URLEntity> updateMap = new HashMap<>();
    private static List<URLEntity> updateList = new ArrayList<>();
    private static int index = 0;
    private static long id = 1L;
    //修改递归为每层逐层爬取
    private static long level = 1L;

    private PropertiesConfig config = PropertiesConfig.getInstance();
    private MySQLHelper mySQLHelper;
    private static long startId;
    /**
     * 主方法
     */
    public void crawlerFromUrl(String url) {
        //如果查询到了未访问过的url，则直接开始爬虫，否则会退出beforeCrawler方法，进入下面的方法
        this.beforeCrawler();
        //由起始url开始
        this.setUrl(url);
    }

    /**
     * 拿取所有a标签的href
     * @param doc
     * @param level 这次得到的所有url所在的层次
     */
    private void getALink(Document doc, long level) {
        Elements elements = doc.select("a[href]");
        for (Element element:elements) {
            String link = element.attr("abs:href").trim();
            if (StringUtils.isNotEmpty(link)) {
                if (link.lastIndexOf(LEFT_SLASH) == (link.length() - 1)) {
                    link = link.substring(0, link.length() - 1);
                }
                //判断是否是已经访问过的url
                if (!aVisitedUrl.contains(link)) {
                    aUnvisitedUrl.add(link);
                }
                //在待请求url过多的情形下，暂定积攒到了1000条后就去请求，并清空待请求url列表
                if (aUnvisitedUrl.size() > QUEUE_MAX) {
                    // TODO: 2017/7/7
//                    new ImageDownload().imageDownload(imgUnvisitedUrl);
//                    imgVisitedUrl.addAll(imgUnvisitedUrl);
//                    imgUnvisitedUrl.removeAll(imgUnvisitedUrl);
                }
            }
        }
    }

    /**
     * 拿取所有img标签的src
     * @param doc
     */
    private void getImgSrc(Document doc) {
        Elements elements = doc.getElementsByTag("img");
        for (Element element : elements) {
            String link = element.attr("src").trim();
            if (StringUtils.isNotEmpty(link)) {
                link = HtmlHelper.correctImgSrc(link);
                //判断是否是已经下载过的图片url
                if (StringUtils.isNotEmpty(link) && !imgVisitedUrl.contains(link)) {
                    imgUnvisitedUrl.add(link);
                }
                //在待下载图片src过多的情形下，暂定积攒到了1000条后就去下载，并清空待下载src列表
                if (imgUnvisitedUrl.size() > QUEUE_MAX) {
                    new ImageDownload().imageDownload(imgUnvisitedUrl);
                    imgVisitedUrl.addAll(imgUnvisitedUrl);
                    imgUnvisitedUrl.removeAll(imgUnvisitedUrl);
                }
            }
        }
    }

    //在本次开始爬虫前，先对数据库中保存的上次未完成的url继续操作
    private void beforeCrawler() {
        mySQLHelper = MySQLHelper.getInstance();
        startId = mySQLHelper.getIdStart(config.getMysqlTableName());
        List<URLEntity> list = mySQLHelper.getVisitedUrl(config.getMysqlTableName(), null);

        if (null != list && list.size() > 0) {
            for (URLEntity entity : list) {
                if (entity.getLevel() == 0) {
                    imgVisitedUrl.add(entity.getUrl());
                } else {
                    aVisitedUrl.add(entity.getUrl());
                }
            }
            list.removeAll(list);
        }

        long maxLevel = 0L;

        list = mySQLHelper.getUnvisitedUrl(config.getMysqlTableName(), null);
        if (null != list && list.size() > 0) {
            for (URLEntity entity : list) {
                if (maxLevel < entity.getLevel()) {
                    maxLevel = entity.getLevel();
                }
                if (entity.getLevel() == 0) {
                    imgUnvisitedUrl.add(entity.getUrl());
                } else {
                    aUnvisitedUrl.add(entity.getUrl());
                }
                //将已经保存过在数据库中的URL实体类放入到做更新操作的集合中
                updateUrl.add(entity.getUrl());
                updateMap.put(entity.getUrl(), entity);
            }

            level = maxLevel + 1L;
            //由未访问url开始爬虫，且所有未曾访问过的url的层级（不问原来高低）全部重置为最底层（比数据库中已有最底一层还要低一层）
            for (URLEntity entity: list) {
                entity.setLevel(level);
            }
            mySQLHelper.updateUrl(list);
            this.doCrawler(list);
        }
    }

    //将开始url保存到实体类中,并开起爬虫
    private void setUrl(String url) {
        //将开始的url加入到未访问url中
        aUnvisitedUrl.add(url);
        URLEntity entity = new URLEntity();
        entity.setId(startId + id);
        entity.setLevel(level);
        entity.setUrl(url);
        entity.setIsUsed(0);
        entity.setNotes("Page");

        //第一层
        List<URLEntity> list = new ArrayList<>();
        list.add(entity);
        this.doCrawler(list);
    }

    //开始爬虫
    private void doCrawler(List<URLEntity> currentLevel) {
        //每进入下一层，层级加一
        level++;
        //记录下一层所有待爬取页面url
        List<URLEntity> nextLevel = new ArrayList<>();

        for (URLEntity entity:currentLevel) {
            //标注本次访问的url为已访问
            entity.setIsUsed(1);

            //将本次访问url添加到已经访问过url
            aVisitedUrl.add(entity.getUrl());
            //将本次访问url从未访问过url中移出
            aUnvisitedUrl.remove(entity.getUrl());

            //通过url进行页面访问
            HttpClient httpClient = new HttpClient(entity.getUrl(), config.getTimeout(), config.getTimeout());
            String htmlBody = "";
            try {
                httpClient.sendData("GET", null);
                htmlBody = httpClient.getResult();
                //LOGGER.info(htmlBody);
            } catch (IOException e) {
                LOGGER.error("error!", e);
            }
            //将通过本次url访问所得页面内容记录下
            if (StringUtils.isNotEmpty(htmlBody)) {
                entity.setContent(htmlBody);
            }
            saveUrlTemp(entity);

            //得到本次url所得页面中文档数据
            Document doc = Jsoup.parse(htmlBody);

            //将本次url指向的页面中图片的url保存下来
            getImgSrc(doc);

            //将本次url指向的页面中的url保存下来
            getALink(doc, level);
        }

        if (imgUnvisitedUrl.size() > 0) {
            //一层所有url访问结束后，下载本层所有图片，并且移除已经访问过的图片src
            new ImageDownload().imageDownload(imgUnvisitedUrl);
            imgVisitedUrl.addAll(imgUnvisitedUrl);
            imgUnvisitedUrl.removeAll(imgUnvisitedUrl);
        }

        //一层结束后，拿取本层得到的未访问过的url，逐条进行访问
        for (String link : aUnvisitedUrl) {
            URLEntity urlEntity = new URLEntity();
            id++;
            urlEntity.setId(startId + id);
            urlEntity.setLevel(level);
            urlEntity.setUrl(link);
            urlEntity.setIsUsed(0);
            urlEntity.setNotes("Page");
            nextLevel.add(urlEntity);
        }
        // TODO: 2017/7/7
        //递归，根据新url去爬下一层的新的页面
        doCrawler(nextLevel);
    }

    //将数据批量存储数据库前的预处理
    private void saveUrlTemp(URLEntity urlEntity) {
        String url = urlEntity.getUrl();
        //判断是否要做更新操作的URL
        if (!updateUrl.contains(url)) {
            addList.add(urlEntity);
            index++;
            if (index >= config.getMysqlBatchMax()) {
                mySQLHelper.saveUrl(addList);

                index = 0;
                addList.removeAll(addList);
                LOGGER.info("save data into MySQL");
            }
        } else {
            //是要做更新的url，因为不做新增操作，所以现有id需要回退一
            id--;
            //将本次实体类添加到更新队列中
            URLEntity entity = updateMap.get(url);
            entity.setLevel(urlEntity.getLevel());
            entity.setIsUsed(urlEntity.getIsUsed());
            entity.setCount(urlEntity.getCount());
            entity.setContent(urlEntity.getContent());
            entity.setNotes(urlEntity.getNotes());
            updateList.add(entity);

            updateUrl.remove(url);
            updateMap.remove(url);
            if (updateMap.size() == 0) {
                mySQLHelper.updateUrl(updateList);
                updateList.removeAll(updateList);
            }
        }
    }
}
