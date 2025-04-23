package FlowerShop.View;

import FlowerShop.Model.ModelCustomers;
import FlowerShop.Model.ModelOrderDetails;
import FlowerShop.Model.ModelOrders;
import FlowerShop.Model.ModelProduct;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface AbsCustomer {
    // ============ TÌM KIẾM SẢN PHẨM ============
    List<ModelProduct> searchProducts(String name, String type, String season) throws SQLException;

    // ============ THÔNG TIN KHÁCH HÀNG ============
    ModelCustomers getCustomerInfo(int userId) throws SQLException;

    // ============ CẬP NHẬT THÔNG TIN ============
    void reNameCustomer(ModelCustomers data) throws SQLException;

    // ============ QUẢN LÝ ĐƠN HÀNG ============
    List<ModelOrderDetails> getOrderDetails(int orderId) throws SQLException;

    boolean addProductToOrder(int orderId, int productId, int quantity);

    // ============ LỊCH SỬ ĐƠN HÀNG ============
    List<ModelOrders> getAllOrders(int customerId) throws SQLException;

    // ============ KIỂM TRA TỒN KHO ============
    boolean checkStock(int productId, int quantity) throws SQLException;
}