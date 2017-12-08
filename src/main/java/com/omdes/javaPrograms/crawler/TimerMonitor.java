package com.omdes.javaPrograms.crawler;

import com.omdes.javaPrograms.crawler.config.PropertiesConfig;
import com.omdes.javaPrograms.crawler.helper.FolderHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Calendar;

/**
 * Created with IntelliJ IDEA.
 * User: Solitary.Wang
 * Date: 2017/12/9
 * Time: 0:49
 * 时间监控线程，以日期为名生成每日文件夹存储下载的图片
 */
public final class TimerMonitor extends Thread {
    private static final Logger LOGGER = LoggerFactory.getLogger(TimerMonitor.class);

    private PropertiesConfig config = PropertiesConfig.getInstance();

    private final String oldPath = config.getImagePath();

    private static int index;
    private static final int ONE_DAY = 86400000;

    public TimerMonitor(String name) {
        super(name);
    }

    @Override
    public void run() {
        //第一次创建今日文件夹
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        index = calendar.get(Calendar.DAY_OF_MONTH);
        String newPath = oldPath + year + month + index;
        FolderHelper.folderJudge(newPath);
        config.setImagePath(newPath);

        while (true) {
            //LOGGER.info("image path: " + oldPath);
            //LOGGER.info("image path: " + config.getImagePath());
            long currentTime = System.currentTimeMillis();
            //LOGGER.info("time: " + currentTime);

            try {
                //如果是新的一天则睡眠到当天与再一个新的一天的交替时间点
                if (isNewDay(currentTime)) {
                    calendar.set(year, month, index + 1, 0, 0, 0);
                    Thread.sleep(1000);
                    //Thread.sleep(calendar.getTimeInMillis() - System.currentTimeMillis() + 1000);
                }
            } catch (InterruptedException e) {
                LOGGER.error("线程错误！\n", e);
            }
        }
    }

    /**
     * 判断是否进入新的一天，如果是，则创建新的文件夹
     * @param curr 当前时间的毫秒级计数
     * @return true-是/false-否
     */
    public boolean isNewDay(long curr) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(curr);
        int year = calendar.get(Calendar.YEAR);
        int month = calendar.get(Calendar.MONTH);
        int day = calendar.get(Calendar.DAY_OF_MONTH);

        boolean result = false;
        if (day != index) {
            index = day;
            String newPath = oldPath + year + month + index;
            FolderHelper.folderJudge(newPath);
            result = true;
        }
        return result;
    }
}
