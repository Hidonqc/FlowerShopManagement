package FlowerShop.View;
import FlowerShop.Model.*;

import java.sql.SQLException;
import java.util.List;



public interface AbsStaff {
	 // ==================== NHÂN VIÊN ====================
    ModelStaff getStaffInfo(int userID) throws SQLException;

    boolean updateStaffName(int userId, String newName) throws SQLException;

    // ==================== KHÁCH HÀNG ====================
    List<ModelCustomer> getAllCustomers() throws SQLException;

    ModelCustomer getCustomerName(int customerId) throws SQLException;

    // ==================== ĐƠN HÀNG ====================
    int createOrder(int customerId, int staffId) throws SQLException;

    boolean addOrderDetail(int orderId, int productId, int quantity)
            throws SQLException;

    // ==================== SẢN PHẨM ====================
    boolean addProductToInventory(ModelProduct product) throws SQLException;

    List<ModelOrderDetails> getOrderDetails(int orderId) throws SQLException;
}
