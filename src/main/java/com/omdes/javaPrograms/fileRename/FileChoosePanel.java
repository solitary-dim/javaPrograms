package com.omdes.javaPrograms.fileRename;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

/**
 * Created with IntelliJ IDEA.
 * User: Solitary.Wang
 * Date: 2017/6/28
 * Time: 5:12
 */
public class FileChoosePanel {

    public OperationFile getFileInfo() {
        return this.operationFile;
    }

    private static final int LINE_SPACING = 10;

    private final JPanel jPanel = new JPanel();

    private final OperationFile operationFile = new OperationFile();

    private JLabel sourceLabel = new JLabel("选择需要批量重命名文件（可多选）");
    private JLabel destinationLabel = new JLabel("选择目标文件夹");
    private JLabel prefixLabel = new JLabel("设置重命名是前缀");
    private JTextArea selectedFiles = new JTextArea(16, 16);
    private JTextField destinationPath = new JTextField(16);
    private JButton sourcesBtn = new JButton("choose");
    private JButton destinationBtn = new JButton("choose");

    public JTextField prefix = new JTextField(16);
    public JButton changeBtn = new JButton("rename");

    protected JPanel createPanel(int width, int height) {
        jPanel.setSize(width, height);
        jPanel.setLayout(new FlowLayout());

        selectedFiles.setSize(16, 16);
        selectedFiles.setEditable(false);
        selectedFiles.setAutoscrolls(true);
        selectedFiles.setBackground(Color.WHITE);

        //把定义的JTextArea放到JScrollPane里面去
        JScrollPane scroll = new JScrollPane(selectedFiles);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        destinationPath.setEditable(false);
        destinationPath.setBackground(Color.WHITE);

        sourcesBtn.addActionListener(sourceBtnListener);

        destinationBtn.addActionListener(destinationBtnListener);

        GroupLayout layout = new GroupLayout(this.jPanel);
        //设置组件之间自动间距
        layout.setAutoCreateGaps(true);
        this.jPanel.setLayout(layout);

        //创建GroupLayout的垂直连续组，，越先加入的ParallelGroup，优先级级别越高。
        layout.setVerticalGroup(layout.createSequentialGroup().
                        addContainerGap().
                        addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).
                                addComponent(sourceLabel, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).
                                addComponent(scroll, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).
                                addComponent(sourcesBtn, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)).
                        addGap(LINE_SPACING).
                        addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).
                                addComponent(destinationLabel, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).
                                addComponent(destinationPath, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).
                                addComponent(destinationBtn, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)).
                        addGap(LINE_SPACING).
                        addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).
                                addComponent(prefixLabel, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).
                                addComponent(prefix, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).
                                addComponent(changeBtn, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
        );
        //创建GroupLayout的水平连续组，，越先加入的ParallelGroup，优先级级别越高。
        layout.setHorizontalGroup(layout.createSequentialGroup().
                        addGap(LINE_SPACING).
                        addGroup(layout.createParallelGroup().
                                addComponent(sourceLabel, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).
                                addComponent(destinationLabel, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).
                                addComponent(prefixLabel, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)).
                        addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).
                        addGroup(layout.createParallelGroup().
                                addComponent(scroll, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).
                                addComponent(destinationPath, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).
                                addComponent(prefix, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)).
                        addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).
                        addGroup(layout.createParallelGroup().
                                addComponent(sourcesBtn, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).
                                addComponent(destinationBtn, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).
                                addComponent(changeBtn, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
        );

        jPanel.add(sourceLabel);
        jPanel.add(scroll);
        jPanel.add(sourcesBtn);
        jPanel.add(destinationLabel);
        jPanel.add(destinationPath);
        jPanel.add(destinationBtn);

        return jPanel;
    }

    private ActionListener sourceBtnListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.setMultiSelectionEnabled(true);
            jFileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
            int rtVal = jFileChooser.showOpenDialog(new JDialog());
            if (rtVal == JFileChooser.APPROVE_OPTION) {
                File[] files = jFileChooser.getSelectedFiles();
                String path = "";

                String[] fileNames = new String[files.length];
                String[] filePaths = new String[files.length];

                for (File file : files) {
                    if (file.isFile()) {
                        fileNames = Helper.arrayAddElement(fileNames, file.getName(), false);
                        filePaths = Helper.arrayAddElement(filePaths, file.getParent(), true);
                    } else {
                        JOptionPane.showMessageDialog(null, "只能选择文件！", "提示", JOptionPane.INFORMATION_MESSAGE);
                    }

                    path += file.getAbsolutePath() + "\n";
                }

                operationFile.setOldFileNames(fileNames);
                operationFile.setSourcePaths(filePaths);
                operationFile.setSourcePath(operationFile.getSourcePaths()[0]);
                operationFile.setFileCount(files.length);

                selectedFiles.setText(path);
            }
        }
    };

    private ActionListener destinationBtnListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFileChooser jFileChooser = new JFileChooser();
            jFileChooser.setMultiSelectionEnabled(false);
            jFileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
            int rtVal = jFileChooser.showOpenDialog(new JDialog());
            if (rtVal == JFileChooser.APPROVE_OPTION) {
                File file = jFileChooser.getSelectedFile();
                if (file.isDirectory()) {
                    operationFile.setDestinationPath(file.getPath());
                    destinationPath.setText(file.getPath());
                } else {
                    JOptionPane.showMessageDialog(null, "只能选择文件夹！", "提示", JOptionPane.INFORMATION_MESSAGE);
                }
            }
        }
    };
}
