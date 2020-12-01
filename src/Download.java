import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.*;

/**
 * @program: wangpan_client
 * @description:
 * @author: qilinxiang
 * @create: 2020-12-01 14:29
 */
public class Download {
    public static void download(String user, String dispath) throws SQLException, ClassNotFoundException, IOException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://39.100.241.116:3306/test", "wangpan", "2017060104018");

        PreparedStatement pstmt = null;
        pstmt = con.prepareStatement("select filename, file from user where name = ?");
        pstmt.setString(1, user);
        ResultSet rs = pstmt.executeQuery();

        String filename = null;

        byte[] data = null;
        if(rs.next()) {
            data = rs.getBytes("file");
            filename = rs.getString("filename");
            rs.close();
            File file = new File(dispath + "\\" + filename);
            if (!file.exists()) {
                file.createNewFile(); // 如果文件不存在，则创建
            }
            FileOutputStream fos = new FileOutputStream(file);
            InputStream in = null;
            int size = 0;
            if (data.length > 0) {
                fos.write(data, 0, data.length);
            } else {
                while ((size = in.read(data)) != -1) {
                    fos.write(data, 0, size);
                }
                in.close();
            }
            fos.close();
            con.close();
            pstmt.close();
            System.out.println("从数据库下载文件成功");
        }
        else throw new SQLException();
    }
}