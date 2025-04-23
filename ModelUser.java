package FlowerShop.Model;

// Model Người dùng của hệ thống
public class ModelUser {

    private int ID_User;  // ID tài khoản
    private String email;  // Email
    private String password; // Mật khẩu
    private String verifyCode; // Mã xác minh
    private String role; // Vai trò : Khách Hàng, Nhân Viên, Quản lý

    // Constructor đầy đủ tham số
    public ModelUser() {
        this.ID_User = ID_User;
        this.email = email;
        this.password = password;
        this.verifyCode = verifyCode;
        this.role = role;
    }

    // Constructor mặc định
    public ModelUser(int userId, String email, String password, String verifyCode, String role) {
    }

    // Getter và Setter
    public int getID_User() {
        return ID_User;
    }

    public void setID_User(int ID_User) {
        this.ID_User = ID_User;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getVerifyCode() {
        return verifyCode;
    }

    public void setVerifyCode(String verifyCode) {
        this.verifyCode = verifyCode;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}