package com.omdes.javaPrograms.crawler;

import org.apache.commons.lang.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.sql.*;
import java.util.*;
import java.util.Date;

import static com.omdes.javaPrograms.crawler.BaseConfig.*;

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

    private PropertiesConfig config = PropertiesConfig.getInstance();

    private MySQLHelper mySQLHelper;
    private static long startId;
    /**
     * 主方法
     */
    public void crawlerFromUrl(String url) {
        this.beforeCrawler();

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
                if (link.contains(DOUBLE_WELL_NUMBER)) {
                    //排除掉src="##"的这种情况
                    continue;
                }
                if (link.contains(DATA)) {
                    //todo
                    /*link = "view-source:" + link;
                    //判断是否是已经下载过的图片url
                    if (!imgVisitedUrl.contains(link)) {
                        imgUnvisitedUrl.add(link);
                    }*/
                    continue;
                }
                if (link.contains("../")) {
                    continue;
                }
                if (link.lastIndexOf(LEFT_SLASH) == (link.length() - 1)) {
                    link = link.substring(0, link.length() - 1);
                }
                if (!link.contains(DOUBLE_LEFT_SLASH)) {
                    link = DOUBLE_LEFT_SLASH + link;
                }
                if (!link.contains(HTTP) && !link.contains(HTTPS)) {
                    link = HTTP + link;
                }
                if (link.contains("///")) {
                    continue;
                }
                //判断是否是已经下载过的图片url
                if (!imgVisitedUrl.contains(link)) {
                    imgUnvisitedUrl.add(link);
                }
            }
        }
    }

    //在本次开始爬虫前，先对数据库中保存的上次未完成的url继续操作
    private void beforeCrawler() {
        mySQLHelper = MySQLHelper.getInstance();
        startId = mySQLHelper.getIdStart(config.getMysqlTableName());
        List<URLEntity> list = mySQLHelper.getUnvisitedUrl(config.getMysqlTableName(), null);

        if (null != list && list.size() > 0) {
            for (URLEntity entity : list) {
                if (entity.getLevel() == 0) {
                    imgUnvisitedUrl.add(entity.getUrl());
                } else {
                    aUnvisitedUrl.add(entity.getUrl());
                }
                //将已经保存过在数据库中的URL实体类放入到做更新操作的集合中
                updateUrl.add(entity.getUrl());
                updateMap.put(entity.getUrl(), entity);
            }
            list.removeAll(list);
        }

        list = mySQLHelper.getVisitedUrl(config.getMysqlTableName(), null);
        if (null != list && list.size() > 0) {
            for (URLEntity entity : list) {
                if (entity.getLevel() == 0) {
                    imgVisitedUrl.add(entity.getUrl());
                } else {
                    aVisitedUrl.add(entity.getUrl());
                }
            }
        }
    }

    //将开始url保存到实体类中,并开起爬虫
    private void setUrl(String url) {
        //将开始的url加入到未访问url中
        aUnvisitedUrl.add(url);
        URLEntity entity = new URLEntity();
        entity.setId(startId + id);
        entity.setLevel(1L);
        entity.setUrl(url);
        entity.setIsUsed(0);
        entity.setNotes("Page");
        doCrawler(entity);
    }

    //开始爬虫
    private void doCrawler(URLEntity entity) {
        //标注本次访问的url为已访问
        entity.setIsUsed(1);

        Long level = entity.getLevel() + 1L;
        List<URLEntity> list = new ArrayList<>();

        //将本次访问url添加到已经访问过url
        aVisitedUrl.add(entity.getUrl());
        //将本次访问url从未访问过url中移出
        aUnvisitedUrl.remove(entity.getUrl());
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

        Document doc = Jsoup.parse(htmlBody);

        //将本次url指向的页面中图片的url保存下来
        getImgLink(doc);
        //download pictures from internet
        new ImageDownload().imageDownload(imgUnvisitedUrl);
        for (String link : imgUnvisitedUrl) {
            //LOGGER.info("image url: " + link);

            URLEntity imgEntity = new URLEntity();
            id++;
            imgEntity.setId(startId + id);
            imgEntity.setLevel(0L);
            imgEntity.setUrl(link);
            imgEntity.setIsUsed(1);
            imgEntity.setNotes("Image");
            saveUrlTemp(imgEntity);

            //将已经下载过的图片src转入已使用过set
            imgVisitedUrl.add(link);
            //imgUnvisitedUrl.remove(link);会报错语句，删除注释可以重新报错
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
            doCrawler(urlEntity);
        }
    }

    //将数据批量存储数据库前的预处理
    private void saveUrlTemp(URLEntity urlEntity) {
        String url = urlEntity.getUrl();
        //判断是否要做更新操作的URL
        if (!updateUrl.contains(url)) {
            addList.add(urlEntity);
            index++;
            if (index >= config.getMysqlBatchMax()) {
                saveUrl(addList);

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
                updateUrl(updateList);
                updateList.removeAll(updateList);
            }
        }
    }

    //连接数据库，将数据批量存入数据库
    private void saveUrl(List<URLEntity> list) {
        String sql = "INSERT INTO T_URL (ID, NAME, STATUS, DELETED_FLAG, CREATED_TIME, " +
                "CREATED_USER_ID, UPDATED_TIME, UPDATED_USER_ID, LEVEL, URL, IS_USED, " +
                "COUNT, CONTENT, NOTES) VALUES (?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try {
            mySQLHelper.openConnection();
            Connection connection = mySQLHelper.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql);
            connection.setAutoCommit(false);
            for (URLEntity entity: list) {
                Date date = new Date();
                pstmt.setLong(1, entity.getId());
                pstmt.setString(2, "");
                pstmt.setInt(3, entity.getStatus());
                pstmt.setInt(4, entity.getDeletedFlag());
                pstmt.setTimestamp(5, new Timestamp(date.getTime()));
                //pstmt.setLong(6, entity.getCreatedUserId());
                pstmt.setLong(6, 0L);
                pstmt.setTimestamp(7, new Timestamp(date.getTime()));
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

            if (null != pstmt) {
                pstmt.close();
            }
        } catch (SQLException e) {
            LOGGER.error("SQLException!", e);
        }
    }

    //连接数据库，将数据批量存入数据库
    private void updateUrl(List<URLEntity> list) {
        String sql = "UPDATE T_URL SET UPDATED_TIME = ?, UPDATED_USER_ID = ?, LEVEL = ?," +
                " IS_USED = ?, COUNT = ?, CONTENT =?, NOTES = ? WHERE ID = ?";

        try {
            mySQLHelper.openConnection();
            Connection connection = mySQLHelper.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql);
            connection.setAutoCommit(false);
            for (URLEntity entity: list) {
                Date date = new Date();
                pstmt.setTimestamp(1, new Timestamp(date.getTime()));
                //pstmt.setLong(2, entity.getUpdatedUserId());
                pstmt.setLong(2, 0L);
                pstmt.setLong(3, entity.getLevel());
                pstmt.setInt(4, entity.getIsUsed());
                pstmt.setInt(5, entity.getCount());
                pstmt.setString(6, entity.getContent());
                pstmt.setString(7, entity.getNotes());
                pstmt.setLong(8, entity.getId());
                pstmt.addBatch();
            }
            pstmt.executeBatch();
            connection.commit();

            if (null != pstmt) {
                pstmt.close();
            }
        } catch (SQLException e) {
            LOGGER.error("SQLException!", e);
        }
    }
}
