package FlowerShop.Controller.Service;

import FlowerShop.Controller.Connection.DatabaseConnection;
import FlowerShop.Model.ModelLogin;
import FlowerShop.Model.ModelUser;
import FlowerShop.View.AbsUser;

import java.sql.*;

// Controller Đăng ký/Đăng nhập vào hệ thống

public class ServiceUser implements AbsUser {

    private final Connection con;

    public ServiceUser() {
        this.con = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public ModelUser login(ModelLogin login) throws SQLException {
        String sql = "{call sp_LoginUser(?, ?)}"; // Gọi stored procedure

        try (CallableStatement cstmt = con.prepareCall(sql)) {
            // Thiết lập tham số đầu vào
            cstmt.setString(1, login.getEmail());
            cstmt.setString(2, login.getPassword());

            try (ResultSet rs = cstmt.executeQuery()) {
                if (rs.next()) {
                    return new ModelUser(
                            rs.getInt("user_id"),
                            rs.getString("email"),
                            rs.getString("password"),
                            rs.getString("verifyCode"),
                            rs.getString("role")
                    );
                }
            }
        }
        return null;
    }
}