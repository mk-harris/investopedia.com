import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {	

	public static Connection ConnectMYSQL() throws ClassNotFoundException, SQLException {
		String driver = "com.mysql.jdbc.Driver"; // ����������
		String dburl = "jdbc:mysql://db101.dev.wl.vcinv.net:3306/INV_Staging"; // URLָ��Ҫ���ʵ����ݿ���INV_Staging
		String dbuser = "dt_cms"; // MySQL����ʱ���û���
		String dbpassword = "dtCms23!"; // Java����MySQL����ʱ������
		try {
			Class.forName(driver);// ������������
			Connection conn = DriverManager.getConnection(dburl, dbuser, dbpassword); // �������ݿ�
			if (!conn.isClosed())
				System.out.println("Succeeded connecting to the Database!");
			else
				System.out.println("Unable to connect the Database!");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			System.out.println("Sorry,can`t find the Driver!");
		}catch(SQLException e) {
			e.printStackTrace();
		}catch(Exception e) {
			e.printStackTrace();
		}
		return DriverManager.getConnection(dburl, dbuser, dbpassword);
	}
}


