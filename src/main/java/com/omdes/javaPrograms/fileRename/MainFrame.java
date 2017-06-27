package com.omdes.javaPrograms.fileRename;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: solitary.wang
 * Date: 2017/5/12
 * Time: 11:05
 */
public class MainFrame {

    public static void main(String[] args) {
        JFrame jf = new JFrame();

        JPanel jPanel = new JPanel();
        JTextField jTextField = new JTextField(10);
        JButton jButton = new JButton("choose");
        jButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser jFileChooser = new JFileChooser();
                //true-select multi files
                jFileChooser.setMultiSelectionEnabled(true);
                /*String dirName = jFileChooser.getCurrentDirectory().toString().trim();
                if (dirName == null || dirName.trim().length() == 0) {
                    jFileChooser.setCurrentDirectory(new File("."));
                }else{
                    jFileChooser.setCurrentDirectory(new File(dirName));
                }*/
                int rtVal = jFileChooser.showOpenDialog(new JDialog());
                if (rtVal == JFileChooser.APPROVE_OPTION) {
                    File[] files = jFileChooser.getSelectedFiles();
                    String path = "";
                    for (File file : files) {
                        path += file.getAbsolutePath() + "\n";
                        //多文件选择，得到多文件的名称，及路径
                        System.out.println(file.getName() + ": " + file.getAbsolutePath());
                    }
                    System.out.println("=============================================");
                    //path = path.substring(0, (path.length()-2));
                    System.out.println(path);
                    jTextField.setText(path);
                }
            }
        });
        jPanel.add(jTextField);
        jPanel.add(jButton);

        jf.add(jPanel);
        jf.setTitle("test");
        jf.setVisible(true);
        jf.setSize(500, 500);
        jf.setLocationRelativeTo(null);
        jf.setResizable(false);
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
    }

}
