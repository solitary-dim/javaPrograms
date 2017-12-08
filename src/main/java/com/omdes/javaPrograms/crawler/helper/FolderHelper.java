package com.omdes.javaPrograms.crawler.helper;

import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: Solitary.Wang
 * Date: 2017/12/8
 * Time: 17:00
 * 判断文件夹是否存在，不存在则创建
 */
public final class FolderHelper {
    private FolderHelper() {
    }

    public static void folderJudge(String path) {
        File folder = new File(path);
        if (!folder.exists()) {
            //folder.mkdir(); //只能在已有目录中创建一层目录
            folder.mkdirs();//在已有目录中创建多层级目录
        }
    }
}
