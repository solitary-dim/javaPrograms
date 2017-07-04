package com.omdes.javaPrograms.crawler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Set;

import static com.omdes.javaPrograms.crawler.BaseConfig.LEFT_SLASH;
import static com.omdes.javaPrograms.crawler.BaseConfig.QUESTION_MARK_CHAR;
import static com.omdes.javaPrograms.crawler.BaseConfig.QUESTION_MARK_STRING;

/**
 * Created with IntelliJ IDEA.
 * User: Solitary.Wang
 * Date: 2017/7/2
 * Time: 9:33
 */
public final class ImageDownload {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageDownload.class);

    private PropertiesConfig config = PropertiesConfig.getInstance();

    //根据src将图片保存到本地
    public void imageDownload(Set<String> links) {
        for (String link: links) {
            LOGGER.info("img src: " + link);
            try {
                //去除问号及后面内容
                if (link.contains(QUESTION_MARK_STRING)) {
                    link = link.substring(0, link.indexOf(QUESTION_MARK_CHAR));
                }

                URL url = new URL(link);
                URLConnection conn = url.openConnection();
                InputStream inStream = conn.getInputStream();
                String imgName = link.substring(link.lastIndexOf(LEFT_SLASH));
                //LOGGER.info("img name: " + imgName);
                FileOutputStream fs = new FileOutputStream(config.getImagePath() + imgName);

                int byteread;
                byte[] buffer = new byte[1204];
                while ((byteread = inStream.read(buffer)) != -1) {
                    fs.write(buffer, 0, byteread);
                }
            } catch (MalformedURLException e) {
                LOGGER.error("MalformedURLException!", e);
            } catch (FileNotFoundException e) {
                LOGGER.error("FileNotFoundException!", e);
            } catch (IOException e) {
                LOGGER.error("IOException!", e);
            }
        }
    }

}
