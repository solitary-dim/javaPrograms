package com.omdes.javaPrograms.fileRename.fileOperatetionTest;

import javax.swing.*;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.util.ArrayList;

/**
 * Created with IntelliJ IDEA.
 * User: solitary.wang
 * Date: 2017/5/12
 * Time: 11:39
 *
 * 生成验证码图片
 */
public class PictureShow extends JFrame implements ActionListener, Serializable {
    private static final long serialVersionUID = -7090270864597297696L;

    private JButton browse;
    private JButton inAlbum;
    private JLabel picShow;     //显示图片的区域
    private JButton next;
    private JButton previous;
    private JTextField picPath;
    private JLabel open;
    private int count = 0;
    private int num = 0;
    private ArrayList<String> list = new ArrayList<String>();

    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                PictureShow inst = new PictureShow();
                inst.setLocationRelativeTo(null);
                inst.setVisible(true);
            }
        });
    }

    public PictureShow() {
        super();
        initGUI();
    }

    private void initGUI() {

        try {
            GroupLayout thisLayout = new GroupLayout((JComponent) getContentPane());
            getContentPane().setLayout(thisLayout);
            setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            setTitle("图片显示V1.0");
            {
                open = new JLabel();
                //打开
                open.setText("\u6253\u5f00");
            }
            {
                picPath = new JTextField();
            }
            {
                //浏览....
                browse = new JButton();
                browse.setText("\u6d4f\u89c8...");
                browse.addActionListener(this);
            }
            {
                //放入相册
                inAlbum = new JButton();
                inAlbum.setText("\u653e\u5165\u76f8\u518c");
                inAlbum.addActionListener(this);
            }
            {
                //显示图片的区域
                picShow = new JLabel();
                picShow.setBorder(new LineBorder(new Color(0, 0, 0), 1, false));
                picShow.setSize(600, 450);
            }
            {
                //上一张
                previous = new JButton();
                previous.setText("\u4e0a\u4e00\u5f20");
                previous.addActionListener(this);
            }
            {
                //下一张
                next = new JButton();
                next.setText("\u4e0b\u4e00\u5f20");
                next.addActionListener(this);
            }
            thisLayout.setVerticalGroup(thisLayout.createSequentialGroup()
                    .addContainerGap()
                    .addGroup(thisLayout.createParallelGroup(GroupLayout.Alignment.BASELINE)
                            .addComponent(open, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 20, GroupLayout.PREFERRED_SIZE)
                            .addComponent(picPath, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(browse, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                            .addComponent(inAlbum, GroupLayout.Alignment.BASELINE, GroupLayout.PREFERRED_SIZE, 22, GroupLayout.PREFERRED_SIZE))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.UNRELATED)
                    .addGroup(thisLayout.createParallelGroup()
                            .addComponent(picShow, GroupLayout.Alignment.LEADING, 0, 496, Short.MAX_VALUE)
                            .addGroup(thisLayout.createSequentialGroup()
                                    .addGap(210)
                                    .addGroup(thisLayout.createParallelGroup()
                                            .addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup()
                                                    .addComponent(previous, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                                    .addGap(0, 11, Short.MAX_VALUE))
                                            .addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup()
                                                    .addGap(11)
                                                    .addComponent(next, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE, GroupLayout.PREFERRED_SIZE)
                                                    .addGap(0, 0, Short.MAX_VALUE)))
                                    .addGap(253)))
                    .addContainerGap(24, 24));
            thisLayout.setHorizontalGroup(thisLayout.createSequentialGroup()
                    .addGroup(thisLayout.createParallelGroup()
                            .addComponent(previous, GroupLayout.Alignment.LEADING, GroupLayout.PREFERRED_SIZE, 79, GroupLayout.PREFERRED_SIZE)
                            .addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup()
                                    .addGap(24)
                                    .addComponent(open, GroupLayout.PREFERRED_SIZE, 54, GroupLayout.PREFERRED_SIZE)))
                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                    .addGroup(thisLayout.createParallelGroup()
                            .addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup()
                                    .addComponent(picShow, GroupLayout.PREFERRED_SIZE, 608, GroupLayout.PREFERRED_SIZE)
                                    .addPreferredGap(LayoutStyle.ComponentPlacement.RELATED)
                                    .addComponent(next, GroupLayout.PREFERRED_SIZE, 82, GroupLayout.PREFERRED_SIZE))
                            .addGroup(GroupLayout.Alignment.LEADING, thisLayout.createSequentialGroup()
                                    .addComponent(picPath, GroupLayout.PREFERRED_SIZE, 428, GroupLayout.PREFERRED_SIZE)
                                    .addGap(28)
                                    .addComponent(browse, GroupLayout.PREFERRED_SIZE, 98, GroupLayout.PREFERRED_SIZE)
                                    .addGap(19)
                                    .addComponent(inAlbum, GroupLayout.PREFERRED_SIZE, 109, GroupLayout.PREFERRED_SIZE)
                                    .addGap(0, 20, Short.MAX_VALUE))));
            pack();
            this.setSize(800, 600);
        } catch (Exception e) {
            //add your error handling code here
            e.printStackTrace();
        }

    }


    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub
        //浏览
        if (e.getSource() == browse) {

            JFileChooser fc = new JFileChooser();
            int returnVal = fc.showOpenDialog(this);
            if (returnVal == JFileChooser.APPROVE_OPTION) {
                //获取这个被选中的文件
                File f = fc.getSelectedFile();
                //文件名
                String fileName = f.getName();
                //文件绝对路径
                String filePath = fc.getSelectedFile().getAbsolutePath();
                //把路径显示在textfield中
                picPath.setText(filePath);
                //
                this.setTitle(fileName + "--图片查看器V1.0");
                //获取这个图片本身
                ImageIcon image = new ImageIcon(filePath);
                //获取图片的宽和高
                int width = image.getIconWidth();
                int height = image.getIconHeight();
                if (width > height) {
                    //宽图  设置成600*450
                    Image scaledImage = image.getImage().getScaledInstance(600, 450, Image.SCALE_DEFAULT);
                    ImageIcon scaledIcon = new ImageIcon(scaledImage);
                    picShow.setIcon(scaledIcon);
                    picShow.setHorizontalAlignment(0);
                    picShow.setVerticalAlignment(0);
                } else {
                    //高图  设置成300*450
                    Image scaledImage = image.getImage().getScaledInstance(300, 450, Image.SCALE_DEFAULT);
                    ImageIcon scaledIcon = new ImageIcon(scaledImage);
                    picShow.setIcon(scaledIcon);
                    picShow.setHorizontalAlignment(0);
                    picShow.setVerticalAlignment(0);
                }

                //下面的代码用于遍历同一个文件夹下的其他图片

                File[] fileList = f.getParentFile().listFiles();
                for (int i = 0; i < fileList.length; i++) {

                    if (fileList[i].isFile()) {

                        String[] part = fileList[i].getName().split("\\.");
                        if (part[1].equals("jpg") || part[1].equals("JPG") || part[1].equals("GIF") || part[1].equals("gif") || part[1].equals("png") || part[1].equals("PNG") || part[1].equals("bmp") || part[1].equals("BMP")) {

                            list.add(fileList[i].getAbsolutePath());
                            num++;

                            if (fileList[i].getAbsolutePath().equals(filePath)) {
                                count = num;
                            }
                        }

                    }

                }


            }
            //
        } else if (e.getSource() == previous) {
            if (count >= 1) {
                String path = list.get(count - 1);
                //System.out.println(path);
                ImageIcon image = new ImageIcon(path);
                int width = image.getIconWidth();
                int height = image.getIconHeight();
                if (width > height) {
                    Image scaledImage = image.getImage().getScaledInstance(600, 450, Image.SCALE_DEFAULT);
                    ImageIcon scaledIcon = new ImageIcon(scaledImage);
                    picShow.setIcon(scaledIcon);
                    picShow.setHorizontalAlignment(0);
                    picShow.setVerticalAlignment(0);

                } else {
                    Image scaledImage = image.getImage().getScaledInstance(300, 450, Image.SCALE_DEFAULT);
                    ImageIcon scaledIcon = new ImageIcon(scaledImage);
                    picShow.setIcon(scaledIcon);
                    picShow.setHorizontalAlignment(0);
                    picShow.setVerticalAlignment(0);
                }
                picPath.setText(path);
                File file = new File(path);
                this.setTitle(file.getName() + "--图片查看器V1.0");
                count--;
                //System.out.println(path+"\t"+count);
            } else {
                JOptionPane.showMessageDialog(null, "已经是第一张图片！");
            }
        } else if (e.getSource() == next) {
            if (count < list.size() - 1) {
                //System.out.println(list.get(0));
                String path = list.get(count + 1);
                ImageIcon image = new ImageIcon(path);
                int width = image.getIconWidth();
                int height = image.getIconHeight();
                if (width > height) {
                    Image scaledImage = image.getImage().getScaledInstance(600, 450, Image.SCALE_DEFAULT);
                    ImageIcon scaledIcon = new ImageIcon(scaledImage);
                    picShow.setIcon(scaledIcon);
                    picShow.setHorizontalAlignment(0);
                    picShow.setVerticalAlignment(0);
                } else {
                    Image scaledImage = image.getImage().getScaledInstance(300, 450, Image.SCALE_DEFAULT);
                    ImageIcon scaledIcon = new ImageIcon(scaledImage);
                    picShow.setIcon(scaledIcon);
                    picShow.setHorizontalAlignment(0);
                    picShow.setVerticalAlignment(0);
                }
                picPath.setText(path);
                File file = new File(path);
                this.setTitle(file.getName() + "--图片查看器V1.0");
                count++;
                //System.out.println(path+"\t"+count);
            } else {
                JOptionPane.showMessageDialog(null, "已经是最后一张图片！");
            }
        } else if (e.getSource() == inAlbum) {
            if (picPath.getText().equals("")) {
                JOptionPane.showMessageDialog(null, "请先打开一张图片！");
            } else {
                try {
                    FileInputStream fin = new FileInputStream(picPath.getText());
                    BufferedInputStream bin = new BufferedInputStream(fin);
                    String filename = (new File(picPath.getText())).getName();
                    FileOutputStream fou = new FileOutputStream("d://diffcult//" + filename);
                    BufferedOutputStream bou = new BufferedOutputStream(fou);
                    byte[] buff = new byte[1024 * 10];
                    int len;
                    while ((len = bin.read(buff)) != -1) {
                        bou.write(buff, 0, len);
                    }
                    bou.flush();
                    bou.close();
                    bin.close();
                    fou.close();
                    fin.close();
                    (new File(picPath.getText())).delete();
                    list.remove(count);
                    JOptionPane.showMessageDialog(null, "该题目已经放进难题册！");
                } catch (IOException e1) {
                    JOptionPane.showMessageDialog(null, "文件流读写错误！");
                }

            }
        }


    }

}