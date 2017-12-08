package com.omdes.javaPrograms.crawler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * Created with IntelliJ IDEA.
 * User: Solitary.Wang
 * Date: 2017/7/8
 * Time: 20:33
 */
public final class MainWin {
    private static final Logger LOGGER = LoggerFactory.getLogger(MainRun.class);

    //窗口界面宽、高、组件之间间隔
    private static final int WIN_WIDTH =500;
    private static final int WIN_HEIGHT =500;
    private static final int LINE_SPACING = 10;

    //输入组件（jtext，jarea）宽度
    private static final int width = 30;

    private static final JFrame win = new JFrame();

    public static void main(String[] args) {
        new MainWin().initWin();
    }

    private void initWin() {
        win.setResizable(false);
        win.setTitle("百度图片下载爬虫!");
        win.setSize(WIN_WIDTH, WIN_HEIGHT);
        win.setLocationRelativeTo(null);
        win.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);

        JLabel urlLabel = new JLabel("输入网址：");
        JLabel keyLabel = new JLabel("搜索单词：");
        JLabel resLabel = new JLabel("搜索结果：");
        JTextField urlText = new JTextField(width);
        JTextField keyText = new JTextField(width);
        JTextArea listArea = new JTextArea(new Double(width * 3 / 4).intValue(), width);

        JButton searchBtn = new JButton("开始");
        searchBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                final String toCrawlerUrl = urlText.getText().trim();
                if (!"".equals(toCrawlerUrl)) {
                    final String toCrawlerKeyword = keyText.getText().trim();
                    if (!"".equals(toCrawlerKeyword)) {
                        String newCrawlerUrl = "https://image.baidu.com/search/index?tn=baiduimage&ipn=r&ct=201326592&cl=2&lm=-1&st=-1&fm=index&fr=&hs=0&xthttps=111111&sf=1&fmq=&pv=&ic=0&nc=1&z=&se=1&showtab=0&fb=0&width=&height=&face=0&istype=2&ie=utf-8&word=" + toCrawlerKeyword + "&oq=" + toCrawlerKeyword + "&rsp=-1";
                        MainRun.runCrawler(newCrawlerUrl);
                    } else {
                        MainRun.runCrawler(toCrawlerUrl);
                    }
                } else {
                    LOGGER.info("no input!");
                }
            }
        });
        JButton exitBtn = new JButton("退出");
        exitBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.exit(0);
            }
        });

        JPanel mainPanel = new JPanel();
        //JPanel高度和宽度，不然将其中组件将无法展现出来
        mainPanel.setSize(WIN_WIDTH, WIN_HEIGHT);

        //listArea.setSize(width, width);
        listArea.setEditable(false);
        listArea.setAutoscrolls(true);
        listArea.setBackground(Color.WHITE);

        //把定义的JTextArea放到JScrollPane里面去
        JScrollPane scroll = new JScrollPane(listArea);
        scroll.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
        scroll.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);

        GroupLayout layout = new GroupLayout(mainPanel);
        //设置组件之间自动间距
        layout.setAutoCreateGaps(true);
        mainPanel.setLayout(layout);

        //创建GroupLayout的垂直连续组，，越先加入的ParallelGroup，优先级级别越高。
        layout.setVerticalGroup(layout.createSequentialGroup().
                        addContainerGap().
                        addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).
                                addComponent(urlLabel, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).
                                addComponent(urlText, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)).
                        addGap(LINE_SPACING).
                        addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).
                                addComponent(keyLabel, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).
                                addComponent(keyText, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)).
                        addGap(LINE_SPACING).
                        addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).
                                addComponent(resLabel, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).
                                addComponent(scroll, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)).
                        addGap(LINE_SPACING).
                        addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).
                                addComponent(searchBtn, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).
                                addComponent(exitBtn, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE))
        );
        //创建GroupLayout的水平连续组，，越先加入的ParallelGroup，优先级级别越高。
        layout.setHorizontalGroup(layout.createSequentialGroup().
                        addGap(LINE_SPACING).
                        addGroup(layout.createParallelGroup().
                                addComponent(urlLabel, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).
                                addComponent(keyLabel, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).
                                addComponent(resLabel, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)).
                        addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).
                        addGroup(layout.createParallelGroup().
                                        addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup().
                                                        addComponent(urlText, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                        ).
                                        addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup().
                                                        addComponent(keyText, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                        ).
                                        addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup().
                                                        addComponent(scroll, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                        ).
                                        addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup().
                                                        addComponent(searchBtn, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).
                                                        addComponent(exitBtn, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                        ))
        );

        mainPanel.add(urlLabel);
        mainPanel.add(keyLabel);
        mainPanel.add(resLabel);
        mainPanel.add(urlText);
        mainPanel.add(keyText);
        mainPanel.add(scroll);
        mainPanel.add(searchBtn);
        mainPanel.add(exitBtn);

        win.setLayout(null);
        win.add(mainPanel);
        win.setVisible(true);
    }
}
