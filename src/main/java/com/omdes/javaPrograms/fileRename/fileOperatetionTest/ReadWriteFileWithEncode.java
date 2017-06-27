package com.omdes.javaPrograms.fileRename.fileOperatetionTest;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: solitary.wang
 * Date: 2017/5/9
 * Time: 8:57
 *
 *                     _ooOoo_
 *                    o8888888o
 *                    88" . "88
 *                    (| -_- |)
 *                    O\  =  /O
 *                 ____/`---'\____
 *               .'  \\|     |//  `.
 *              /  \\|||  :  |||//  \
 *             /  _||||| -:- |||||-  \
 *             |   | \\\  -  /// |   |
 *             | \_|  ''\---/''  |   |
 *             \  .-\__  `-`  ___/-. /
 *           ___`. .'  /--.--\  `. . __
 *        ."" '<  `.___\_<|>_/___.'  >'"".
 *      | | :  `- \`.;`\ _ /`;.`/ - ` : | |
 *      \  \ `-.   \_ __\ /__ _/   .-` /  /
 * ======`-.____`-.___\_____/___.-`____.-'======
 *                    `=---='
 * ^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
 *            佛祖保佑        永无BUG
 *  佛曰:
 *          写字楼里写字间，写字间里程序员；
 *          程序人员写程序，又拿程序换酒钱。
 *          酒醒只在网上坐，酒醉还来网下眠；
 *          酒醉酒醒日复日，网上网下年复年。
 *          但愿老死电脑间，不愿鞠躬老板前；
 *          奔驰宝马贵者趣，公交自行程序员。
 *          别人笑我忒疯癫，我笑自己命太贱；
 *          不见满街漂亮妹，哪个归得程序员？
 *
 * 可以指定编码如：utf-8来写入和读取文件。如果文件编码未知，可以通过该方法先得到文件的编码后再指定正确的编码来读取，否则会出现文件乱码问题。
 */
public class ReadWriteFileWithEncode {
    private static final String ENCODE = "utf-8";

    public static void write(String path, String content, String encoding)
            throws IOException {
        File file = new File(path);
        file.delete();
        file.createNewFile();
        BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
                new FileOutputStream(file), encoding));
        writer.write(content);
        writer.close();
    }

    public static String read(String path, String encoding) throws IOException {
        String content = "";
        File file = new File(path);
        BufferedReader reader = new BufferedReader(new InputStreamReader(
                new FileInputStream(file), encoding));
        String line = null;
        while ((line = reader.readLine()) != null) {
            content += line + "\n";
        }
        reader.close();
        return content;
    }

    public static void main(String[] args) throws IOException {
        String content = "中文内容";
        String path = "F:/test/myWeb/test.txt";
        ReadWriteFileWithEncode.write(path, content, ENCODE);
        System.out.println(ReadWriteFileWithEncode.read(path, ENCODE));
    }
}

    /*
                   _ooOoo_
                  o8888888o
                  88" . "88
                  (| -_- |)
                  O\  =  /O
               ____/`---'\____
             .'  \\|     |//  `.
            /  \\|||  :  |||//  \
           /  _||||| -:- |||||-  \
           |   | \\\  -  /// |   |
           | \_|  ''\---/''  |   |
           \  .-\__  `-`  ___/-. /
         ___`. .'  /--.--\  `. . __
      ."" '<  `.___\_<|>_/___.'  >'"".
     | | :  `- \`.;`\ _ /`;.`/ - ` : | |
     \  \ `-.   \_ __\ /__ _/   .-` /  /
======`-.____`-.___\_____/___.-`____.-'======
                   `=---='
^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^^
           佛祖保佑        永无BUG
佛曰:
         写字楼里写字间，写字间里程序员；
         程序人员写程序，又拿程序换酒钱。
         酒醒只在网上坐，酒醉还来网下眠；
         酒醉酒醒日复日，网上网下年复年。
         但愿老死电脑间，不愿鞠躬老板前；
         奔驰宝马贵者趣，公交自行程序员。
         别人笑我忒疯癫，我笑自己命太贱；
         不见满街漂亮妹，哪个归得程序员？
     */