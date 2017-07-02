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

import static com.omdes.javaPrograms.crawler.Config.LEFT_SLASH;
import static com.omdes.javaPrograms.crawler.Config.IMAGE_PATH;

/**
 * Created with IntelliJ IDEA.
 * User: Solitary.Wang
 * Date: 2017/7/2
 * Time: 9:33
 */
public final class ImageDownload {
    private static final Logger LOGGER = LoggerFactory.getLogger(ImageDownload.class);

    public void imageDownload(Set<String> links) {
        for (String link: links) {
            try {
                URL url = new URL(link);
                URLConnection conn = url.openConnection();
                InputStream inStream = conn.getInputStream();
                String imgName = link.substring(link.lastIndexOf(LEFT_SLASH));
                LOGGER.info("img name: " + imgName);
                FileOutputStream fs = new FileOutputStream(IMAGE_PATH + imgName);

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
