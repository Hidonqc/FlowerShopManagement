package FlowerShop.MainFrame.Customer;

import FlowerShop.Controller.Service.ServiceCustomer;
import FlowerShop.Model.ModelCustomers;
import FlowerShop.Model.ModelOrderDetails;
import FlowerShop.Model.ModelOrders;
import FlowerShop.Model.ModelProduct;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class CustomerView {
    private final Scanner scanner = new Scanner(System.in);
    private final ServiceCustomer customerService = new ServiceCustomer();
    private int currentCustomerId;

    public void showCustomerMenu(int customerId) throws SQLException {
        this.currentCustomerId = customerId;

        while (true) {
            System.out.println("\n🌼🌼🌼 MENU KHÁCH HÀNG 🌼🌼🌼");
            System.out.println("1. Tìm kiếm hoa");
            System.out.println("2. Đơn hàng của tôi");
            System.out.println("3. Thông tin cá nhân");
            System.out.println("4. Đăng xuất");
            System.out.print("➔ Chọn: ");

            switch (scanner.nextInt()) {
                case 1 -> searchProducts();
                case 2 -> showOrderHistory();
                case 3 -> showCustomerInfo();
                case 4 -> {
                    return; // Thoát khỏi menu khách hàng
                }
                default -> System.out.println("Lựa chọn không hợp lệ, vui lòng thử lại.");
            }
        }
    }

    private void searchProducts() throws SQLException {
        System.out.println("\n🔍 TÌM KIẾM NÂNG CAO");
        System.out.println("1. Theo tên");
        System.out.println("2. Theo loại");
        System.out.println("3. Theo mùa");
        System.out.print("➡️ Chọn: ");

        String searchType = switch (scanner.nextInt()) {
            case 1 -> "name";
            case 2 -> "type";
            case 3 -> "season";
            default -> "all";
        };

        System.out.print("➡️ Nhập từ khóa: ");
        scanner.nextLine(); // Đọc bỏ dòng thừa sau nextInt()
        String keyword = scanner.nextLine();

        List<ModelProduct> results = null;
        try {
            results = customerService.searchProducts(keyword, searchType, searchType);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }



        System.out.println("\n🌸 KẾT QUẢ TÌM KIẾM 🌸");
        results.forEach(p -> System.out.printf(
                "%-5d | %-20s | %-10s | %,.2f VND | %-10s | %-10s \n",
                p.getProductID(), p.getProductName(), p.getType(), p.getPrice(),p.getSeason(),p.getStock()
        ));
    }
    private void showOrderHistory() {
        try {
            List<ModelOrders> orders = customerService.getAllOrders(currentCustomerId);

            if (orders.isEmpty()) {
                System.out.println("\nKhông có đơn hàng nào.");
            } else {
                System.out.println("\n🌸 LỊCH SỬ ĐƠN HÀNG 🌸");
                for (ModelOrders order : orders) {
                    System.out.printf("Mã đơn hàng: %d | Ngày tạo: %s | Tổng tiền: %.2f VND\n",
                            order.getorder_ID(), order.getorder_Day(), order.getTotal_Amount());
                    System.out.println("Chi tiết đơn hàng:");
                    List<ModelOrderDetails> orderDetails = customerService.getOrderDetails(order.getorder_ID());
                    orderDetails.forEach(detail -> System.out.printf(
                            "Sản phẩm: %-20s | Số lượng: %-3d | Giá: %,.2f VND\n",
                            detail.getproduct_ID(), detail.getquantity(), detail.gettotal()));
                    System.out.println("========================================");
                }
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi lấy thông tin đơn hàng: " + e.getMessage());
        }
    }

    private void showCustomerInfo() {
        try {
            ModelCustomers customer = customerService.getCustomerInfo(currentCustomerId);
            if (customer != null) {
                System.out.println("\n📝 THÔNG TIN CÁ NHÂN 📝");
                System.out.printf("ID: %s\nHọ và tên: %s\nNgày tham gia: %s\n", customer.getCustomer_ID(), customer.getCustomer_Name(), customer.getJoinDate());
            } else {
                System.out.println("Không tìm thấy thông tin khách hàng.");
            }
        } catch (SQLException e) {
            System.out.println("Lỗi khi lấy thông tin cá nhân: " + e.getMessage());
        }
    }
}
