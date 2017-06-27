package com.omdes.javaPrograms.fileRename.fileOperatetionTest;

import java.io.*;

/**
 * Created with IntelliJ IDEA.
 * User: solitary.wang
 * Date: 2016/12/8
 * Time: 14:19
 */
public class testFile {
    private static final String path = "F:\\test\\myWeb\\";
    private static final String fileName = "test-1.txt";
    /**
     * file(内存)----输入流---->【程序】----输出流---->file(内存)
     */
    public static void main(String[] args) {
        // TODO Auto-generated method stub

        long begin = System.currentTimeMillis();
        System.out.println("start time: " + begin);

        File file = new File(path, fileName);
        System.out.println(file.getPath());
        System.out.println(file.getParent());
        System.out.println(file.getParentFile());

        testFile tt = new testFile();
        //tt.createFile(file);
        //tt.deleteFile(file);
        //tt.updateFile(file);
        //tt.readFile(file);
        //tt.copyFolder("F:\\test\\myWeb", "F:\\test\\test");
        tt.moveFolder("F:\\test\\myWeb", "F:\\test\\test");

        long end = System.currentTimeMillis();
        System.out.println("end time: " + end);
        System.out.println("use time: " + (end - begin) + "millisecond");
    }

    /**
     * 新建文件
     * @param file
     */
    private void createFile(File file) {
        //创建文件上层所有文件夹
        if (!file.exists()) {
            //mkdirs()可以创建多层多个文件夹
            file.getParentFile().mkdirs();
        }

        /*//判断文件夹是否存在，不存在则创建文件夹
        if (!file.exists()) {
            //mkdir()只能创建一层一个文件夹
            file.getParentFile().mkdir();
        }*/

        //判断文件是否存在，不存在则创建文件
        if (file.isFile()) {
            try {
                file.createNewFile();
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }

        // 向文件写入内容(输出流)
        String str = "亲爱的小南瓜！\r\nhello world!";
        byte bt[] = new byte[1024];
        bt = str.getBytes();
        try {
            FileOutputStream in = new FileOutputStream(file);
            try {
                in.write(bt, 0, bt.length);
                in.close();
                // boolean success=true;
                // System.out.println("写入文件成功");
            } catch (IOException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        try {
            // 读取文件内容 (输入流)
            FileInputStream out = new FileInputStream(file);
            InputStreamReader isr = new InputStreamReader(out);
            int ch = 0;
            while ((ch = isr.read()) != -1) {
                System.out.print((char) ch);
            }
        } catch (Exception e) {
            // TODO: handle exception
        }
    }

    /**
     * 删除文件
     * @param file
     */
    private void deleteFile(File file) {

        //判断文件是否存在，存在则删除文件
        if (file.exists()) {
            file.delete();
        } else {
            System.out.println("文件不存在！");
        }

        //判断当前文件夹在删除文件后是否是空文件夹，是则删除
        File[] list = file.getParentFile().listFiles();
        if (null == list || list.length ==0) {
            file.getParentFile().delete();
        }
    }

    /**
     * 向文件追加内容
     * @param file
     */
    private void updateFile(File file) {
        try {
            // 打开一个写文件器，构造函数中的第二个参数true表示以追加形式写文件
            FileWriter writer = new FileWriter(file, true);
            writer.write("\r\n此处为追加内容");
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取文件内容
     */
    private void readFile(File file) {
        BufferedReader reader = null;
        try {
            System.out.println("以行为单位读取文件内容，一次读一整行：");
            reader = new BufferedReader(new FileReader(file));
            String tempString = null;
            int line = 1;
            // 一次读入一行，直到读入null为文件结束
            while ((tempString = reader.readLine()) != null) {
                // 显示行号
                System.out.println("line " + line + ": " + tempString);
                line++;
            }
            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e1) {
                }
            }
        }
    }

    /**
     * 复制单个文件
     * @param oldPathFile 准备复制的文件源
     * @param newPathFile 拷贝到新绝对路径带文件名
     * @return
     */
    public void copyFile(String oldPathFile, String newPathFile) {
        try {
            int bytesum = 0;
            int byteread = 0;
            File oldfile = new File(oldPathFile);
            if (oldfile.exists()) { //文件存在时
                InputStream inStream = new FileInputStream(oldPathFile); //读入原文件
                FileOutputStream fs = new FileOutputStream(newPathFile);
                byte[] buffer = new byte[1444];
                while((byteread = inStream.read(buffer)) != -1){
                    bytesum += byteread; //字节数 文件大小
                    System.out.println(bytesum);
                    fs.write(buffer, 0, byteread);
                }
                inStream.close();
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 复制整个文件夹内容
     * @param oldPath String 原文件路径 如：c:/fqf
     * @param newPath String 复制后路径 如：f:/fqf/ff
     * @return boolean
     */
    public void copyFolder(String oldPath, String newPath) {

        try {
            (new File(newPath)).mkdirs(); //如果文件夹不存在 则建立新文件夹
            File a=new File(oldPath);
            String[] file=a.list();
            File temp=null;
            for (int i = 0; i < file.length; i++) {
                if(oldPath.endsWith(File.separator)){
                    temp=new File(oldPath+file[i]);
                }
                else{
                    temp=new File(oldPath+File.separator+file[i]);
                }

                if(temp.isFile()){
                    FileInputStream input = new FileInputStream(temp);
                    FileOutputStream output = new FileOutputStream(newPath + "/" +
                            (temp.getName()).toString());
                    byte[] b = new byte[1024 * 5];
                    int len;
                    while ( (len = input.read(b)) != -1) {
                        output.write(b, 0, len);
                    }
                    output.flush();
                    output.close();
                    input.close();
                }
                if(temp.isDirectory()){//如果是子文件夹
                    copyFolder(oldPath+"/"+file[i],newPath+"/"+file[i]);
                }
            }
        }
        catch (Exception e) {
            System.out.println("复制整个文件夹内容操作出错");
            e.printStackTrace();

        }

    }

    /**
     * 移动目录
     * @param oldPath
     * @param newPath
     * @return
     */
    public void moveFolder(String oldPath, String newPath) {
        copyFolder(oldPath, newPath);
        delFolder(oldPath);
    }

    /**
     * 删除文件夹
     * @param folderPath 文件夹完整绝对路径
     * @return
     */
    public void delFolder(String folderPath) {
        try {
            delAllFile(folderPath); //删除完里面所有内容
            String filePath = folderPath;
            filePath = filePath.toString();
            File myFilePath = new File(filePath);
            myFilePath.delete(); //删除空文件夹
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 删除指定文件夹下所有文件
     * @param path 文件夹完整绝对路径
     * @return
     * @return
     */
    public boolean delAllFile(String path) {
        boolean bea = false;
        File file = new File(path);
        if (!file.exists()) {
            return bea;
        }
        if (!file.isDirectory()) {
            return bea;
        }
        String[] tempList = file.list();
        File temp = null;
        for (int i = 0; i < tempList.length; i++) {
            if (path.endsWith(File.separator)) {
                temp = new File(path + tempList[i]);
            }else{
                temp = new File(path + File.separator + tempList[i]);
            }
            if (temp.isFile()) {
                temp.delete();
            }
            if (temp.isDirectory()) {
                delAllFile(path+"/"+ tempList[i]);//先删除文件夹里面的文件
                delFolder(path+"/"+ tempList[i]);//再删除空文件夹
                bea = true;
            }
        }
        return bea;
    }

}
