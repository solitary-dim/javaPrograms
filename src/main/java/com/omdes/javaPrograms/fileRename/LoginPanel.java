package com.omdes.javaPrograms.fileRename;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

/**
 * Created with IntelliJ IDEA.
 * User: Solitary.Wang
 * Date: 2017/6/27
 * Time: 20:59
 */
public class LoginPanel implements Serializable {
    private static final long serialVersionUID = -6492622093561905995L;

    private static final int LINE_SPACING = 10;

    private final JPanel jPanel = new JPanel();

    private JLabel title;
    private JLabel loginName;
    private JLabel password;
    private JTextField name;
    private JTextField pwd;

    public JButton loginBtn;
    public JButton resetBtn;

    public JTextField getPwd() {
        return pwd;
    }

    public JTextField getName() {
        return name;
    }

    protected JPanel createPanel(int width, int height) {
        jPanel.setSize(width, height);

        title = new JLabel("Input your account and password!");
        loginName = new JLabel("AccountName");
        password = new JLabel("Password");
        name = new JTextField(16);
        pwd = new JTextField(16);
        loginBtn = new JButton("Login");

        resetBtn = new JButton("Reset");

        resetBtn.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                name.setText(null);
                pwd.setText(null);
            }
        });

        GroupLayout layout = new GroupLayout(this.jPanel);
        //设置组件之间自动间距
        layout.setAutoCreateGaps(true);
        this.jPanel.setLayout(layout);

        //创建GroupLayout的垂直连续组，，越先加入的ParallelGroup，优先级级别越高。
        layout.setVerticalGroup(layout.createSequentialGroup().
                        addContainerGap().
                        addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).
                                addComponent(title, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)).
                        addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).
                        addGap(LINE_SPACING).//添加间隔
                        addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).
                        addComponent(loginName, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).
                        addComponent(name, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)).
                        addGap(LINE_SPACING).
                        addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).
                                addComponent(password, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).
                                addComponent(pwd, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)).
                        addGap(LINE_SPACING).
                        addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).
                                        addComponent(loginBtn, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).
                                        addComponent(resetBtn, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                        )
        );
        //创建GroupLayout的水平连续组，，越先加入的ParallelGroup，优先级级别越高。
        layout.setHorizontalGroup(layout.createSequentialGroup().
                        addGap((int) (width * 0.15)).
                        addGroup(layout.createParallelGroup().
                                addComponent(loginName, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).
                                addComponent(password, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)).
                        addPreferredGap(LayoutStyle.ComponentPlacement.RELATED).
                        addGroup(layout.createParallelGroup().
                                        addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup().
                                                        addComponent(title, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                        ).
                                        addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup().
                                                        addComponent(name, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                        ).
                                        addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup().
                                                        addComponent(pwd, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                        ).
                                        addGroup(GroupLayout.Alignment.LEADING, layout.createSequentialGroup().
                                                        addComponent(loginBtn).
                                                        addComponent(resetBtn)
                                        )
                        )
        );
        jPanel.add(title);
        jPanel.add(loginName);
        jPanel.add(name);
        jPanel.add(password);
        jPanel.add(pwd);
        jPanel.add(loginBtn);
        jPanel.add(resetBtn);

        return jPanel;
    }
}
