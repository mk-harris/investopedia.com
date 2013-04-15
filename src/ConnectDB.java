import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectDB {	

	public static Connection ConnectMYSQL() throws ClassNotFoundException, SQLException {
		String driver = "com.mysql.jdbc.Driver"; // 驱动程序名
		String dburl = "jdbc:mysql://db101.dev.wl.vcinv.net:3306/INV_Staging"; // URL指向要访问的数据库名INV_Staging
		String dbuser = "dt_cms"; // MySQL配置时的用户名
		String dbpassword = "dtCms23!"; // Java连接MySQL配置时的密码
		try {
			Class.forName(driver);// 加载驱动程序
			Connection conn = DriverManager.getConnection(dburl, dbuser, dbpassword); // 连接数据库
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


