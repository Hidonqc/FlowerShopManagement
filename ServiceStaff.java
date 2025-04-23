package FlowerShop.Controller.Service;

import FlowerShop.Controller.Connection.DatabaseConnection;
import FlowerShop.Model.*;
import FlowerShop.View.AbsStaff;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ServiceStaff implements AbsStaff {

    private final Connection con;

    public ServiceStaff() {
        this.con = DatabaseConnection.getInstance().getConnection();
    }

    // ==================== NHÂN VIÊN ====================
    @Override
    public ModelStaff getStaffInfo(int userID) throws SQLException {
        String sql = "{call sp_GetStaffInfo(?)}";

        try (CallableStatement cstmt = con.prepareCall(sql)) {
            cstmt.setInt(1, userID);

            try (ResultSet rs = cstmt.executeQuery()) {
                if (rs.next()) {
                    ModelStaff staff = new ModelStaff();
                    staff.setID_Staff(rs.getInt("staff_id"));           // sửa tên cột nếu khác
                    staff.setNameStaff(rs.getString("name"));           // sửa tên cột nếu khác
                    staff.setDateJoin(rs.getString("join_date"));       // sửa tên cột nếu khác
                    staff.setPhoneNumber(rs.getString("phone"));        // sửa tên cột nếu khác
                    staff.setRole(rs.getString("role"));                // sửa tên cột nếu khác
                    staff.setID_Manager(rs.getInt("manager_id"));       // sửa tên cột nếu khác
                    return staff;
                }
            }
        }
        return null;
    }
    @Override
    public boolean updateStaffName(int userId, String newName) throws SQLException {
        String sql = "{call sp_UpdateStaffProfile(?, ?)}";

        try (CallableStatement cstmt = con.prepareCall(sql)) {
            cstmt.setInt(1, userId);
            cstmt.setString(2, newName);
            return cstmt.executeUpdate() > 0;
        }
    }

    // ==================== KHÁCH HÀNG ====================
    @Override
    public List<ModelCustomers> getAllCustomers() throws SQLException {
        List<ModelCustomers> customers = new ArrayList<>();
        String sql = "{call sp_GetAllCustomers()}";

        try (CallableStatement cstmt = con.prepareCall(sql);
             ResultSet rs = cstmt.executeQuery()) {

            while (rs.next()) {
                customers.add(new ModelCustomers(
                        rs.getInt("customer_id"),
                        rs.getString("getCustomer_Name"),
                        rs.getString("joinDate")
                ));
            }
        }
        return customers;
    }
    @Override
    public ModelCustomers getCustomerName(int customerId) throws SQLException {
        String sql = "{call sp_GetCustomerBasicInfo(?)}";

        try (CallableStatement cstmt = con.prepareCall(sql)) {
            cstmt.setInt(1, customerId);

            try (ResultSet rs = cstmt.executeQuery()) {
                if (rs.next()) {
                    // Đảm bảo tên cột đúng
                    String customerName = rs.getString("customer_name");  // Kiểm tra tên cột trong procedure
                    String joinDate = rs.getString("join_date");          // Kiểm tra tên cột trong procedure

                    return new ModelCustomers(
                            customerId,
                            customerName,     // Trả về tên khách hàng
                            joinDate          // Trả về ngày gia nhập
                    );
                }
            }
        }
        return null;
    }

    // ==================== ĐƠN HÀNG ====================
    @Override
    public int createOrder(int customerId, int staffId) throws SQLException {
        String sql = "{call sp_CreateNewOrder(?, ?, ?)}";

        try (CallableStatement cstmt = con.prepareCall(sql)) {
            cstmt.setInt(1, customerId);
            cstmt.setInt(2, staffId);
            cstmt.registerOutParameter(3, Types.INTEGER);
            cstmt.execute();
            return cstmt.getInt(3);
        }
    }
    @Override
    public boolean addOrderDetail(int orderId, int productId, int quantity)
            throws SQLException {
        String sql = "{call sp_ProcessOrderItem(?, ?, ?)}";  // Tên procedure

        try (CallableStatement cstmt = con.prepareCall(sql)) {
            // Thiết lập tham số vào statement
            cstmt.setInt(1, orderId);         // Tham số orderId
            cstmt.setInt(2, productId);       // Tham số productId
            cstmt.setInt(3, quantity);        // Tham số quantity

            // Thực thi câu lệnh
            cstmt.execute();
            return true;  // Nếu không có lỗi thì trả về true
        } catch (SQLException e) {
            // Kiểm tra mã lỗi SQLState, xử lý theo từng mã
            if (e.getSQLState().equals("45000")) {
                // Nếu gặp lỗi "45000", có thể là lỗi thiếu hàng (hoặc bạn có thể xử lý theo thông báo lỗi này)
                System.out.println("❌ Lỗi: " + e.getMessage());  // In thông báo lỗi
            }
            // Nếu có lỗi khác, ném lại lỗi gốc
            throw e;
        }
    }
    // ==================== SẢN PHẨM ====================
    @Override
    public boolean addProductToInventory(ModelProduct product) throws SQLException {
        String sql = "{call sp_AddNewProduct(?, ?, ?, ?, ?, ?)}";

        try (CallableStatement cstmt = con.prepareCall(sql)) {
            cstmt.setString(1, product.getProductName());
            cstmt.setDouble(2, product.getPrice());
            cstmt.setString(3, product.getType());
            cstmt.setString(4, product.getSeason());
            cstmt.setInt(5, product.getStock());
            return cstmt.executeUpdate() > 0;
        }
    }
    @Override
    public List<ModelOrderDetails> getOrderDetails(int orderId) throws SQLException {

        List<ModelOrderDetails> orderDetailsList = new ArrayList<>();
        String sql = "{call sp_GetOrderDetails(?)}";

        try (CallableStatement cstmt = con.prepareCall(sql)) {
            cstmt.setInt(1, orderId);

            try (ResultSet rs = cstmt.executeQuery()) {
                while (rs.next()) {
                    ModelOrderDetails detail = new ModelOrderDetails();
                    detail.setorder_ID(rs.getInt("order_id"));
                    detail.setproduct_ID(rs.getInt("product_id"));
                    detail.setquantity(rs.getInt("quantity"));
                    detail.settotal(rs.getInt("unit_price"));
                    orderDetailsList.add(detail);
                }
            }
        }
        return orderDetailsList;
    }
    }

