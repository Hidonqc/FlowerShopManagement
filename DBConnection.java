package MainConsole;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
	private static final String DB_SERVER_NAME = "localhost"; 
	private static final String DB_PORT = "1433"; 
	private static final String DB_NAME = "HumanResourceDB";
	private static final String DB_USER = "sa"; 
	private static final String DB_PASSWORD = "sa";

	private static final String CONNECTION_URL = "jdbc:sqlserver://" + DB_SERVER_NAME + ":" + DB_PORT + ";databaseName="
			+ DB_NAME + ";trustServerCertificate=true;";

	public static Connection getConnection() throws SQLException {
		Connection conn = null;
		try {
			conn = DriverManager.getConnection(CONNECTION_URL, DB_USER, DB_PASSWORD);

			System.out.println("Kết nối đến " + DB_NAME + " thành công!"); 

		} catch (SQLException e) {
			System.err.println("Lỗi kết nối cơ sở dữ liệu: " + e.getMessage());
			e.printStackTrace();
			throw e;
		}
		return conn;
	}
	 public static void closeConnection(Connection conn) {
	        if (conn != null) {
	            try {
	                conn.close();
	                System.out.println("Đã đóng kết nối" + DB_NAME + " thành công!");
	            } catch (SQLException e) {
	                System.err.println("Lỗi đóng kết nối cơ sở dữ liệu: " + e.getMessage());
	                e.printStackTrace(); 
	            }
	        }
	 }
}


