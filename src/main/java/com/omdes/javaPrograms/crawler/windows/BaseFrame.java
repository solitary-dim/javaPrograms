package com.omdes.javaPrograms.crawler.windows;

import javax.swing.JFrame;
import javax.swing.WindowConstants;

/**
 * Created with IntelliJ IDEA.
 * User: Solitary.Wang
 * Date: 2017/7/2
 * Time: 8:11
 */
public class BaseFrame extends JFrame {
    public final JFrame jFrame = new JFrame();

    public BaseFrame() {
        this.jFrame.setVisible(true);
        this.jFrame.setResizable(false);
        this.jFrame.setLocationRelativeTo(null);
        this.jFrame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

    public JFrame getjFrame() {
        return jFrame;
    }

    @Override
    public void setTitle(String title) {
        this.jFrame.setTitle(title);
    }

    @Override
    public void setSize(int width, int height) {
        this.jFrame.setSize(width, height);
    }

    @Override
    public void setResizable(boolean flag) {
        this.jFrame.setResizable(flag);
    }
}
