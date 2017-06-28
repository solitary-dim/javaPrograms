package com.omdes.javaPrograms.fileRename;

/**
 * Created with IntelliJ IDEA.
 * User: Solitary.Wang
 * Date: 2017/6/28
 * Time: 6:05
 */
public final class Helper {

    /**
     * 判断String是空字符串
     *
     * @param str
     * @return true-是空字符串；false-不是空字符串
     */
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

    /**
     * 判断String不是空字符串
     *
     * @param str
     * @return true-不是空字符串；false-是空字符串
     */
    public static boolean stringIsNotEmpty(String str) {
        return !stringIsEmpty(str);
    }

    /**
     * 向已有未满数组中追加元素
     *
     * @param arr 被追加数组
     * @param str 新追加元素
     * @param flag 是否允许重复值
     * @return 追加新元素后数组
     */
    public static String[] arrayAddElement(String[] arr, String str, boolean flag) {
        if (null == arr[0]) {
            arr[0] = str;
            return arr;
        }

        int i = 0;
        for (int j = 0; j < arr.length; j++) {
            if (null == arr[j]) {
                i = j;
                break;
            }
        }

        //判断新追加元素是否在数组中已经存在
        if (flag) {
            for (String s : arr) {
                if (s.equals(str)) {
                    return arr;
                }
            }
        }

        arr[i] = str;

        return arr;
    }

}
