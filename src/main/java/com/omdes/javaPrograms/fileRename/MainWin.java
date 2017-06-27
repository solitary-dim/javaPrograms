package com.omdes.javaPrograms.fileRename;

import javax.swing.*;

/**
 * Created with IntelliJ IDEA.
 * User: Solitary.Wang
 * Date: 2017/6/27
 * Time: 21:06
 */
public final class MainWin {

    public static void main(String[] args) {
        JPanel jPanel = new LoginPanel().loginPanel();
        MainWin win = new MainWin();
        win.init(jPanel);
    }

    private void init(JPanel jPanel) {
        JFrame mainWin = new JFrame("Login");
        mainWin.add(jPanel);
        mainWin.setVisible(true);
        mainWin.setSize(500, 500);
        mainWin.setLocationRelativeTo(null);
        mainWin.setResizable(false);
        mainWin.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }
}
