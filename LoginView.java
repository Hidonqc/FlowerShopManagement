package FlowerShop.MainFrame;

import FlowerShop.Controller.Service.ServiceUser;
import FlowerShop.Model.ModelLogin;
import FlowerShop.Model.ModelUser;

import java.util.Scanner;

public class LoginView {
    private final ServiceUser userService = new ServiceUser();
    private final Scanner scanner = new Scanner(System.in);

    public ModelUser showLoginMenu() {
        while (true) {
            System.out.println("\n🌸🌸🌸 CỬA HÀNG HOA NÈ 🌸🌸🌸");
            System.out.println("1. Đăng nhập");
            System.out.println("2. Thoát");
            System.out.print("➔ Chọn: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Clear buffer

            if (choice == 2) {
                System.out.println("👋 Hẹn gặp lại!");
                System.exit(0);
            }

            System.out.print("\n📧 Email: ");
            String email = scanner.nextLine();

            System.out.print("🔒 Mật khẩu: ");
            String password = scanner.nextLine();

            try {
                ModelUser user = userService.login(new ModelLogin(email, password));
                if (user != null) {
                    System.out.println("\n✅ Đăng nhập thành công!");
                    return user;
                }
                System.out.println("❌ Sai thông tin đăng nhập!");
            } catch (Exception e) {
                System.out.println("❗ Lỗi hệ thống: " + e.getMessage());
            }
        }
    }
}