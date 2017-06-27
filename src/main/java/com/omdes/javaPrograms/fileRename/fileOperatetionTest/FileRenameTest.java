package com.omdes.javaPrograms.fileRename.fileOperatetionTest;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: solitary.wang
 * Date: 2017/5/10
 * Time: 13:35
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
 * 文件重命名方法 和 通过流复制文件重命名 效率比较
 */
public class FileRenameTest {

    public static void main(String[] args) {
        FileRenameTest test = new FileRenameTest();
        test.fileCopyRename();
        /*try {
            sleep(10000L);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }*/
        test.fileRename();
    }

    private void fileRename() {
        String path = "F:\\test\\myWeb\\101 - Copy.txt";
        String newName = "F:\\test\\myWeb\\javaRename.txt";
        File file = new File(path);
        Long startTime = 0L;
        Long endTime = 0L;
        if (file.exists()) {
            startTime = System.currentTimeMillis();
            file.renameTo(new File(newName));
            endTime = System.currentTimeMillis();
        }

        System.out.println("============== rename end ======================\n" + (endTime - startTime));
    }

    private void fileCopyRename(){
        String path = "F:\\test\\myWeb\\javaRename.txt";
        String newName = "F:\\test\\myWeb\\101 - Copy.txt";
        File file = new File(path);
        Long startTime = 0L;
        Long endTime = 0L;
        try {
            if (file.exists()) {
                startTime = System.currentTimeMillis();
                InputStream inputStream = new FileInputStream(path);
                FileOutputStream outputStream = new FileOutputStream(newName);

                byte[] buffer = new byte[1444];
                int bytesum = 0;
                int byteread = 0;
                while ( (byteread = inputStream.read(buffer)) != -1) {
                    //字节数 文件大小
                    bytesum += byteread;
                    outputStream.write(buffer, 0, byteread);
                }
                inputStream.close();
                file.delete();

                //输出文件大小（字节）
                System.out.println(bytesum);
                endTime = System.currentTimeMillis();
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("============== copy end ======================\n" + (endTime - startTime));
    }
}
