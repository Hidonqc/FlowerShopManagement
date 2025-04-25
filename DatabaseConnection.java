package FlowerShop.Controller.Connection;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.DriverManager;



	// Kết nối tới DataBase của hệ thống

	public class DatabaseConnection {

	    private static DatabaseConnection instance;
	    private Connection connection;
	    
	    public static DatabaseConnection getInstance() throws SQLException {
	        if (instance == null) {
	            instance = new DatabaseConnection();
	        }
	        return instance;
	    }

	   
	    private DatabaseConnection() throws SQLException {
	        connectToDatabase();
	    }
	    //Thực hiện kết nối tới Database
	    public void connectToDatabase() throws SQLException {
	    	//Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
	        final String url = "jdbc:sqlserver://localhost:1433;databaseName=FlowerShop_Management;encrypt=true;trustServerCertificate=true";
	        final String username = "sa";
	        final String password = "123456";
	        connection = DriverManager.getConnection(url, username, password);
	    }

	    public Connection getConnection() {
	        return connection;
	    }

	    public void setConnection(Connection connection) {
	        this.connection = connection;
	    }
	
}
