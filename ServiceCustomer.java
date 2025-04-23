package FlowerShop.Controller.Service;

import FlowerShop.Controller.Connection.DatabaseConnection;
import FlowerShop.Model.ModelCustomers;
import FlowerShop.Model.ModelOrderDetails;
import FlowerShop.Model.ModelOrders;
import FlowerShop.Model.ModelProduct;
import FlowerShop.View.AbsCustomer;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceCustomer implements AbsCustomer {

    private final Connection con;

    public ServiceCustomer() {
        this.con = DatabaseConnection.getInstance().getConnection();
    }

    // ============ TÌM KIẾM SẢN PHẨM ============
    @Override
    public List<ModelProduct> searchProducts(String name, String type, String season) throws SQLException {
        List<ModelProduct> results = new ArrayList<>();
        String sql = "{call sp_SearchProducts(?, ?, ?)}";

        try (CallableStatement cstmt = con.prepareCall(sql)) {
            cstmt.setString(1, name);
            cstmt.setString(2, type);
            cstmt.setString(3, season);

            try (ResultSet rs = cstmt.executeQuery()) {
                while (rs.next()) {
                    results.add(new ModelProduct(
                    ));
                }
            }
        }
        return results;
    }

    // ============ THÔNG TIN KHÁCH HÀNG ============
    @Override
    public ModelCustomers getCustomerInfo(int userId) throws SQLException {
        String sql = "{call sp_GetCustomerInfo(?)}";

        try (CallableStatement cstmt = con.prepareCall(sql)) {
            cstmt.setInt(1, userId);

            try (ResultSet rs = cstmt.executeQuery()) {
                if (rs.next()) {
                    return new ModelCustomers(
                            rs.getInt("customer_id"),
                            rs.getString("name"),
                            rs.getString("joinDate")
                    );
                }
            }
        }
        return null;
    }

    // ============ CẬP NHẬT THÔNG TIN ============
    @Override
    public void reNameCustomer(ModelCustomers data) throws SQLException {
        String sql = "{call sp_UpdateCustomerProfile(?, ?, ?)}";

        try (CallableStatement cstmt = con.prepareCall(sql)) {
            cstmt.setInt(1, data.getCustomer_ID());
            cstmt.setString(2, data.getCustomer_Name());
            cstmt.setString(3, data.getJoinDate());
            cstmt.executeUpdate();
        }
    }

    // ============ QUẢN LÝ ĐƠN HÀNG ============
    @Override
    public List<ModelOrderDetails> getOrderDetails(int orderId) throws SQLException {
        List<ModelOrderDetails> details = new ArrayList<>();
        String sql = "{call sp_GetOrderDetails(?)}";

        try (CallableStatement cstmt = con.prepareCall(sql)) {
            cstmt.setInt(1, orderId);

            try (ResultSet rs = cstmt.executeQuery()) {
                while (rs.next()) {
                    details.add(new ModelOrderDetails(
                            rs.getInt("order_id"),
                            rs.getInt("product_id"),
                            rs.getInt("quantity"),
                            rs.getFloat("unit_price")
                    ));
                }
            }
        }
        return details;
    }
    @Override
    public boolean addProductToOrder(int orderId, int productId, int quantity) {
        String sql = "{call sp_ProcessOrder(?, ?, ?)}";

        try (CallableStatement cstmt = con.prepareCall(sql)) {
            cstmt.setInt(1, orderId);
            cstmt.setInt(2, productId);
            cstmt.setInt(3, quantity);
            cstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            System.err.println("Lỗi xử lý đơn hàng: " + e.getMessage());
            return false;
        }
    }

    // ============ LỊCH SỬ ĐƠN HÀNG ============
    @Override
    public List<ModelOrders> getAllOrders(int customerId) throws SQLException {
        List<ModelOrders> orders = new ArrayList<>();
        String sql = "{call sp_GetCustomerOrders(?)}";

        try (CallableStatement cstmt = con.prepareCall(sql)) {
            cstmt.setInt(1, customerId);

            try (ResultSet rs = cstmt.executeQuery()) {
                while (rs.next()) {
                    orders.add(new ModelOrders(
                            rs.getInt("order_id"),
                            rs.getInt("customer_id"),
                            rs.getString("customer_name"),
                            rs.getString("order_day"),
                            rs.getInt("total_amount")
                    ));
                }
            }
        }
        return orders;
    }

    // ============ KIỂM TRA TỒN KHO ============
    @Override
    public boolean checkStock(int productId, int quantity) throws SQLException {
        String sql = "{call sp_CheckStock(?, ?)}";

        try (CallableStatement cstmt = con.prepareCall(sql)) {
            cstmt.setInt(1, productId);
            cstmt.registerOutParameter(2, Types.INTEGER);
            cstmt.execute();

            return cstmt.getInt(2) >= quantity;
        }
    }
}