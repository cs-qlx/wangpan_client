import java.io.*;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * @program: wangpan_client
 * @description:
 * @author: qilinxiang
 * @create: 2020-12-01 13:06
 */
public class UpLoad {
    public static void upload(File f, String name) throws SQLException, IOException, ClassNotFoundException {
        FileInputStream fis = new FileInputStream(f.getAbsolutePath());
        String fileName = f.getName();
        byte[] filebyte = inputSwitchByte(fis);
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection con = DriverManager.getConnection("jdbc:mysql://39.100.241.116:3306/test", "wangpan", "2017060104018");
        PreparedStatement pstmt = null;

        pstmt = con.prepareStatement("UPDATE user SET filename = ?, file = ? WHERE name = ?");
        pstmt.setString(1, fileName);
        pstmt.setBytes(2, filebyte);
        pstmt.setString(3, name);
        pstmt.executeUpdate();

        fis.close();
        con.close();
        pstmt.close();
}
    /*
    文件转为字节
     */
    public static byte[] inputSwitchByte(InputStream input){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        int len = 0;
        byte[] result = null;
        byte[] bt = new byte[2048];
        try {
            while( (len=input.read(bt)) != -1){
                baos.write(bt, 0, len);
            }
            result = baos.toByteArray();
            baos.close();
            input.close();
        } catch (IOException e) {
            System.out.print("IO操作失败");
        }finally{
            if (null != baos) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (null != input) {
                try {
                    input.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

}
