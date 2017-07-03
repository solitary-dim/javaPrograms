package com.omdes.javaPrograms.crawler;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

import static com.omdes.javaPrograms.crawler.Config.*;

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

    private static int index = 0;
    private static long id = 1L;

    private MySQLHelper mySQLHelper;
    private static long startId;
    public void crawlerFromUrl(String url) {
        mySQLHelper = MySQLHelper.getInstance();
        startId = mySQLHelper.getIdStart();
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
                if (!link.contains(DOUBLE_LEFT_SLASH)) {
                    link = DOUBLE_LEFT_SLASH + link;
                }
                if (!link.contains(HTTP) && !link.contains(HTTPS)) {
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
        //将开始的url加入到未访问url中
        aUnvisitedUrl.add(url);
        URLEntity entity = new URLEntity();
        entity.setId(startId + id);
        entity.setLevel(1L);
        entity.setUrl(url);
        entity.setIsUsed(0);
        entity.setNotes("Page");
        getUrl(entity);
    }

    private void getUrl(URLEntity entity) {
        entity.setIsUsed(1);
        saveUrl(entity);

        Long level = entity.getLevel() + 1L;
        List<URLEntity> list = new ArrayList<>();

        //将本次访问url添加到已经访问过url
        aVisitedUrl.add(entity.getUrl());
        //将本次访问url从未访问过url中移出
        aUnvisitedUrl.remove(entity.getUrl());
        HttpClient httpClient = new HttpClient(entity.getUrl(), TIME_OUT, TIME_OUT);

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
            //LOGGER.info("image url: " + link);

            URLEntity imgEntity = new URLEntity();
            id++;
            imgEntity.setId(startId + id);
            imgEntity.setLevel(0L);
            imgEntity.setUrl(link);
            imgEntity.setIsUsed(1);
            imgEntity.setNotes("Image");
            saveUrl(imgEntity);

            //假设此处为图片下载
            imgVisitedUrl.add(link);
            //imgUnvisitedUrl.remove(link);
        }
        //移除已经访问过的图片src
        for (String link: imgVisitedUrl) {
            if (imgUnvisitedUrl.contains(link)) {
                imgUnvisitedUrl.remove(link);
            }
        }

        //将本次url指向的页面中的url保存下来
        getALink(doc);
        for (String link : aUnvisitedUrl) {
            //LOGGER.info("page url: " + link);

            URLEntity urlEntity = new URLEntity();
            id++;
            urlEntity.setId(startId + id);
            urlEntity.setLevel(level);
            urlEntity.setUrl(link);
            urlEntity.setIsUsed(0);
            urlEntity.setNotes("Page");
            list.add(urlEntity);

            //递归，根据新url去爬下一层的新的页面
            getUrl(urlEntity);
        }
    }

    static List<URLEntity> list = new ArrayList<>();
    public void saveUrl(URLEntity urlEntity) {
        String sql = "INSERT INTO T_URL (ID, NAME, STATUS, DELETED_FLAG, CREATED_TIME, " +
                "CREATED_USER_ID, UPDATED_TIME, UPDATED_USER_ID, LEVEL, URL, IS_USED, " +
                "COUNT, CONTENT, NOTES) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        list.add(urlEntity);
        index++;
        if (index >= BATCH_MAX) {
            try {
                mySQLHelper.openConnection();
                Connection connection = mySQLHelper.getConnection();
                PreparedStatement pstmt = connection.prepareStatement(sql);
                connection.setAutoCommit(false);
                for (URLEntity entity: list) {
                    long currentTime = System.currentTimeMillis();
                    pstmt.setLong(1, entity.getId());
                    pstmt.setString(2, "");
                    pstmt.setInt(3, entity.getStatus());
                    pstmt.setInt(4, entity.getDeletedFlag());
                    pstmt.setDate(5, new Date(currentTime));
                    //pstmt.setLong(6, entity.getCreatedUserId());
                    pstmt.setLong(6, 0L);
                    pstmt.setDate(7, new Date(currentTime));
                    //pstmt.setLong(8, entity.getUpdatedUserId());
                    pstmt.setLong(8, 0L);
                    pstmt.setLong(9, entity.getLevel());
                    pstmt.setString(10, entity.getUrl());
                    pstmt.setInt(11, entity.getIsUsed());
                    pstmt.setInt(12, entity.getCount());
                    pstmt.setString(13, entity.getContent());
                    pstmt.setString(14, entity.getNotes());
                    pstmt.addBatch();
                }
                pstmt.executeBatch();
                connection.commit();

                pstmt.close();
                connection.close();
            } catch (SQLException e) {
                LOGGER.error("SQLException!", e);
            }
            index = 0;
            list.removeAll(list);
            LOGGER.info("save data into MySQL");
        }
    }
}
