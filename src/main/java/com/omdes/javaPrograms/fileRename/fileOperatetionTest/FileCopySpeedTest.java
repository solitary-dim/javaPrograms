package com.omdes.javaPrograms.fileRename.fileOperatetionTest;

import java.io.*;
import java.nio.channels.FileChannel;

/**
 * Created with IntelliJ IDEA.
 * User: solitary.wang
 * Date: 2016/12/8
 * Time: 14:05
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
public class FileCopySpeedTest {

    /*
    管道操作文件
     */
    public void fileChannelCopy(File s, File t) {
        FileInputStream fi = null;
        FileOutputStream fo = null;
        FileChannel in = null;
        FileChannel out = null;
        try {
            fi = new FileInputStream(s);
            fo = new FileOutputStream(t);
            //得到对应的文件通道
            in = fi.getChannel();
            //得到对应的文件通道
            out = fo.getChannel();
            //连接两个通道，并且从in通道读取，然后写入out通道
            in.transferTo(0, in.size(), out);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fi.close();
                in.close();
                fo.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /*
    普通的缓冲输入输出流操作文件
     */
    private void copy(File s, File t) {
        InputStream fis = null;
        OutputStream fos = null;
        try {
            fis = new BufferedInputStream(new FileInputStream(s));
            fos = new BufferedOutputStream(new FileOutputStream(t));
            byte[] buf = new byte[2048];
            int i;
            while ((i=fis.read(buf)) != -1) {
                fos.write(buf, 0, i);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try{
                fis.close();
                fos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        //2M左右的文件，操作速度最为接近；
        //文件越小，buffer的性能表现越优；
        //文件越大，channel的性能表现越优
        FileCopySpeedTest fo = new FileCopySpeedTest();
        File s = new File("F:\\test\\myWeb\\01.txt");
        File t = new File("F:\\test\\myWeb\\02.txt");
        File t2 = new File("F:\\test\\myWeb\\03.txt");
        long start, end;

        start = System.currentTimeMillis();
        fo.fileChannelCopy(s, t);
        end = System.currentTimeMillis();
        System.out.println("FileChannel copy file : " + (end - start));

        start = System.currentTimeMillis();
        fo.copy(s, t2);
        end = System.currentTimeMillis();
        System.out.println("Buffer copy file : " + (end - start));
    }
}
