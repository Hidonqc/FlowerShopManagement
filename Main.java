import FlowerShop.MainFrame.Admin.AdminView;
import FlowerShop.MainFrame.Customer.CustomerView;
import FlowerShop.MainFrame.Staff.StaffView;
import FlowerShop.Model.ModelLogin;
import FlowerShop.MainFrame.LoginView;
import FlowerShop.Model.ModelUser;
import java.sql.SQLException;


public class Main {
    public static void main(String[] args) throws SQLException {
        LoginView loginView = new LoginView();
        AdminView adminView = new AdminView();
        StaffView staffView = new StaffView();
        CustomerView customerView = new CustomerView();

        while (true) {
            ModelUser user = loginView.showLoginMenu();

            switch (user.getRole().toLowerCase()) {
                case "Quan Ly" -> adminView.showAdminMenu();
                case "Nhan Vien" -> staffView.showStaffMenu(user.getID_User());
                case "Khach Hang" -> customerView.showCustomerMenu(user.getID_User());
            }
        }
    }
}