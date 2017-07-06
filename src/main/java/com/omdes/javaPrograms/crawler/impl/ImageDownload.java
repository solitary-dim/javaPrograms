package com.omdes.javaPrograms.crawler.impl;

import com.omdes.javaPrograms.crawler.helper.MySQLHelper;
import com.omdes.javaPrograms.crawler.config.PropertiesConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.HashSet;
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

    //根据src将图片保存到本地
    public void imageDownload(Set<String> links) {
        for (String link: links) {
            //LOGGER.info("img src: " + link);
            try {
                //去除问号及后面内容
                if (link.contains(QUESTION_MARK_STRING)) {
                    link = link.substring(0, link.indexOf(QUESTION_MARK_STRING));
                }
                if (link.contains(AND_STRING) || link.contains(PHP)) {
                    return;
                }
                LOGGER.info("img src: " + link);

                URL url = new URL(link);
                URLConnection conn = url.openConnection();
                InputStream inStream = conn.getInputStream();
                if (null != inStream) {
                    String imgName = link.substring(link.lastIndexOf(LEFT_SLASH) + 1);
                    imgName = config.getImagePath() + System.currentTimeMillis() + "_" + imgName;
                    //LOGGER.info("img name: " + imgName);
                    FileOutputStream fs = new FileOutputStream(imgName);

                    int byteread;
                    byte[] buffer = new byte[10240];
                    while ((byteread = inStream.read(buffer)) != -1) {
                        fs.write(buffer, 0, byteread);
                    }
                    fs.flush();
                    fs.close();
                    inStream.close();
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

        if (blackList.size() > 0) {
            saveBlackList();
            blackList.removeAll(blackList);
        }
    }

    //记录黑名单操作
    private void saveBlackList() {
        long id = mySQLHelper.getIdStart("T_BLACKLIST");

        String sql = "INSERT INTO T_BLACKLIST (ID, NAME, COUNT, NOTES) VALUES (?,?,?,?)";

        try {
            mySQLHelper.openConnection();
            Connection connection = mySQLHelper.getConnection();
            PreparedStatement pstmt = connection.prepareStatement(sql);
            connection.setAutoCommit(false);
            for (String str: blackList) {
                pstmt.setLong(1, id);
                pstmt.setString(2, str);
                pstmt.setInt(3, 1);
                pstmt.setString(4, "");
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
