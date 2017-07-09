package com.omdes.javaPrograms.crawler.impl;

import com.omdes.javaPrograms.crawler.config.PropertiesConfig;
import com.omdes.javaPrograms.crawler.entity.BlackEntity;
import com.omdes.javaPrograms.crawler.entity.URLEntity;
import com.omdes.javaPrograms.crawler.helper.MySQLHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
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
        startId = mySQLHelper.getIdStart(config.getUrlTableName());
        long id = 0L;

        for (String link : links) {
            LOGGER.info("img src: " + link);
            try {
                URL url = new URL(link);
                URLConnection conn = url.openConnection();
                InputStream inStream = conn.getInputStream();
                if (null != inStream) {
                    String imgName = System.currentTimeMillis() + "_" + link.substring(link.lastIndexOf(LEFT_SLASH) + 1);
                    String imgFullName = config.getImagePath() + imgName;
                    //LOGGER.info("img name: " + imgName);
                    FileOutputStream fs = new FileOutputStream(imgFullName);

                    int imgSize = 0;
                    int byteRead;
                    byte[] buffer = new byte[10240];
                    while ((byteRead = inStream.read(buffer)) != -1) {
                        imgSize += byteRead;
                        fs.write(buffer, 0, byteRead);
                    }
                    fs.flush();
                    fs.close();
                    inStream.close();

                    //如果图片过小，则删除，并且不保存src
                    if (imgSize < 2 * 1024) {
                        new File(imgFullName).delete();
                        continue;
                    }

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
                link = link.substring(link.indexOf(DOUBLE_LEFT_SLASH) + 2);
                blackList.add(link.substring(0, link.indexOf(LEFT_SLASH)));
                LOGGER.error("IOException!", e);
            }
        }

        //save src to database
        mySQLHelper.saveUrlList(list);
        LOGGER.info("save images' src into MySQL: " + list.size());
        list.removeAll(list);

        //save blacklist to database
        if (blackList.size() > 0) {
            long beginId = mySQLHelper.getIdStart(config.getBlackTableName());
            int index = 0;
            List<BlackEntity> blackEntities = new ArrayList<>();
            for (String name : blackList) {
                index++;
                BlackEntity entity = new BlackEntity();
                entity.setId(beginId + index);
                entity.setName(name);
                entity.setCount(1);
                entity.setNotes(NOTES_IMAGE);
                blackEntities.add(entity);
            }
            mySQLHelper.saveBlackList(blackEntities);
            blackList.removeAll(blackList);
        }
    }

}
