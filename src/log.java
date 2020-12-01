import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.*;

/**
 * @program: GUI
 * @description: 图形界面
 * @author: qilinxiang
 * @create: 2020-11-29 15:33
 */
public class log {
    private JFrame jf = new JFrame("保国网盘");          // 创建窗口
    private JLabel j_user = new JLabel("用户");
    private JTextField username = new JTextField();
    private JLabel j_password = new JLabel("密码");
    private JPasswordField password = new JPasswordField();
    private JButton j_ok = new JButton("登录");
    private JButton j_sign = new JButton("注册");
    private Container c = jf.getContentPane();

    private static final String URL = "jdbc:mysql://39.100.241.116:3306/test";
    private static final String USER = "wangpan";
    private static final String PASSWORD = "2017060104018";

    public log(){
        // 1. 创建一个顶层容器（窗口）
        jf.setBounds(0, 0, 300, 220);                       // 设置窗口大小
        jf.setLocationRelativeTo(null);             // 把窗口位置设置到屏幕中心
        jf.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE); // 当点击窗口的关闭按钮时退出程序（没有这一句，程序不会退出）


        //窗口上部
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout());
        titlePanel.add(new JLabel("保国网盘登录系统"));
        c.add(titlePanel, "North");

        //窗口中部
        JPanel fieldPanel = new JPanel();
        fieldPanel.setLayout(null);
        j_user.setBounds(50, 20, 50, 20);
        j_password.setBounds(50, 60, 50, 20);
        fieldPanel.add(j_user);
        fieldPanel.add(j_password);
        username.setBounds(110, 20, 120, 20);
        password.setBounds(110, 60, 120, 20);
        fieldPanel.add(username);
        fieldPanel.add(password);
        c.add(fieldPanel, "Center");

        //按钮部分
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(j_ok);
        buttonPanel.add(j_sign);
        c.add(buttonPanel, "South");

        // 5. 显示窗口，前面创建的信息都在内存中，通过 jf.setVisible(true) 把内存中的窗口显示在屏幕上。
        jf.setVisible(true);
        this.listener();
    }

    public void listener() {
        //确认按下去获取
        j_ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String uname = username.getText();
                String pwd = String.valueOf(password.getPassword());
                int x = judge(uname, pwd);
                if(x == 0) JOptionPane.showMessageDialog(null, "密码错误请重新输入", "提醒", JOptionPane.WARNING_MESSAGE);
                if(x == 1){
                    JOptionPane.showMessageDialog(null, "登录成功！", "欢迎", JOptionPane.DEFAULT_OPTION);
                    jf.dispose();

                    new service(uname); //转入登录界面
                }
                if(x == -1) JOptionPane.showMessageDialog(null, "用户未注册", "提醒", JOptionPane.WARNING_MESSAGE);
                if(x < -1) JOptionPane.showMessageDialog(null, "系统错误", "警告", JOptionPane.ERROR_MESSAGE);
            }
        });
        //新用户注册
        j_sign.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                sign_up();
            }
        });
    }

    public int judge(String name, String pwd){
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
            String sql = "select pwd from user where name = ?";
            PreparedStatement ptmt = conn.prepareStatement(sql);
            ptmt.setString(1, name);
            ResultSet rs = ptmt.executeQuery();
            if(!rs.next()) return -1;    //用户未注册
            if(pwd.equals(rs.getString("pwd"))) return 1;   //用户名密码对应成功
            else return 0;   //密码输入错误
        }
        catch(SQLException e){
            System.out.println("连接数据库失败");
            return -2;
        }
        catch (ClassNotFoundException e){
            System.out.println("类加载失败");
            return  -3;
        }
    }

    public int sign_up(){
        JFrame _jf = new JFrame("保国网盘");          // 创建窗口
        JLabel _j_user = new JLabel("请输入您的用户名");
        JTextField _username = new JTextField();
        JLabel _j_password = new JLabel("请输入您的密码");
        JPasswordField _password = new JPasswordField();
        JLabel _j_password2 = new JLabel("请确认您的密码");
        JPasswordField _password2 = new JPasswordField();
        JButton _j_sign = new JButton("注册");
        Container _c = _jf.getContentPane();

        // 1. 创建一个顶层容器（窗口）
        _jf.setBounds(0, 0, 600, 400);                       // 设置窗口大小
        _jf.setLocationRelativeTo(null);             // 把窗口位置设置到屏幕中心

        JPanel _titlePanel = new JPanel();
        _titlePanel.setLayout(new FlowLayout());
        _titlePanel.add(new JLabel("保国网盘用户注册"));
        _c.add(_titlePanel, "North");

        //窗口中部
        JPanel _fieldPanel = new JPanel();
        _fieldPanel.setLayout(null);
        _j_user.setBounds(200, 0, 100, 20);
        _j_password.setBounds(200, 100, 100, 20);
        _j_password2.setBounds(200, 200, 100, 20);
        _fieldPanel.add(_j_user);
        _fieldPanel.add(_j_password);
        _fieldPanel.add(_j_password2);
        _username.setBounds(300, 0, 100, 20);
        _password.setBounds(300, 100, 100, 20);
        _password2.setBounds(300,200, 100, 20 );
        _fieldPanel.add(_username);
        _fieldPanel.add(_password);
        _fieldPanel.add(_password2);
        _c.add(_fieldPanel, "Center");

        //按钮部分
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new FlowLayout());
        buttonPanel.add(_j_sign);
        _c.add(buttonPanel, "South");

        // 5. 显示窗口，前面创建的信息都在内存中，通过 _jf.setVisible(true) 把内存中的窗口显示在屏幕上。
        _jf.setVisible(true);

        _j_sign.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String uname = _username.getText();
                String pwd1 = String.valueOf(_password.getPassword());
                String pwd2 = String.valueOf(_password2.getPassword());
                if(!pwd1.equals(pwd2)) {
                    JOptionPane.showMessageDialog(null, "两次密码输入不一样，请重新输入", "提醒", JOptionPane.WARNING_MESSAGE);
                    _password.setText("");
                    _password2.setText("");
                }
                else{
                    try {
                        Class.forName("com.mysql.cj.jdbc.Driver");
                        Connection conn = DriverManager.getConnection(URL, USER, PASSWORD);
                        String sql = "INSERT INTO user(name, pwd) VALUES(?, ?);";
                        PreparedStatement ptmt = conn.prepareStatement(sql);
                        ptmt.setString(1, uname);
                        ptmt.setString(2, pwd1);
                        ptmt.executeUpdate();
                        JOptionPane.showMessageDialog(null, "注册成功！", "提醒", JOptionPane.WARNING_MESSAGE);
                        _jf.dispose();
                    } catch (ClassNotFoundException classNotFoundException) {
                        classNotFoundException.printStackTrace();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                        JOptionPane.showMessageDialog(null, "注册失败，用户名已存在！", "提醒", JOptionPane.WARNING_MESSAGE);
                    }
                }
            }
        });
        return 1;
    }

}

