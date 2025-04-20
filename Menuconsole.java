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
        
        while (true) {
            System.out.println("\n FlowerShop Management System ");
            System.out.println("1. Đăng nhập");
            System.out.println("2. Thoát chương trình");
            System.out.print("Chọn chức năng: ");
            int option = Integer.parseInt(scanner.nextLine());
            switch (option) {
                case 1:
                    loginAndShowMenu(scanner, productDAO, orderDAO, staffDAO, customerDAO, userDAO);
                    break;
                case 2:
                    System.out.print("Bạn có chắc muốn thoát không? (y/n): ");
                    String confirm = scanner.nextLine();
                    if (confirm.equalsIgnoreCase("y")) {
                        System.out.println("Đã thoát chương trình. Cảm ơn bạn đã sử dụng!");
                        System.exit(0); }
                    break;
                default:
                    System.out.println("Lựa chọn không hợp lệ. Vui lòng chọn lại."); }
        }
    }
    private static void loginAndShowMenu(Scanner scanner, ProductDAO productDAO, OrderDAO orderDAO, StaffDAO staffDAO, CustomerDAO customerDAO, UserDAO userDAO) {
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
        System.out.println("\nĐăng nhập thành công!" + currentUser.getUsername() + 
                " (" + currentUser.getRole() + ")");
        
        // Hiển thị menu theo quyền
        if (currentUser.getRole().equalsIgnoreCase("admin")) {
            menuAdmin(scanner, userDAO);
        } else if (currentUser.getRole().equalsIgnoreCase("staff")) {
            menuStaff(scanner, productDAO, orderDAO, customerDAO);
        } else if (currentUser.getRole().equalsIgnoreCase("customer")) {
            menuCustomer(scanner, productDAO, orderDAO);
        } else {
            System.out.println("Quyền người dùng không xác định.");
        }
    }
    // Menu cho Admin
    private static void menuAdmin(Scanner scanner, UserDAO userDAO) 
    {
        while (true) {
            System.out.println("\n MENU ADMIN ");
            System.out.println("1. Thêm người dùng");
            System.out.println("2. Sửa thông tin người dùng");
            System.out.println("3. Xóa người dùng");
            System.out.println("4. Xem danh sách người dùng");
            System.out.println("0. Thoát");
            System.out.print("Chọn chức năng: ");
            int choice = Integer.parseInt(scanner.nextLine());
            switch (choice) {
                case 1:
                    userDAO.addUser(scanner);
                    break;
                case 2:
                    userDAO.updateUser(scanner);
                    break;
                case 3:
                    userDAO.deleteUser(scanner);
                    break;
                case 4:
                    userDAO.displayAllUser();
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
    private static void addUser(Scanner scanner, UserDAO userDAO) {
        System.out.println("\n THÊM NGƯỜI DÙNG MỚI ");
        System.out.print("Nhập username: ");
        String username = scanner.nextLine();
        System.out.print("Nhập password: ");
        String password = scanner.nextLine();
        System.out.print("Nhập role (admin/staff/customer): ");
        String role = scanner.nextLine();
        User newUser = new User(username, password, role);
        if (userDAO.addUser(newUser)) {
            System.out.println("Thêm người dùng thành công!");
        } else {
            System.out.println("Có lỗi xảy ra! Vui lòng xem lại.");
        }
    }
    private static void updateUser(Scanner scanner, UserDAO userDAO) {
        System.out.println("\n CẬP NHẬT THÔNG TIN NGƯỜI DÙNG ");
        System.out.print("Nhập username cần sửa: ");
        String username = scanner.nextLine();
        System.out.print("Nhập password mới (để trống nếu không đổi): ");
        String password = scanner.nextLine();
        System.out.print("Nhập role mới (admin/staff/customer - để trống nếu không đổi): ");
        String role = scanner.nextLine();
        if (userDAO.updateUser(username, password, role)) {
            System.out.println("Cập nhật thông tin thành công!");
        } else {
            System.out.println("Có lỗi xảy ra! Vui lòng xem lại.");
        }
    }
    private static void deleteUser(Scanner scanner, UserDAO userDAO) {
        System.out.println("\n XÓA NGƯỜI DÙNG ");
        System.out.print("Nhập username cần xóa: ");
        String username = scanner.nextLine();
        System.out.print("Bạn có chắc chắn muốn xóa người dùng này? (y/n): ");
        String confirm = scanner.nextLine();
        if (confirm.equalsIgnoreCase("y")) {
            if (userDAO.deleteUser(username)) {
                System.out.println("Xóa người dùng thành công!");
            } else {
                System.out.println("Có lỗi xảy ra! Vui lòng xem lại.");
            }
        }
    }
    private static void displayAllUser(UserDAO userDAO) {
        System.out.println("\nDANH SÁCH NGƯỜI DÙNG\n" + 
                          "----------------------------------------------\n" +
                          String.format("%-20s %-20s %-10s", "USERNAME", "PASSWORD", "ROLE") + 
                          "\n----------------------------------------------");
        
        List<User> users = userDAO.getAllUsers();
        users.forEach(user -> System.out.printf("%-20s %-20s %-10s\n", 
                                              user.getUsername(), 
                                              user.getPassword(), 
                                              user.getRole()));
        
        if (users.isEmpty()) System.out.println("Không có người dùng nào!");
        System.out.println("----------------------------------------------");
    }

    // Menu cho Staff
    private static void menuStaff(Scanner scanner, ProductDAO productDAO, OrderDAO orderDAO, CustomerDAO customerDAO ) {
        while (true) {
        	 while (true) {
                 System.out.println("\n MENU NHÂN VIÊN ");
                 System.out.println("1. Quản lý sản phẩm");
                 System.out.println("2. Quản lý đơn hàng");
                 System.out.println("3. Quản lý khách hàng");
                 System.out.println("0. Thoát ");
                 System.out.print("Chọn chức năng: ");
                 int choice = Integer.parseInt(scanner.nextLine());
                 switch (choice) {
                     case 1:
                         manageProducts(scanner, productDAO);
                         break;
                     case 2:
                         manageOrders(scanner, orderDAO);
                         break;
                     case 3:
                         manageCustomers(scanner, customerDAO);
                         break;
                     case 0:
                         System.out.println("Đăng xuất thành công!");
                         return;
                     default:
                         System.out.println("Lựa chọn không hợp lệ.");
                 }
        	 }
        }
    }
    private static void manageProducts(Scanner scanner, ProductDAO productDAO) {
             while (true) {
                 System.out.println("\n QUẢN LÝ SẢN PHẨM ");
                 System.out.println("1. Xem danh sách sản phẩm");
                 System.out.println("2. Thêm sản phẩm");
                 System.out.println("3. Sửa thông tin sản phẩm");
                 System.out.println("4. Xóa sản phẩm");
                 System.out.println("0. Quay lại");
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
                         productDAO.updateProduct(scanner);
                         break;
                     case 4:
                         productDAO.deleteProduct(scanner);
                         break;
                     case 0:
                         return;
                     default:
                         System.out.println("Lựa chọn không hợp lệ.");
                 }
             }
         }
    private static void displayAllProducts(ProductDAO productDAO) {
	    System.out.printf("\nDANH SÁCH SẢN PHẨM\n%-40s\n%-10s %-20s %-10s %-10s\n%-40s\n",
	        "----------------------------------------------",
	        "ID", "TÊN", "GIÁ", "SL",
	        "----------------------------------------------");
	    
	    productDAO.getAllProducts().forEach(p -> System.out.printf("%-10s %-20s %-10.2f %-10d\n",
	        p.getId(), p.getName(), p.getPrice(), p.getQuantity()));
	    
	    System.out.println(productDAO.getAllProducts().isEmpty() ? "Không có SP!" : "----------------------------------------------"); }
    private static String input(Scanner scanner, String string) {
		// TODO Auto-generated method stub
		return null; }
    private static void addProduct(Scanner scanner, ProductDAO productDAO) {
        System.out.println("\n THÊM SẢN PHẨM MỚI: ");
        Product newProduct = new Product(
            input(scanner, "Nhập ID sản phẩm: "),
            input(scanner, "Nhập tên sản phẩm: "),
            Double.parseDouble(input(scanner, "Nhập giá sản phẩm: ")),
            Integer.parseInt(input(scanner, "Nhập số lượng: ")));
        System.out.println(productDAO.addProduct(newProduct) ? "Thêm thành công!" : "Có lỗi xảy ra. Vui lòng thử lại!"); }
	private static void updateProduct(Scanner scanner, ProductDAO productDAO) {
        System.out.println("\n CẬP NHẬT SẢN PHẨM: ");
        String id = input(scanner, "Nhập ID sản phẩm cần sửa: ");
        String name = input(scanner, "Nhập tên mới (để trống nếu không đổi): ");
        String priceStr = input(scanner, "Nhập giá mới (để trống nếu không đổi): ");
        String quantityStr = input(scanner, "Nhập số lượng mới (để trống nếu không đổi): ");
        Double price = priceStr.isEmpty() ? null : Double.parseDouble(priceStr);
        Integer quantity = quantityStr.isEmpty() ? null : Integer.parseInt(quantityStr);
        System.out.println(productDAO.updateProduct(id, name, price, quantity) ? 
        		"Cập nhật thành công!" : "Có lỗi xảy ra. Vui lòng thử lại!"); }
    private static void deleteProduct(Scanner scanner, ProductDAO productDAO) {
        System.out.println("\nXÓA SẢN PHẨM");
        String id = input(scanner, "Nhập ID sản phẩm cần xóa: ");
        if (input(scanner, "Bạn có chắc chắn muốn xóa? (y/n): ").equalsIgnoreCase("y")) {
            System.out.println(productDAO.deleteProduct(id) ? "Xóa thành công!" : "Có lỗi xảy ra. Vui lòng thử lại!"); } }
     
    private static void manageOrders(Scanner scanner, OrderDAO orderDAO) {
             while (true) {
                 System.out.println("\n QUẢN LÝ ĐƠN HÀNG ");
                 System.out.println("1. Tạo đơn hàng mới");
                 System.out.println("2. Xem danh sách đơn hàng");
                 System.out.println("3. Xóa đơn hàng");
                 System.out.println("0. Quay lại");
                 System.out.print("Chọn chức năng: ");
                 int choice = Integer.parseInt(scanner.nextLine());
                 
                 switch (choice) {
                     case 1:
                         orderDAO.createOrder(scanner);
                         break;
                     case 2:
                         orderDAO.displayAllOrders();
                         break;
                     case 3:
                         orderDAO.updateOrderStatus(scanner);
                         break;
                     case 4:
                         orderDAO.deleteOrder(scanner);
                         break;
                     case 0:
                         return;
                     default:
                         System.out.println("Lựa chọn không hợp lệ.");
                 }
             }
         }
    private static void createOrder(Scanner s, OrderDAO oDao) {
        System.out.println("\nTẠO ĐƠN HÀNG: ");
        Order o = new Order(input(s, "Nhập ID đơn hàng: "), input(s, "Nhập ID khách hàng: "));
        System.out.println(oDao.createOrder(o) ? "Thành công!" : "Có lỗi xảy ra. Vui lòng thử lại!"); }
    private static void deleteOrder(Scanner s, OrderDAO oDao) {
        System.out.println("\nXÓA ĐƠN HÀNG: ");
        String id = input(s, "Nhập ID đơn hàng cần xóa: ");
        if (input(s, "Xác nhận xóa? (y/n): ").equalsIgnoreCase("y")) {
            System.out.println(oDao.deleteOrder(id) ? "Thành công!" : "Có lỗi xảy ra. Vui lòng thử lại!"); } }
    private static void displayAllOrders(OrderDAO o) {
        String line = "=".repeat(40);
        System.out.printf("\nDANH SÁCH ĐƠN HÀNG\n%s\n%-15s %-15s %s\n%s\n", 
            line, "MÃ ĐƠN", "KHÁCH HÀNG", "TRẠNG THÁI", line);
        o.getAllOrders().forEach(d -> System.out.printf("%-15s %-15s %s\n", d.getId(), d.getCustomerId(), d.getStatus()));
        System.out.println(o.getAllOrders().isEmpty() ? "Không có đơn hàng!" : line); }
    
    private static void manageCustomers(Scanner scanner, CustomerDAO customerDAO) {
             while (true) {
                 System.out.println("\n QUẢN LÝ KHÁCH HÀNG ");
                 System.out.println("1. Thêm khách hàng");
                 System.out.println("2. Sửa thông tin khách hàng");
                 System.out.println("3. Xóa khách hàng");
                 System.out.println("4. Xem danh sách khách hàng");
                 System.out.println("0. Quay lại");
                 System.out.print("Chọn chức năng: ");
                 int choice = Integer.parseInt(scanner.nextLine());
                 
                 switch (choice) {
                     case 1:
                         customerDAO.addCustomer(scanner);
                         break;
                     case 2:
                         customerDAO.updateCustomer(scanner);
                         break;
                     case 3:
                         customerDAO.deleteCustomer(scanner);
                         break;
                     case 4:
                         customerDAO.dislayAllCustomer();
                         break;
                     case 0:
                         return;
                     default:
                         System.out.println("Lựa chọn không hợp lệ.");
                 }
             }
         }
    private static void addCustomer(Scanner s, CustomerDAO d) {
        Customer c = new Customer(input(s,"\n THÊM KHÁCH HÀNG:"),input(s,"Tên:"),input(s,"SĐT:"),input(s,"Email:"));
        System.out.println(d.addCustomer(c)?"Thành công!":"Có lỗi xảy ra. Vui lòng thử lại!"); }
    private static void updateCustomer(Scanner s, CustomerDAO d) {
        String id = input(s,"\n CẬP NHẬT ID KHÁCH HÀNG cần sửa:");
        System.out.println(d.updateCustomer(id,input(s,"Tên mới:"),input(s,"SĐT mới:"),input(s,"Email mới:"))?
        		"Thành công!":"Có lỗi xảy ra. Vui lòng thử lại!"); }
    private static void deleteCustomer(Scanner s, CustomerDAO d) {
        String id = input(s,"\n ID KHÁCH HÀNG cần xóa:");
        if(input(s,"Xác nhận (y/n):").equalsIgnoreCase("y")) System.out.println(d.deleteCustomer(id)?"Đã xóa!":"Có lỗi xảy ra. Vui lòng thử lại!"); }
    private static void displayAllCustomers(CustomerDAO d) {
        String b = "=".repeat(40);
        System.out.printf("\nDANH SÁCH KHÁCH HÀNG\n%s\n%-10s %-20s %-15s %s\n%s\n",b,"ID","TÊN","SĐT","EMAIL",b);
        d.getAllCustomers().forEach(c->System.out.printf("%-10s %-20s %-15s %s\n",c.getId(),c.getName(),c.getPhone(),c.getEmail()));
        System.out.println(d.getAllCustomers().isEmpty()?"Trống!":b); }
           
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
    private static void displayAllProducts(ProductDAO p) {
        String border = "=".repeat(40);
        System.out.printf("\nDANH SÁCH SẢN PHẨM\n%s\n%-10s %-20s %-6s %s\n%s\n",
            border, "ID", "TÊN", "GIÁ", "SL", border);
        p.getAllProducts().forEach(x -> 
            System.out.printf("%-10s %-20s %-6.2f %d\n", 
                x.getId(), x.getName(), x.getPrice(), x.getQuantity()));
        System.out.println(p.getAllProducts().isEmpty() ? "Không có sản phẩm nào!" : border); }
    private static void createOrder(Scanner s, OrderDAO o) {
        Order ord = new Order(input(s,"\nTẠO ID ĐƠN HÀNG:"), User.currentUser.getUsername());
        System.out.println(o.createOrder(ord) ? "Thành công!" : "Có lỗi xảy ra. Vui lòng thử lại!"); }
}
