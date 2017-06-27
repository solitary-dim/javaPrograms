package com.omdes.javaPrograms.fileRename.jframe;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: solitary.wang
 * Date: 2016/12/9
 * Time: 17:17
 */
public class baseWindow {

    public static void main(String[] args) {
        JFrame jf = new JFrame();
        jf.setTitle("test");
        jf.setVisible(true);
        jf.setSize(500, 500);
        jf.setLocationRelativeTo(null);
        jf.setResizable(false);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
/*
JFrame居中显示在整个屏幕的正中位置

方法一:
JFrame frame = new JFrame("TEST");
frame.setSize(200,100) Toolkit toolkit = Toolkit.getDefaultToolkit();
int x = (int)(toolkit.getScreenSize().getWidth()-f.getWidth())/2;
int y = (int)(toolkit.getScreenSize().getHeight()-f.getHeight())/2;
frame.setLocation(x, y);
frame.setVisible(true);

方法二:
JFrame frame = new JFrame("TEST");
frame.setLocationRelativeTo(null);
//传入参数null 即可让JFrame 位于屏幕中央, 这个函数若传入一个Component ,则JFrame位于该组件的中央
*/
