package Extra;

import java.sql.*;
import java.util.Scanner;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.CallableStatement;


public class FlowerShopManager {
	

	    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=FlowerMangement;user=sa;password=123456";

	    private static Connection getConnection() throws SQLException {
	        return DriverManager.getConnection(DB_URL);
	    }

	    // Thêm khách hàng (Nhập từ bàn phím)
	    public static void addCustomer() {
	        Scanner scanner = new Scanner(System.in);
	        System.out.print("Nhập ID khách hàng: ");
	        int customerID = scanner.nextInt();
	        scanner.nextLine(); // Xử lý dòng trống

	        System.out.print("Nhập tên khách hàng: ");
	        String customerName = scanner.nextLine();

	        try (Connection conn = getConnection()) {
	            CallableStatement stmt = conn.prepareCall("{call AddCustomer(?, ?)}");
	            stmt.setInt(1, customerID);
	            stmt.setString(2, customerName);

	            stmt.executeUpdate();
	            System.out.println("Khách hàng đã được thêm thành công!");
	            stmt.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    // Thêm nhân viên (Nhập từ bàn phím)
	    public static void addEmployee() {
	        Scanner scanner = new Scanner(System.in);
	        System.out.print("Nhập ID nhân viên: ");
	        int employeeID = scanner.nextInt();
	        scanner.nextLine();
	        
	        System.out.print("Nhập tên nhân viên: ");
	        String employeeName = scanner.nextLine();

	        System.out.print("Nhập ngày vào làm (YYYY-MM-DD): ");
	        String hireDate = scanner.nextLine();

	        System.out.print("Nhập số điện thoại: ");
	        String phoneNumber = scanner.nextLine();

	        System.out.print("Nhập chức vụ: ");
	        String position = scanner.nextLine();

	        System.out.print("Nhập ID của người quản lý: ");
	        int managerID = scanner.nextInt();

	        try (Connection conn = getConnection()) {
	            CallableStatement stmt = conn.prepareCall("{call AddEmployee(?, ?, ?, ?, ?, ?)}");
	            stmt.setInt(1, employeeID);
	            stmt.setString(2, employeeName);
	            stmt.setDate(3, java.sql.Date.valueOf(hireDate));
	            stmt.setString(4, phoneNumber);
	            stmt.setString(5, position);
	            stmt.setInt(6, managerID);

	            stmt.executeUpdate();
	            System.out.println("Nhân viên đã được thêm thành công!");
	            stmt.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    // Lấy thông tin khách hàng từ SQL
	    public static void getCustomerInfo() {
	        Scanner scanner = new Scanner(System.in);
	        System.out.print("Nhập ID khách hàng cần xem: ");
	        int customerID = scanner.nextInt();

	        try (Connection conn = getConnection()) {
	            CallableStatement stmt = conn.prepareCall("{call GetCustomerInfo(?)}");
	            stmt.setInt(1, customerID);

	            ResultSet rs = stmt.executeQuery();
	            if (rs.next()) {
	                System.out.println("Thông tin khách hàng:");
	                System.out.println("-----------------------------");
	                System.out.println("ID: " + rs.getInt("Customer_ID"));
	                System.out.println("Tên khách hàng: " + rs.getString("Customer_Name"));
	            } else {
	                System.out.println("Không tìm thấy khách hàng với ID: " + customerID);
	            }

	            rs.close();
	            stmt.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    public static void main(String[] args) {
	        Scanner scanner = new Scanner(System.in);
	        while (true) {
	            System.out.println("\n=== MENU ===");
	            System.out.println("1. Thêm khách hàng");
	            System.out.println("2. Thêm nhân viên");
	            System.out.println("3. Xem thông tin khách hàng");
	            System.out.println("4. Thoát");

	            System.out.print("Chọn chức năng: ");
	            int choice = scanner.nextInt();

	            switch (choice) {
	                case 1:
	                    addCustomer();
	                    break;
	                case 2:
	                    addEmployee();
	                    break;
	                case 3:
	                    getCustomerInfo();
	                    break;
	                case 4:
	                    System.out.println("Kết thúc chương trình!");
	                    scanner.close();
	                    return;
	                default:
	                    System.out.println("Không hợp lệ, vui lòng thử lại.");
	            }
	        }
	    }
	
}
