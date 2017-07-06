package com.omdes.javaPrograms.crawler.impl;

import com.omdes.javaPrograms.crawler.helper.HttpClient;
import com.omdes.javaPrograms.crawler.helper.MySQLHelper;
import com.omdes.javaPrograms.crawler.config.PropertiesConfig;
import com.omdes.javaPrograms.crawler.entity.URLEntity;
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
    private void getImgSrc(Document doc) {
        Elements elements = doc.getElementsByTag("img");
        for (Element element : elements) {
            String link = element.attr("src").trim();
            if (StringUtils.isNotEmpty(link)) {
                link = this.correctImgSrc(link);
                //判断是否是已经下载过的图片url
                if (StringUtils.isNotEmpty(link) && !imgVisitedUrl.contains(link)) {
                    imgUnvisitedUrl.add(link);
                }
            }
        }
    }

    //对图片的src进行处理
    private String correctImgSrc(String link) {
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
        if (link.contains("../")) {
            return null;
        }
        if (link.lastIndexOf(LEFT_SLASH) == (link.length() - 1)) {
            link = link.substring(0, link.length() - 1);
        }
        if (!link.contains(DOUBLE_LEFT_SLASH)) {
            link = DOUBLE_LEFT_SLASH + link;
        }
        //去除http://charts.edgar-online.com/ext/charts.dll?2-2-18-0-0-56-03NA000000BIDU-&SF:1|31-HT=180-WD=192-FREQ=6-BG=FFFFFF-FF:A18=ffffff|A33=ffffff-FTS:A17=0-FC:2=c9e4ff-FC:3=c9e4ff-HC=2-HO:SW-FF:1=c9e4ff-FB:1=c9e4ff-FL:1=c9e4ff-AT:9=0:8=000000-FC:3=009900-BT=0-FL:A17=f2f2f2|A5=f2f2f2|A9=f2f2f2|A33=f2f2f2|A34=f2f2f2|A6=999999|A10=999999|A18=999999|G1=dddddd|G2=dddddd-FTC:NW=f2f2f2|NE=f2f2f2|A17=999999|SE=f2f2f2|SW=f2f2f2-HO:NW-FF:1=c9e4ff-FB:1=c9e4ff-FL:1=c9e4ff-AT:9=0-FTC:AI33=999999|AM33=999999|AM9=999999-FF:1=c9e4ff-FB:1=c9e4ff-FL:1=c9e4ff-AT:9=0
        if (!link.contains(HTTP) && !link.contains(HTTPS) && !link.contains(".dll")) {
            link = HTTP + link;
        }
        //去除http:///wolfman/static/common/images/transparent.gif
        if (link.contains("///")) {
            return null;
        }
        //去除http://timg.baidu.com/timg
        String temp = link.substring(link.lastIndexOf("/"), link.length());
        if (!temp.contains(".")) {
            return null;
        }
        return link;
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
        entity.setLevel(level);
        entity.setUrl(url);
        entity.setIsUsed(0);
        entity.setNotes("Page");

        //第一层
        List<URLEntity> list = new ArrayList<>();
        list.add(entity);
        doCrawler(list);
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
            getALink(doc);
        }

        //一层所有url访问结束后，下载本层所有图片
        //download pictures from internet
        new ImageDownload().imageDownload(imgUnvisitedUrl);
        for (String link : imgUnvisitedUrl) {
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
            //imgUnvisitedUrl.remove(link);会报错语句，取消注释可以重新报错
        }
        //移除已经访问过的图片src
        for (String link: imgVisitedUrl) {
            if (imgUnvisitedUrl.contains(link)) {
                imgUnvisitedUrl.remove(link);
            }
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
