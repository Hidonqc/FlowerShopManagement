package FlowerShop.MainFrame;
import FlowerShop.Controller.Connection.DatabaseConnection;
import FlowerShop.Controller.Service.ServiceUsers;
import FlowerShop.MainFrame.Admin.AdminView;
import FlowerShop.MainFrame.Customer.CustomerView;
import FlowerShop.MainFrame.Staff.StaffView;
import FlowerShop.Model.ModelCustomer;
import FlowerShop.Model.ModelLogin;
import FlowerShop.Model.ModelStaff;
import FlowerShop.Model.ModelUser;

import java.sql.SQLException;
import java.util.Scanner;


public class LoginView {
	DatabaseConnection dbConnection = DatabaseConnection.getInstance();
	private final ServiceUsers userService = new ServiceUsers();
    private final Scanner scanner = new Scanner(System.in);
    public LoginView() throws SQLException{};
    public void showLoginMenu() {
        while (true) {
            System.out.println("\n🌸🌸🌸 CỬA HÀNG HOA NÈ 🌸🌸🌸");
            System.out.println("1. Đăng nhập");
            System.out.println("2. Thoát");
            System.out.print("➔ Chọn: ");
            int choice = scanner.nextInt();
            scanner.nextLine(); 

            if (choice == 2) {
                System.out.println("👋 Hẹn gặp lại!");
                System.exit(0);
            }

            System.out.print("\n📧 Email: ");
            String email = scanner.nextLine();
            System.out.print("🔒 Mật khẩu: ");
            String password = scanner.nextLine();

            try {
                ModelUser user = userService.login(new ModelLogin(email, password));
                if (user != null) {
                    System.out.println("\n✅ Đăng nhập thành công!");
                    showUserMenu(user); // Điều hướng trực tiếp
                    return;
                }
                System.out.println("❌ Sai thông tin đăng nhập!");
            } catch (Exception e) {
                System.out.println("❗ Lỗi hệ thống: " + e.getMessage());
            }
        }
    }

    private void showUserMenu(ModelUser user) throws SQLException {
    	    // Debug: In ra thông tin user để kiểm tra
    	    System.out.println("Debug - User role: " + user.getRole());
    	    System.out.println("Debug - User class: " + user.getClass().getName());
    	    
    	    try {
    	        // Chuẩn hóa role (bỏ khoảng trắng thừa và chuyển về chữ thường)
    	        String normalizedRole = user.getRole().trim().toLowerCase();
    	        
    	        switch (normalizedRole) {
    	            case "khach hang":
    	                if (user instanceof ModelCustomer) {
    	                    ModelCustomer customer = (ModelCustomer) user;
    	                    System.out.println("Debug - Customer ID: " + customer.getCustomer_ID());
    	                    new CustomerView().showCustomerMenu(customer.getCustomer_ID());
    	                } else {
    	                    System.out.println("Lỗi: Đây không phải tài khoản khách hàng");
    	                }
    	                break;
    	                
    	            case "nhan vien":
    	                if (user instanceof ModelStaff) {
    	                    ModelStaff staff = (ModelStaff) user;
    	                    new StaffView().showStaffMenu(staff.getID_Staff());
    	                }
    	                break;
    	                
    	            case "quan ly":
    	                new AdminView().showAdminMenu(user.getID_User());
    	                break;
    	                
    	            default:
    	                System.out.println("Vai trò không hợp lệ: " + user.getRole());
    	        }
    	    } catch (Exception e) {
    	        System.err.println("Lỗi hệ thống khi hiển thị menu: ");
    	        e.printStackTrace();
    	    }
    	}
    }