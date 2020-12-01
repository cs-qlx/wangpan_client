
import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.archivers.tar.TarArchiveOutputStream;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.sql.SQLException;

/**
 * @program: GUI
 * @description: 登录后的功能窗口
 * @author: qilinxiang
 * @create: 2020-11-29 19:38
 */
public class service {
    private JFrame frame = new JFrame("欢迎您");// 框架布局
    private JTabbedPane tabPane = new JTabbedPane();// 选项卡布局
    private Container con = new Container();//
    private JLabel label1 = new JLabel("文件目录");
    private JTextField text1 = new JTextField();// TextField 目录的路径
    private JButton button1 = new JButton("...");// 选择
    private JButton button2 = new JButton("打包");//打包功能
    private JButton button3 = new JButton("压缩");//压缩功能
    private JButton button4 = new JButton("上传");//上传功能
    private JButton button5 = new JButton("下载");//下载功能
    private JButton button6 = new JButton("解包");//解包功能
    private JButton button7 = new JButton("解压缩");//解压缩功能
    private JFileChooser jfc = new JFileChooser();// 文件选择器

    private File f;
    private String user;

    service(String name){
        this.user = name;
        jfc.setCurrentDirectory(new File("c://"));// 文件选择器的初始目录定为c盘
        frame.setBounds(0,0,500,200);
        frame.setLocationRelativeTo(null);
        frame.setContentPane(tabPane);// 设置布局
        label1.setBounds(100, 50, 70, 20);
        text1.setBounds(180, 50, 120, 20);
        button1.setBounds(310, 50, 50, 20); //选择文件目录
        button2.setBounds(20,100,60,20); //文件目录打包
        button3.setBounds(90,100,60,20);//文件压缩
        button4.setBounds(160,100,60,20);//文件上传
        button5.setBounds(230,100,60,20);//文件下载
        button6.setBounds(300,100,60,20);//文件解包
        button7.setBounds(370,100,80,20);//文件解压缩

        ButtonHandler listener = new ButtonHandler();   //创建监听器
        button1.addActionListener(listener);
        button2.addActionListener(listener);
        button3.addActionListener(listener);
        button4.addActionListener(listener);
        button5.addActionListener(listener);
        button6.addActionListener(listener);
        button7.addActionListener(listener);
        con.add(label1);
        con.add(text1);
        con.add(button1);
        con.add(button2);
        con.add(button3);
        con.add(button4);
        con.add(button5);
        con.add(button6);
        con.add(button7);
        frame.setVisible(true);// 窗口可见
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);// 使能关闭窗口，结束程序
        tabPane.add("1面板", con);// 添加布局1
    }

    private class ButtonHandler implements ActionListener{
        public void actionPerformed(ActionEvent e) {
            // TODO Auto-generated method stub
            if (e.getSource().equals(button1)) {// 判断触发方法的按钮是哪个
                jfc.setFileSelectionMode(2);// 选择文件目录
                jfc.showOpenDialog(null);// 此句是打开文件选择器界面的触发语句
                f = jfc.getSelectedFile();// f为选择到的目录
                text1.setText(f.getAbsolutePath());
            }
            if(e.getSource().equals(button2)){
                if(f == null) JOptionPane.showMessageDialog(null, "请先选择文件", "警告", JOptionPane.WARNING_MESSAGE);
                else {
                    try {
                        tar(f);
                        JOptionPane.showMessageDialog(null, "文件打包成功！", "恭喜", JOptionPane.DEFAULT_OPTION);
                    }
                    catch (IOException q){
                        JOptionPane.showMessageDialog(null, "文件打包失败", "警告", JOptionPane.WARNING_MESSAGE);
                    }
                    catch (RuntimeException y){
                        JOptionPane.showMessageDialog(null, "文件打包失败,目录名过长，请修改", "警告", JOptionPane.WARNING_MESSAGE);
                    }
                }
                text1.setText("");
            }
            if(e.getSource().equals(button3)){
                if(f.isDirectory()) JOptionPane.showMessageDialog(null, "文件无法压缩", "警告", JOptionPane.WARNING_MESSAGE);
                else{
                    Huffman.hfmFile(f.getAbsolutePath(), f.getAbsolutePath() + ".hfm");
                    JOptionPane.showMessageDialog(null, "文件压缩成功!", "恭喜", JOptionPane.DEFAULT_OPTION);
                }
                text1.setText("");
            }
            if(e.getSource().equals(button4)){
                if(f.isDirectory()) JOptionPane.showMessageDialog(null, "文件夹不能上传", "警告", JOptionPane.WARNING_MESSAGE);
                else {
                    try {
                        UpLoad.upload(f, user);
                        JOptionPane.showMessageDialog(null, "文件上传成功", "恭喜", JOptionPane.DEFAULT_OPTION);
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    } catch (ClassNotFoundException classNotFoundException) {
                        classNotFoundException.printStackTrace();
                    }
                }
                text1.setText("");
            }
            if(e.getSource().equals(button5)){
                if(f.isFile()) JOptionPane.showMessageDialog(null, "请选择备份文件存放的文件夹", "警告", JOptionPane.WARNING_MESSAGE);
                else{
                    try {
                        Download.download(user, f.getAbsolutePath());
                        JOptionPane.showMessageDialog(null, "文件下载成功", "恭喜", JOptionPane.DEFAULT_OPTION);
                    } catch (ClassNotFoundException classNotFoundException) {
                        classNotFoundException.printStackTrace();
                    } catch (SQLException throwables) {
                        JOptionPane.showMessageDialog(null, "服务器中无备份文件，请上传", "警告", JOptionPane.WARNING_MESSAGE);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                }
                text1.setText("");
            }
            if(e.getSource().equals(button6)){
                try{
                    unCompressTar(f);
                    JOptionPane.showMessageDialog(null, "文件解包成功", "警告", JOptionPane.WARNING_MESSAGE);
                }
                catch (IOException w){
                    JOptionPane.showMessageDialog(null, "文件解包失败", "警告", JOptionPane.WARNING_MESSAGE);
                }
                text1.setText("");
            }
            if(e.getSource().equals(button7)){
                Huffman.unhfmFile(f.getAbsolutePath(), f.getAbsolutePath().substring(0, f.getAbsolutePath().length() - 4));
                JOptionPane.showMessageDialog(null, "文件解压缩成功!", "恭喜", JOptionPane.DEFAULT_OPTION);
                text1.setText("");
            }
        }
    }

    void tar(File f) throws IOException, RuntimeException {
        File target = new File(f.getAbsolutePath());
        TarArchiveOutputStream tos = new TarArchiveOutputStream(new FileOutputStream(target.getAbsolutePath() + ".tar"));
        String base = target.getName();
        if(target.isDirectory()){
            archiveDir(target, tos, base);
        }else{
            archiveHandle(tos, target, base);
        }
        tos.close();
    }
    private static void archiveDir(File file, TarArchiveOutputStream tos, String basePath) throws IOException {
        File[] listFiles = file.listFiles();
        for(File fi : listFiles){
            if(fi.isDirectory()){
                archiveDir(fi, tos, basePath + File.separator + fi.getName());
            }else{
                archiveHandle(tos, fi, basePath);
            }
        }
    }
    private static void archiveHandle(TarArchiveOutputStream tos, File fi, String basePath) throws IOException {
        TarArchiveEntry tEntry = new TarArchiveEntry(basePath + File.separator + fi.getName());
        tEntry.setSize(fi.length());

        tos.putArchiveEntry(tEntry);

        BufferedInputStream bis = new BufferedInputStream(new FileInputStream(fi));

        byte[] buffer = new byte[1024];
        int read = -1;
        while((read = bis.read(buffer)) != -1){
            tos.write(buffer, 0 , read);
        }
        bis.close();
        tos.closeArchiveEntry();//这里必须写，否则会失败
    }

    void unCompressTar(File f) throws IOException {

        File file = new File(f.getAbsolutePath());
        String parentPath = file.getParent();
        TarArchiveInputStream tais = new TarArchiveInputStream(new FileInputStream(file));
        TarArchiveEntry tarArchiveEntry = null;
        while((tarArchiveEntry = tais.getNextTarEntry()) != null){
            String name = tarArchiveEntry.getName();
            File tarFile = new File(parentPath, name);
            if(!tarFile.getParentFile().exists()){
                tarFile.getParentFile().mkdirs();
            }
            BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(tarFile));
            int read = -1;
            byte[] buffer = new byte[1024];
            while((read = tais.read(buffer)) != -1){
                bos.write(buffer, 0, read);
            }
            bos.close();
        }
        tais.close();
        file.delete();//删除tar文件
    }
}

