package MainConsole;

import java.sql.Connection;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        
        DBConnection dbConnect = new DBConnection();
        Connection conn = DBConnection.getConnection();
        
        // Khởi tạo các DAO
        ProductDAO productDAO = new ProductDAO(conn);
        OrderDAO orderDAO = new OrderDAO(conn);
        StaffDAO staffDAO = new StaffDAO(conn);
        CustomerDAO customerDAO = new CustomerDAO(conn);
        UserDAO userDAO = new UserDAO(conn);
        
        // Đăng nhập
        System.out.println(" ĐĂNG NHẬP ");
        System.out.print("Username: ");
        String username = scanner.nextLine();
        System.out.print("Password: ");
        String password = scanner.nextLine();
        
        // Kiểm tra đăng nhập
        User currentUser = userDAO.checkLogin(username, password);
        
        if (currentUser == null) {
            System.out.println("Sai tài khoản hoặc mật khẩu!");
            return; 
        }
        System.out.println("Đăng nhập thành công!" + currentUser.getRole());
        
        // Hiển thị menu theo quyền
        if (currentUser.getRole().equalsIgnoreCase("admin")) {
            menuAdmin(scanner, staffDAO);
        } else if (currentUser.getRole().equalsIgnoreCase("staff")) {
            menuStaff(scanner, productDAO, orderDAO, customerDAO);
        } else if (currentUser.getRole().equalsIgnoreCase("customer")) {
            menuCustomer(scanner, productDAO, orderDAO);
        } else {
            System.out.println("Quyền người dùng không xác định.");
        }
    }
    
    // Menu cho Admin
    private static void menuAdmin(Scanner scanner, StaffDAO staffDAO) 
    {
        while (true) {
            System.out.println("\n MENU ADMIN ");
            System.out.println("1. Thêm nhân viên");
            System.out.println("2. Sửa nhân viên");
            System.out.println("3. Xem danh sách nhân viên");
            System.out.println("0. Thoát");
            System.out.print("Chọn chức năng: ");
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    staffDAO.addStaff(scanner);
                    break;
                case 2:
                    staffDAO.updateStaff(scanner);
                    break;
                case 3:
                    staffDAO.displayAllStaff();
                    break;
                case 0:
                    System.out.print("Bạn có chắc muốn thoát không? (y/n): ");
                    String confirm = scanner.nextLine();
                    if (confirm.equalsIgnoreCase("y")) {
                        System.out.println("Đã thoát chương trình.");
                        System.exit(0);
                    }
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ.");
            }
        }
    }

    // Menu cho Staff
    private static void menuStaff(Scanner scanner, ProductDAO productDAO, OrderDAO orderDAO, CustomerDAO customerDAO ) {
        while (true) {
            System.out.println("\n MENU STAFF ");
            System.out.println("1. Xem danh sách sản phẩm");
            System.out.println("2. Thêm sản phẩm");
            System.out.println("3. Sửa sản phẩm");
            System.out.println("4. Tạo đơn hàng");
            System.out.println("5. Xem danh sách đơn hàng");
            System.out.println("6. Thêm khách hàng");
            System.out.println("7. Sửa khách hàng");
            System.out.println("8. Xem danh sách khách hàng");
            System.out.println("0. Thoát");
            System.out.print("Chọn chức năng: ");
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    productDAO.displayAllProducts();
                    break;
                case 2:
                    productDAO.addProduct(scanner);
                    break;
                case 3:
                    productDAO.editProduct(scanner);
                    break;
                case 4:
                    orderDAO.createOrder(scanner);
                    break;
                case 5:
                    orderDAO.displayAllOrders();
                    break;
                case 6:
                    customerDAO.addCustomer(scanner);
                    break;
                case 7:
                	customerDAO.editCustomer(scanner);
                    break;
                case 8:
                	customerDAO.dislayAllCustomer();
                    break;
                case 0:
                	 System.out.print("Bạn có chắc muốn thoát không? (y/n): ");
                     String confirm = scanner.nextLine();
                     if (confirm.equalsIgnoreCase("y")) {
                         System.out.println("Đã thoát chương trình.");
                         System.exit(0);
                     }
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ.");
            }
        }
    }

    // Menu cho Customer
    private static void menuCustomer(Scanner scanner, ProductDAO productDAO, OrderDAO orderDAO) {
        while (true) {
            System.out.println("\n MENU CUSTOMER ");
            System.out.println("1. Xem danh sách sản phẩm");
            System.out.println("2. Tạo đơn hàng");
            System.out.println("0. Thoát");
            System.out.print("Chọn: ");
            int choice = Integer.parseInt(scanner.nextLine());

            switch (choice) {
                case 1:
                    productDAO.displayAllProducts();
                    break;
                case 2:
                    orderDAO.createOrder(scanner);
                    break;
                case 0:
                	System.out.print("Bạn có chắc muốn thoát không? (y/n): ");
                    String confirm = scanner.nextLine();
                    if (confirm.equalsIgnoreCase("y")) {
                        System.out.println("Đã thoát chương trình.");
                        System.exit(0);
                    }
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ.");
            }
        }
    }
}
