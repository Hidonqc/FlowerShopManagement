package FlowerShop;
import FlowerShop.MainFrame.Admin.AdminView;
import FlowerShop.MainFrame.Customer.CustomerView;
import FlowerShop.MainFrame.Staff.StaffView;
import FlowerShop.Model.ModelCustomer;
import FlowerShop.Model.ModelLogin;
import FlowerShop.Model.ModelStaff;
import FlowerShop.Controller.Connection.DatabaseConnection;
import FlowerShop.MainFrame.LoginView;
import FlowerShop.Model.ModelUser;
import java.sql.SQLException;


public class Main  { 
	
	 public static void main(String[] args) throws SQLException {
		 try {
				         DatabaseConnection.getInstance(); // Khởi tạo kết nối DB
				            LoginView loginView = new LoginView();
				            loginView.showLoginMenu(); // Xử lý điều hướng trong LoginView
				        } catch (SQLException e) {
				            System.err.println("❌ Lỗi kết nối database: " + e.getMessage());
				            e.printStackTrace();
				        }
				    }
				}
