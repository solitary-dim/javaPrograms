package com.omdes.javaPrograms.crawler;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.omdes.javaPrograms.crawler.Config.LEFT_SLASH;
import static com.omdes.javaPrograms.crawler.Config.HTTP;

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

    public void crawlerFromUrl(String url) {
        this.setUrl(url);
    }

    /**
     * 拿取所有a标签的href
     * @param doc
     */
    private void getALink(Document doc) {
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
            }
        }
    }

    /**
     * 拿取所有img标签的src
     * @param doc
     */
    private void getImgLink(Document doc) {
        Elements elements = doc.getElementsByTag("img");
        for (Element element : elements) {
            String link = element.attr("src").trim();
            if (StringUtils.isNotEmpty(link)) {
                if (link.lastIndexOf(LEFT_SLASH) == (link.length() - 1)) {
                    link = link.substring(0, link.length() - 1);
                }
                if (!link.contains(HTTP)) {
                    link = HTTP + link;
                }
                //判断是否是已经下载过的图片url
                if (!imgVisitedUrl.contains(link)) {
                    imgUnvisitedUrl.add(link);
                }
            }
        }

        //download test
        //new ImageDownload().imageDownload(imgUrl);
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

        //将本次访问url添加到已经访问过url
        aVisitedUrl.add(entity.getUrl());
        HttpClient httpClient = new HttpClient(entity.getUrl(), 300*1000, 300*1000);

        String htmlBody = "";
        try {
            httpClient.sendData("GET", null);
            htmlBody = httpClient.getResult();
            //LOGGER.info(htmlBody);
        } catch (IOException e) {
            LOGGER.error("error!", e);
        }

        Document doc = Jsoup.parse(htmlBody);

        //将本次url指向的页面中图片的url保存下来
        getImgLink(doc);
        for (String link : imgUnvisitedUrl) {
            LOGGER.info("image url: " + link);
        }

        //将本次url指向的页面中的url保存下来
        getALink(doc);
        for (String link : aUnvisitedUrl) {
            LOGGER.info("page url: " + link);

            URLEntity urlEntity = new URLEntity();
            urlEntity.setLevel(level);
            urlEntity.setUrl(link);
            urlEntity.setIsUsed(0);
            list.add(urlEntity);

            //递归，根据新url去爬下一层的新的页面
            getUrl(urlEntity);
        }
    }
}
