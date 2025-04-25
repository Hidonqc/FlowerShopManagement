package FlowerShop.MainFrame.Admin;
import FlowerShop.Controller.Service.ServiceAdmin;
import FlowerShop.Model.ModelProduct;
import FlowerShop.Model.ModelStaff;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Scanner;


public class AdminView {
	 private final Scanner scanner = new Scanner(System.in);
	    private final ServiceAdmin adminService = new ServiceAdmin();
	    public AdminView() throws SQLException{};
	    public boolean showAdminMenu(int ID_Staff) throws SQLException {
	        while (true) {
	            System.out.println("\n⚡⚡⚡ MENU QUẢN LÝ ⚡⚡⚡");
	            System.out.println("1. Quản lý nhân viên");
	            System.out.println("2. Quản lý khách hàng");
	            System.out.println("3. Quản lý sản phẩm");
	            System.out.println("4. Báo cáo doanh thu");
	            System.out.println("5. Thống kê sản phẩm");
	            System.out.println("6. Đăng xuất");
	            System.out.print("➡️ Chọn: ");

	            switch (scanner.nextInt()) {
	                case 1 -> manageStaff();
	                case 2 -> manageCustomers();
	                case 3 -> manageProducts();
	                case 4 -> generateRevenueReport();
	                case 5 -> showProductStatistics();
	                case 6 -> { return false; }
	                default -> System.out.println("❌ Lựa chọn không hợp lệ!");
	            }
	        }
	    }

	    private void manageStaff() {
	        while (true) {
	            System.out.println("\n👨💼 QUẢN LÝ NHÂN VIÊN");
	            System.out.println("1. Xem danh sách");
	            System.out.println("2. Thêm nhân viên");
	            System.out.println("3. Cập nhật thông tin");
	            System.out.println("4. Quay lại");
	            System.out.print("➡️ Chọn: ");

	            switch (scanner.nextInt()) {
	                case 1 -> adminService.getAllStaff().forEach(staff ->
	                        System.out.printf("ID: %-5d | %-20s |  %-20s | %-15s | %-15s | %-5d\n",
	                                staff.getID_Staff(), staff.getNameStaff(), staff.getJoinDate(), staff.getPhoneNumber(), staff.getRole(), staff.getID_Manager()));
	                case 2 -> {
	                    ModelStaff newStaff = new ModelStaff();
	                    System.out.print("Tên nhân viên: "); newStaff.setNameStaff(scanner.next());
	                    System.out.print("Ngày vào làm: "); newStaff.setDateJoin(scanner.next());
	                    System.out.print("SĐT: "); newStaff.setPhoneNumber(scanner.next());
	                    System.out.print("Role: "); newStaff.setRole(scanner.next());
	                    System.out.print("ID_Manager: "); newStaff.setID_Manager(scanner.nextInt());

	                    if (adminService.addStaff(newStaff)) {
	                        System.out.println("✅ Thêm thành công!");
	                    }
	                }
	                case 3 -> {
	                    System.out.print("Nhập ID nhân viên: ");
	                    int staffId = scanner.nextInt();
	                    ModelStaff staff = adminService.getID_Staff(staffId);

	                    System.out.print("Nhập tên mới: ");
	                    staff.setNameStaff(scanner.next());

	                    if (adminService.updateStaff(staff)) {
	                        System.out.println("✅ Cập nhật thành công!");
	                    }
	                }
	                case 4 -> { return; }
	            }
	        }
	    }

	    private void manageCustomers() throws SQLException {
	        System.out.println("\n📃 DANH SÁCH KHÁCH HÀNG");
	        System.out.printf("%-5s | %-20s | %-15s\n", "ID", "Tên", "Ngày tham gia");
	        adminService.getAllCustomers().forEach(customer ->
	                System.out.printf("%-5d | %-20s | %-15s\n",
	                        customer.getCustomer_ID(),
	                        customer.getCustomer_Name(),
	                        customer.getJoinDate())
	        );
	    }

	    private void manageProducts() throws SQLException {
	        while (true) {
	            System.out.println("\n🛒 QUẢN LÝ SẢN PHẨM");
	            System.out.println("1. Xem danh sách");
	            System.out.println("2. Thêm sản phẩm");
	            System.out.println("3. Cập nhật thông tin");
	            System.out.println("4. Quay lại");
	            System.out.print("➡️ Chọn: ");

	            switch (scanner.nextInt()) {
	                case 1 -> adminService.getAllProducts().forEach(product ->
	                        System.out.printf("ID: %-5d | Tên: %-20s | Giá: %,.2f | Tồn kho: %-5d | Loại: %-10s | Mùa: %-10s\n",
	                                product.getProductID(),
	                                product.getProductName(),
	                                product.getPrice(),
	                                product.getStock(),
	                                product.getType(),
	                                product.getSeason()));
	                case 2 -> {
	                    ModelProduct newProduct = new ModelProduct();
	                    System.out.print("Tên sản phẩm: "); newProduct.setProductName(scanner.next());
	                    System.out.print("Giá: "); newProduct.setPrice((int) scanner.nextDouble());
	                    System.out.print("Số lượng tồn kho: "); newProduct.setStock(scanner.nextInt());
	                    System.out.print("Loại hoa: "); newProduct.setType(scanner.next());
	                    System.out.print("Mùa hoa: "); newProduct.setSeason(scanner.next());

	                    if (adminService.addProduct(newProduct)) {
	                        System.out.println("✅ Thêm sản phẩm thành công!");
	                    } else {
	                        System.out.println("❌ Thêm thất bại!");
	                    }
	                }
	                case 3 -> {
	                    System.out.print("Nhập ID sản phẩm cần cập nhật: ");
	                    int productId = scanner.nextInt();
	                    ModelProduct product = adminService.getProductById(productId);

	                    System.out.print("Tên mới: "); product.setProductName(scanner.next());
	                    System.out.print("Giá mới: "); product.setPrice((int) scanner.nextDouble());
	                    System.out.print("Số lượng mới: "); product.setStock(scanner.nextInt());
	                    System.out.print("Loại mới: "); product.setType(scanner.next());
	                    System.out.print("Mùa mới: "); product.setSeason(scanner.next());

	                    if (adminService.updateProduct(product)) {
	                        System.out.println("✅ Cập nhật thành công!");
	                    }
	                }
	                case 4 -> { return; }
	            }
	        }
	    }

	    private void generateRevenueReport() throws SQLException {
	        System.out.println("\n📊 CHỌN LOẠI BÁO CÁO");
	        System.out.println("1. Theo ngày");
	        System.out.println("2. Theo tháng");
	        System.out.println("3. Theo năm");
	        System.out.print("➡️ Chọn: ");

	        String period = switch (scanner.nextInt()) {
	            case 1 -> "ngày";
	            case 2 -> "tháng";
	            case 3 -> "năm";
	            default -> throw new IllegalArgumentException("❌ Lựa chọn không hợp lệ!");
	        };

	        System.out.print("Nhập ngày/tháng/năm (yyyy-MM-dd): ");
	        Date date = Date.valueOf(scanner.next());

	        System.out.println("\n📈 KẾT QUẢ BÁO CÁO");
	        System.out.printf("%-15s | %-15s\n", "Thời gian", "Doanh thu");
	        adminService.getRevenueReport(period, date).forEach((k, v) ->
	                System.out.printf("%-15s | %,-15.2f VND\n", k, v)
	        );
	    }

	    private void showProductStatistics() throws SQLException {
	        System.out.println("\n🏆 TOP SẢN PHẨM BÁN CHẠY");
	        System.out.printf("%-5s | %-20s | %-15s\n", "ID", "Tên sản phẩm", "Doanh thu");
	        adminService.getProductStatistics().forEach(product ->
	                System.out.printf("%-5d | %-20s | %,-15.2f VND\n",
	                        product.getProductId(),
	                        product.getProductName(),
	                        product.getTotalSales()
	                )
	        );
	    }
}
