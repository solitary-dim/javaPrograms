package com.omdes.javaPrograms.crawler.impl;

import com.omdes.javaPrograms.crawler.config.PropertiesConfig;
import com.omdes.javaPrograms.crawler.entity.URLEntity;
import com.omdes.javaPrograms.crawler.helper.MySQLHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.omdes.javaPrograms.crawler.config.BaseConfig.*;

/**
 * Created with IntelliJ IDEA.
 * User: Solitary.Wang
 * Date: 2017/7/2
 * Time: 9:33
 */
public final class ImageDownload {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageDownload.class);

    private PropertiesConfig config = PropertiesConfig.getInstance();
    private MySQLHelper mySQLHelper = MySQLHelper.getInstance();

    //记录图片不能访问的src的第一个地址到黑名单
    //如：http://images/201607/thumb_img/526_thumb_G_1468185673057.jpg中的images
    private static Set<String> blackList = new HashSet<>();

    private static long startId = 0L;

    //根据src将图片保存到本地
    public void imageDownload(Set<String> links) {
        if (links.size() <= 0) {
            LOGGER.info("没有可以去下载的图片！");
            return;
        }

        final List<URLEntity> list = new ArrayList<>();
        startId = mySQLHelper.getIdStart(config.getMysqlTableName());
        long id = 0L;

        for (String link: links) {
            //LOGGER.info("img src: " + link);
            try {
                LOGGER.info("img src: " + link);

                URL url = new URL(link);
                URLConnection conn = url.openConnection();
                InputStream inStream = conn.getInputStream();
                if (null != inStream) {
                    String imgName = System.currentTimeMillis() + "_" + link.substring(link.lastIndexOf(LEFT_SLASH) + 1);
                    String imgFullName = config.getImagePath() + imgName;
                    //LOGGER.info("img name: " + imgName);
                    FileOutputStream fs = new FileOutputStream(imgFullName);

                    int byteread;
                    byte[] buffer = new byte[10240];
                    while ((byteread = inStream.read(buffer)) != -1) {
                        fs.write(buffer, 0, byteread);
                    }
                    fs.flush();
                    fs.close();
                    inStream.close();

                    //下载图片成功后，将图片src保存到数据库
                    URLEntity imgEntity = new URLEntity();
                    id++;
                    imgEntity.setId(startId + id);
                    imgEntity.setName(imgName);
                    imgEntity.setLevel(0L);
                    imgEntity.setUrl(link);
                    imgEntity.setIsUsed(1);
                    imgEntity.setContent(imgFullName);
                    imgEntity.setNotes(NOTES_IMAGE);
                    list.add(imgEntity);
                }
            } catch (MalformedURLException e) {
                LOGGER.error("MalformedURLException!", e);
            } catch (FileNotFoundException e) {
                LOGGER.error("FileNotFoundException!", e);
            } catch (IOException e) {
                //添加到黑名单
                link = link.substring(link.indexOf("//"), link.length());
                link = link.substring(0, link.indexOf("/"));
                blackList.add(link);
                LOGGER.error("IOException!", e);
            }
        }

        //save src to database
        mySQLHelper.saveUrlList(list);
        list.removeAll(list);

        //save blacklist to database
        if (blackList.size() > 0) {
            mySQLHelper.saveBlackList(blackList);
            blackList.removeAll(blackList);
        }
    }

}
