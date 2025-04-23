package FlowerShop.MainFrame.Staff;

import FlowerShop.Controller.Service.ServiceStaff;
import FlowerShop.Model.*;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class StaffView {
    private final Scanner scanner = new Scanner(System.in);
    private final ServiceStaff staffService = new ServiceStaff();
    private int currentOrderId = -1;

    public void showStaffMenu(int staffId) {
        while (true) {
            System.out.println("\n🌺🌺🌺 MENU NHÂN VIÊN 🌺🌺🌺");
            System.out.println("1. Thêm sản phẩm vào kho");
            System.out.println("2. Tạo đơn hàng mới");
            System.out.println("3. Xem chi tiết đơn hàng");
            System.out.println("4. Tìm khách hàng");
            System.out.println("5. Thông tin cá nhân");
            System.out.println("6. Đăng xuất");
            System.out.print("➡️ Chọn: ");

            try {
                int choice = scanner.nextInt();
                scanner.nextLine(); // Clear buffer

                switch (choice) {
                    case 1 -> addProductToStock();
                    case 2 -> createNewOrder(staffId);
                    case 3 -> showOrderDetails();
                    case 4 -> findCustomer();
                    case 5 -> showStaffInfo(staffId);
                    case 6 -> { return; }
                    default -> System.out.println("❌ Lựa chọn không hợp lệ!");
                }
            } catch (Exception e) {
                scanner.nextLine(); // Clear invalid input
            }
        }
    }

    // ==================== QUẢN LÝ SẢN PHẨM ====================
    private void addProductToStock() {
        try {
            ModelProduct product = new ModelProduct();
            System.out.println("\n📦 NHẬP HÀNG MỚI");

            System.out.print("Tên hoa: ");
            product.setProductName(scanner.nextLine());

            System.out.print("Loại: ");
            product.setType(scanner.nextLine());

            System.out.print("Mùa: ");
            product.setSeason(scanner.nextLine());

            System.out.print("Giá: ");
            product.setPrice(scanner.nextInt());

            System.out.print("Số lượng: ");
            product.setStock(scanner.nextInt());
            scanner.nextLine(); // Clear buffer

            if (staffService.addProductToInventory(product)) {
                System.out.println("✅ Nhập kho thành công!");
            }
        } catch (SQLException e) {
            System.out.println("❗ Lỗi thêm sản phẩm: " + e.getMessage());
        }
    }

    // ==================== QUẢN LÝ ĐƠN HÀNG ====================
    private void createNewOrder(int staffId) {
        try {
            System.out.println("\n🛒 TẠO ĐƠN HÀNG MỚI");
            System.out.print("Nhập ID khách hàng: ");
            int customerId = scanner.nextInt();
            scanner.nextLine(); // Clear buffer

            currentOrderId = staffService.createOrder(customerId, staffId);
            System.out.println("📄 Mã đơn hàng: " + currentOrderId);

            while (true) {
                System.out.print("\nNhập ID sản phẩm (0 để kết thúc): ");
                int productId = scanner.nextInt();
                if (productId == 0) break;

                System.out.print("Số lượng: ");
                int quantity = scanner.nextInt();
                scanner.nextLine(); // Clear buffer

                try {
                    staffService.addOrderDetail(currentOrderId, productId, quantity);
                    System.out.println("➕ Đã thêm vào đơn hàng!");
                } catch (ServiceStaff.InsufficientStockException e) {
                    System.out.println("⚠️ Cảnh báo: " + e.getMessage());
                }
            }
        } catch (SQLException e) {
            System.out.println("❗ Lỗi tạo đơn hàng: " + e.getMessage());
        }
    }

    private void showOrderDetails() {
        try {
            System.out.print("\nNhập mã đơn hàng: ");
            int orderId = scanner.nextInt();
            scanner.nextLine(); // Clear buffer

            List<ModelOrderDetails> details = staffService.getOrderDetails(orderId);

            System.out.println("\n📋 CHI TIẾT ĐƠN HÀNG #" + orderId);
            System.out.printf("%-5s | %-20s | %-10s | %-15s\n",
                    "STT", "Tên sản phẩm", "Số lượng", "Thành tiền");

            for (int i = 0; i < details.size(); i++) {
                ModelOrderDetails detail = details.get(i);
                System.out.printf("%-5d | %-20s | %-10d | %,-15.2f VND\n",
                        i + 1,
                        detail.getproduct_ID(),
                        detail.getquantity(),
                        detail.gettotal() * detail.getquantity()
                );
            }
        } catch (SQLException e) {
            System.out.println("❗ Lỗi hiển thị đơn hàng: " + e.getMessage());
        }
    }

    // ==================== QUẢN LÝ KHÁCH HÀNG ====================
    private void findCustomer() {
        try {
            System.out.print("\nNhập ID khách hàng: ");
            int customerId = scanner.nextInt();
            scanner.nextLine(); // Clear buffer

            ModelCustomers customer = staffService.getCustomerName(customerId);

            System.out.println("\n👤 THÔNG TIN KHÁCH HÀNG");
            System.out.println("ID: " + customer.getCustomer_ID());
            System.out.println("Tên: " + customer.getCustomer_Name());
            System.out.println("Ngày tham gia: " + customer.getJoinDate());
        } catch (SQLException e) {
            System.out.println("❗ Lỗi tìm khách hàng: " + e.getMessage());
        }
    }

    // ==================== THÔNG TIN CÁ NHÂN ====================
    private void showStaffInfo(int staffId) {
        try {
            ModelStaff staff = staffService.getStaffInfo(staffId);

            System.out.println("\n👔 THÔNG TIN CÁ NHÂN");
            System.out.println("ID: " + staff.getID_Staff());
            System.out.println("Họ tên: " + staff.getNameStaff());
            System.out.println("Ngày vào làm: " + staff.getJoinDate());
            System.out.println("SĐT: " + staff.getPhoneNumber());
            System.out.println("Vai trò: " + staff.getRole());
            System.out.println("ID_Manager: " + staff.getID_Manager());


        } catch (SQLException e) {
            System.out.println("❗ Lỗi hiển thị thông tin: " + e.getMessage());
        }
    }
}