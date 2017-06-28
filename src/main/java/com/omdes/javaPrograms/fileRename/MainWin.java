package com.omdes.javaPrograms.fileRename;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;

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
        MainWin mainWin = new MainWin();
        mainWin.init();
    }

    private void init() {
        JFrame mainWin = new JFrame("Login");

        mainWin.setVisible(true);
        mainWin.setSize(WIDTH, HEIGHT);
        mainWin.setLocationRelativeTo(null);
        mainWin.setResizable(false);
        mainWin.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        LoginPanel loginPanel = new LoginPanel();
        JPanel login = loginPanel.createPanel(WIDTH, HEIGHT);
        FileChoosePanel fileChoosePanel = new FileChoosePanel();
        JPanel fileChooser = fileChoosePanel.createPanel(WIDTH, HEIGHT);

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

        final OperationFile operationFile = fileChoosePanel.getFileInfo();
        fileChoosePanel.changeBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                operationFile.setPrefix(fileChoosePanel.prefix.getText().trim());
                copyRename(operationFile);
                System.out.println(operationFile.toString());
            }
        });
    }

    private void copyRename(OperationFile operationFile) {
        String sourcePath = operationFile.getSourcePath();
        String basePath = operationFile.getDestinationPath();
        String baseName = operationFile.getPrefix();
        String[] fileNames = operationFile.getOldFileNames();
        Long startTime = System.currentTimeMillis();
        if (null == fileNames || fileNames.length == 0) {
            return;
        }
        try {
            int index = 0;
            for (String name : fileNames) {
                String path = sourcePath + "\\" + name;
                System.out.println(path);
                File file = new File(path);
                if (file.exists()) {
                    InputStream inputStream = new FileInputStream(path);
                    String newName = basePath + "\\" + baseName + index + name.substring(name.indexOf('.'));
                    System.out.println(newName);
                    FileOutputStream outputStream = new FileOutputStream(newName);

                    byte[] buffer = new byte[1024];
                    int bytesum = 0;
                    int byteread = 0;
                    while ((byteread = inputStream.read(buffer)) != -1) {
                        //字节数 文件大小
                        bytesum += byteread;
                        outputStream.write(buffer, 0, byteread);
                    }
                    inputStream.close();
                    //file.delete();

                    //输出文件大小（字节）
                    System.out.println(bytesum);
                }
                index++;
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Long endTime = System.currentTimeMillis();

        System.out.println("============== copy end ======================\n" + (endTime - startTime));
    }
}
