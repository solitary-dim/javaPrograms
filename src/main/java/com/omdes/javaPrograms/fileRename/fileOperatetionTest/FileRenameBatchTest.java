package com.omdes.javaPrograms.fileRename.fileOperatetionTest;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created with IntelliJ IDEA.
 * User: solitary.wang
 * Date: 2017/5/10
 * Time: 15:40
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
 */
public class FileRenameBatchTest {
    private static final String FOLDER_PATH = "F:\\test\\myWeb\\test";
    private static final String NEW_NAME = "img";
    //时间戳
    private static final String TIME_STAMP = new SimpleDateFormat("yyyyMMdd").format(new Date());
    //前缀
    private static final String PREFIX = "";

    public static void main(String[] args) {
        FileRenameBatchTest test = new FileRenameBatchTest();
        test.renameBatch();
    }

    private void renameBatch() {
        File folder = new File(FOLDER_PATH);
        //判断是否是文件夹
        if (folder.isDirectory()) {
            File[] files = folder.listFiles();
            //判断文件夹下是否为空
            if (null != files && files.length > 0) {
                boolean flag = false;
                int failCount = 0;
                int index = 0;
                for (File file : files) {
                    index++;
                    //后缀名（文件扩展名）
                    String extension = file.getPath();
                    extension = extension.substring(extension.indexOf("."));
                    boolean result = file.renameTo(new File(FOLDER_PATH + "\\" + NEW_NAME + TIME_STAMP + "_" + index + extension));
                    if (!result) {
                        flag = true;
                        failCount++;
                    }
                }
                //如果有重命名失败的情况
                if (flag) {
                    System.out.println("有【" + failCount + "】个文件重命名失败！");
                } else {
                    System.out.println("一共重命名【" + (files.length - failCount) + "】个文件！");
                }
            }
        }
    }
}
