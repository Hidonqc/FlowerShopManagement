package FlowerShop.Controller.Service;

import FlowerShop.Controller.Connection.DatabaseConnection;
import FlowerShop.Model.*;
import FlowerShop.View.AbsAdmin;

import java.sql.*;
import java.util.*;
import java.util.Date;

public  class ServiceAdmin implements AbsAdmin {

    private final Connection con;

    public ServiceAdmin() {
        this.con = DatabaseConnection.getInstance().getConnection();
    }
    @Override
    public List<ModelStaff> getAllStaff() {
        List<ModelStaff> staffList = new ArrayList<>();
        String sql = "{call sp_GetAllStaff()}";

        try (CallableStatement cstmt = con.prepareCall(sql);
             ResultSet rs = cstmt.executeQuery()) {

            while (rs.next()) {
                staffList.add(new ModelStaff(
                ));
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy danh sách NV: " + e.getMessage());
        }
        return staffList;
    }

    @Override
    public boolean addStaff(ModelUser staff) {
        return false;
    }

    @Override
    public boolean updateStaff(ModelUser staff) {
        return false;
    }

    @Override
    public boolean addStaff(ModelStaff staff) {
        String sql = "{call sp_AddStaff(?, ?, ?, ?, ?)}";

        try (CallableStatement cstmt = con.prepareCall(sql)) {
            cstmt.setInt(1, staff.getID_Staff());
            cstmt.setString(2, staff.getNameStaff());
            cstmt.setString(3, staff.getJoinDate());
            cstmt.setString(4, staff.getPhoneNumber());
            cstmt.setString(5, staff.getRole());
            cstmt.setInt(6, staff.getID_Manager());

            return cstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi thêm NV: " + e.getMessage());
            return false;
        }
    }
    @Override
    public boolean updateStaff(ModelStaff staff) {
        String sql = "{call sp_UpdateStaff(?, ?, ?, ?)}";

        try (CallableStatement cstmt = con.prepareCall(sql)) {
            cstmt.setInt(1, staff.getID_Staff());
            cstmt.setString(2, staff.getNameStaff());
            cstmt.setString(3, staff.getJoinDate());
            cstmt.setString(4, staff.getPhoneNumber());
            cstmt.setString(5, staff.getRole());
            cstmt.setInt(6, staff.getID_Manager());
            return cstmt.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Lỗi cập nhật NV: " + e.getMessage());
            return false;
        }
    }

    public ModelStaff getID_Staff(int staffId) {
        String sql = "SELECT * FROM Staff WHERE id = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, staffId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ModelStaff staff = new ModelStaff();
                    staff.setID_Staff(rs.getInt("id"));
                    staff.setNameStaff(rs.getString("name"));
                    staff.setDateJoin(rs.getString("join_date"));
                    staff.setPhoneNumber(rs.getString("phone"));
                    staff.setRole(rs.getString("role"));
                    staff.setID_Manager(rs.getInt("manager_id"));
                    return staff;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    // ==================== HÓA ĐƠN ====================
    @Override
    public List<ModelOrders> getInvoices(String period, Date date) {
        List<ModelOrders> invoices = new ArrayList<>();
        String sql = "{call sp_GetInvoices(?, ?)}";

        try (CallableStatement cstmt = con.prepareCall(sql)) {
            cstmt.setString(1, period);
            cstmt.setDate(2, new java.sql.Date(date.getTime()));

            try (ResultSet rs = cstmt.executeQuery()) {
                while (rs.next()) {
                    invoices.add(new ModelOrders(
                            rs.getInt("order_id"),
                            rs.getInt("customer_id"),
                            rs.getString("customer_name"),
                            rs.getString("order_date"),
                            rs.getInt("total_amount")
                    ));
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi lấy hóa đơn: " + e.getMessage());
        }
        return invoices;
    }

    // ==================== DOANH THU ====================
    @Override
    public Map<String, Double> getRevenueReport(String period, Date date) {
        Map<String, Double> report = new LinkedHashMap<>();
        String sql = "{call sp_RevenueReportByPeriod(?, ?)}";

        try (CallableStatement cstmt = con.prepareCall(sql)) {
            cstmt.setString(1, period);
            cstmt.setDate(2, new java.sql.Date(date.getTime()));

            try (ResultSet rs = cstmt.executeQuery()) {
                while (rs.next()) {
                    report.put(rs.getString("period"), rs.getDouble("total"));
                }
            }
        } catch (SQLException e) {
            System.err.println("Lỗi báo cáo doanh thu: " + e.getMessage());
        }
        return report;
    }

    // ==================== SẢN PHẨM ====================
    @Override
    public List<ModelProduct> getAllProducts() throws SQLException {
        List<ModelProduct> products = new ArrayList<>();
        String sql = "{call sp_GetAllProducts()}";

        try (CallableStatement cstmt = con.prepareCall(sql);
             ResultSet rs = cstmt.executeQuery()) {

            while (rs.next()) {
                products.add(new ModelProduct(
                ));
            }
        }
        return products;
    }

    @Override
    public int generateNewProductId() throws SQLException {
        String sql = "{call sp_GetMaxProductId(?)}";
        try (CallableStatement cstmt = con.prepareCall(sql)) {
            cstmt.registerOutParameter(1, Types.INTEGER);
            cstmt.execute();
            return cstmt.getInt(1) + 1;
        }
    }

    @Override
    public boolean addProduct(ModelProduct product) throws SQLException {
        String sql = "{call sp_AddProduct(?, ?, ?, ?, ?, ?)}";
        try (CallableStatement cstmt = con.prepareCall(sql)) {
            cstmt.setInt(1, generateNewProductId());
            cstmt.setString(2, product.getProductName());
            cstmt.setString(3, product.getType());
            cstmt.setString(4, product.getSeason());
            cstmt.setDouble(5, product.getPrice());
            cstmt.setInt(6, product.getStock());
            cstmt.executeUpdate();
        }
        return false;
    }

    @Override
    public boolean updateProduct(ModelProduct product) throws SQLException {
        String sql = "{call sp_UpdateProduct(?, ?, ?, ?, ?, ?)}";
        try (CallableStatement cstmt = con.prepareCall(sql)) {
            cstmt.setInt(1, product.getProductID());
            cstmt.setString(2, product.getProductName());
            cstmt.setString(3, product.getType());
            cstmt.setString(4, product.getSeason());
            cstmt.setDouble(5, product.getPrice());
            cstmt.setInt(6, product.getStock());
            cstmt.executeUpdate();
        }
        return false;
    }

    public ModelProduct getProductById(int productId) {
        String sql = "SELECT * FROM Product WHERE id = ?";
        try (PreparedStatement stmt = con.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    ModelProduct product = new ModelProduct();
                    product.setProductID(rs.getInt("id"));
                    product.setProductName(rs.getString("name"));
                    product.setPrice(rs.getInt("price"));
                    product.setType(rs.getString("type"));
                    product.setSeason(rs.getString("season"));
                    product.setStock(rs.getInt("stock")); // hoặc "quantity", tuỳ cột trong DB
                    return product;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }


    // ==================== THỐNG KÊ ====================
    @Override
    public List<ModelProductSales> getProductStatistics() {
        List<ModelProductSales> salesList = new ArrayList<>();
        String sql = "{call sp_GetProductStatistics()}";

        try (CallableStatement cstmt = con.prepareCall(sql);
             ResultSet rs = cstmt.executeQuery()) {

            while (rs.next()) {
                salesList.add(new ModelProductSales(
                        rs.getInt("product_id"),
                        rs.getString("name"),
                        rs.getDouble("total_sales")
                ));
            }
            bubbleSort(salesList);
        } catch (SQLException e) {
            System.err.println("Lỗi thống kê: " + e.getMessage());
        }
        return salesList;
    }
    @Override
    public void bubbleSort(List<ModelProductSales> list) {
        int n = list.size();
        for (int i = 0; i < n-1; i++) {
            for (int j = 0; j < n-i-1; j++) {
                if (list.get(j).getTotalSales() < list.get(j+1).getTotalSales()) {
                    Collections.swap(list, j, j+1);
                }
            }
        }
    }

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
}