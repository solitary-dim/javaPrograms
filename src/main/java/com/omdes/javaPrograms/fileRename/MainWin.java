package com.omdes.javaPrograms.fileRename;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: Solitary.Wang
 * Date: 2017/6/27
 * Time: 21:06
 */
public final class MainWin {

    private static final int WIDTH = 500;
    private static final int HEIGHT = 500;

    public static void main(String[] args) {
        JFrame mainWin = new JFrame("Login");

        mainWin.setVisible(true);
        mainWin.setSize(WIDTH, HEIGHT);
        mainWin.setLocationRelativeTo(null);
        mainWin.setResizable(false);
        mainWin.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        LoginPanel loginPanel = new LoginPanel();
        JPanel login = loginPanel.loginPanel(WIDTH, HEIGHT);
        FileChoosePanel fileChoosePanel = new FileChoosePanel();
        JPanel fileChooser = fileChoosePanel.fileChoosePanel(WIDTH, HEIGHT);

        CardLayout layout = new CardLayout();

        mainWin.setLayout(layout);
        mainWin.add(login);
        mainWin.add(fileChooser);

        loginPanel.loginBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (Helper.stringIsNotEmpty(loginPanel.getName().getText()) &&
                        Helper.stringIsNotEmpty(loginPanel.getPwd().getText())) {
                    layout.next(mainWin.getContentPane());
                }
            }
        });

    }
}
