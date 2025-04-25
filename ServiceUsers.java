package FlowerShop.Controller.Service;

import FlowerShop.Controller.Connection.DatabaseConnection;
import FlowerShop.Model.ModelCustomer;
import FlowerShop.Model.ModelStaff;
import FlowerShop.Model.ModelLogin;
import FlowerShop.Model.ModelUser;
import FlowerShop.View.AbsUser;
import java.sql.*;

public class ServiceUsers implements AbsUser {
    private final Connection con;

    public ServiceUsers() throws SQLException {
        DatabaseConnection dbConnection = DatabaseConnection.getInstance();
        this.con = dbConnection.getConnection();
        // Kiểm tra kết nối
        if (this.con == null) {
            throw new SQLException("Không thể kết nối đến cơ sở dữ liệu: Connection is null");
        }
    }

    @Override
    public ModelUser login(ModelLogin login) throws SQLException {
        // Kiểm tra đối tượng login
        if (login == null || login.getEmail() == null || login.getPassword() == null) {
            throw new SQLException("Thông tin đăng nhập không hợp lệ: Email hoặc mật khẩu không được để trống");
        }

        String sql = "{call sp_LoginUser(?, ?)}"; // Gọi stored procedure

        try (CallableStatement cstmt = con.prepareCall(sql)) {
            // Thiết lập tham số đầu vào
            cstmt.setString(1, login.getEmail());
            cstmt.setString(2, login.getPassword());
         
            try (ResultSet rs = cstmt.executeQuery()) {
                if (rs.next()) {
                    String role = rs.getString("Role");
                    ModelUser user;

                    // Tạo đối tượng tùy theo role
                    switch (role) {
                        case "Khach Hang" -> {
                            ModelCustomer customer = new ModelCustomer();
                            customer.setCustomer_ID(rs.getInt("customer_ID")); 
                            user = customer;
                        }
                        case "Nhan Vien" -> {
                            ModelStaff staff = new ModelStaff();
                            staff.setID_Staff(rs.getInt("ID_Staff")); 
                            user = staff;
                        }
                        default -> user = new ModelUser(); 
                    }
        } 
       }
                catch (SQLException e) {
            System.err.println("Lỗi khi thực hiện đăng nhập: " + e.getMessage());
            throw e;
        }
        return null; 
    }
}
}