package com.omdes.javaPrograms.fileRename;

/**
 * Created with IntelliJ IDEA.
 * User: Solitary.Wang
 * Date: 2017/6/28
 * Time: 6:05
 */
public final class Helper {

    public static boolean stringIsEmpty(String str) {
        if (null == str) {
            return true;
        }
        str = str.trim();
        if ("".equals(str) || str.length() == 0) {
            return true;
        }
        return false;
    }

    public static boolean stringIsNotEmpty(String str) {
        return !stringIsEmpty(str);
    }

}
