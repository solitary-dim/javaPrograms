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

    protected final JPanel jPanel = new JPanel();

    private JLabel title;
    private JLabel loginName;
    private JLabel password;
    private JTextField name;
    private JTextField pwd;
    private JButton login;
    private JButton reset;

    protected JPanel loginPanel() {

        title = new JLabel("Input your account and password!");
        loginName = new JLabel("AccountName");
        password = new JLabel("Password");
        name = new JTextField(16);
        pwd = new JTextField(16);
        login = new JButton("Login");

        login.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                System.out.println(name.getText() + "||" + pwd.getText());
            }
        });

        reset = new JButton("Reset");

        reset.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                name.setText(null);
                pwd.setText(null);
            }
        });

        GroupLayout layout = new GroupLayout(this.jPanel);
        this.jPanel.setLayout(layout);

        //创建GroupLayout的垂直连续组，，越先加入的ParallelGroup，优先级级别越高。
        layout.setVerticalGroup(layout.createSequentialGroup().
                        addContainerGap().
                        addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).
                                addComponent(title, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)).
                        addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED).
                        addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).
                                addComponent(loginName, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).
                                addComponent(name, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)).
                        addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).
                                addComponent(password, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).
                                addComponent(pwd, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)).
                        addGroup(layout.createParallelGroup(GroupLayout.Alignment.BASELINE).
                                        addComponent(login, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).
                                        addComponent(reset, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                        )
        );
        //创建GroupLayout的水平连续组，，越先加入的ParallelGroup，优先级级别越高。
        layout.setHorizontalGroup(layout.createSequentialGroup().
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
                                                        addComponent(login, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE).
                                                        addComponent(reset, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                        )
                        )
        );
        jPanel.add(title);
        jPanel.add(loginName);
        jPanel.add(name);
        jPanel.add(password);
        jPanel.add(pwd);
        jPanel.add(login);
        jPanel.add(reset);
        jPanel.setSize(350, 180);

        return jPanel;
    }
}
