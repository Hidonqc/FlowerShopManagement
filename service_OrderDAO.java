package service;

import java.sql.Connection;
import java.sql.Date;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;

import module.module_Order;
import module.module_OrderDetail;

// Lớp OrderDAO dùng để thao tác với cơ sở dữ liệu về đơn hàng
public class service_OrderDAO {

	// Hàm kết nối tới cơ sở dữ liệu
	private static Connection connect() throws SQLException {
		String url = "jdbc:sqlserver://localhost:1433;databaseName=FlowerManagement;encrypt=true;trustServerCertificate=true";
		String user = "sa"; // Tài khoản SQL Server
		String password = "123456"; // Mật khẩu SQL Server
		return DriverManager.getConnection(url, user, password);
	}

	// Hàm tạo đơn hàng mới, bao gồm cả chi tiết đơn hàng và cập nhật tồn kho
	public static void createOrder(module_Order order, List<module_OrderDetail> details) throws SQLException {
		Connection conn = connect();
		try {
			// Bắt đầu giao dịch (transaction)
			conn.setAutoCommit(false);

			// Kiểm tra tồn kho cho từng sản phẩm
			for (module_OrderDetail d : details) {
				PreparedStatement checkStock = conn
						.prepareStatement("SELECT product_Stock FROM Products WHERE product_ID = ?");
				checkStock.setInt(1, d.getProduct_ID());
				ResultSet rs = checkStock.executeQuery();
				if (rs.next()) {
					int stock = rs.getInt("product_Stock");
					if (stock < d.getQuantity()) {
						throw new Exception("Số lượng tồn kho không đủ cho sản phẩm ID: " + d.getProduct_ID());
					}
				} else {
					throw new Exception("Không tìm thấy sản phẩm với ID: " + d.getProduct_ID());
				}
			}

			// Thêm đơn hàng vào bảng Orders
			PreparedStatement insertOrder = conn.prepareStatement("INSERT INTO Orders VALUES (?, ?, ?, ?, ?)");
			insertOrder.setInt(1, order.getOrder_ID());
			insertOrder.setString(2, order.getCustomer_Name());
			insertOrder.setInt(3, order.getCustomer_ID());
			insertOrder.setDate(4, order.getOrder_Day());
			insertOrder.setDouble(5, order.getTotal_Amount());
			insertOrder.executeUpdate();

			// Thêm từng chi tiết đơn hàng vào bảng OrderDetails và cập nhật tồn kho
			for (module_OrderDetail d : details) {
				// Lấy giá sản phẩm từ bảng Products
				double price = 0;
				PreparedStatement priceStmt = conn.prepareStatement("SELECT product_Price FROM Products WHERE product_ID = ?");
				priceStmt.setInt(1, d.getProduct_ID());
				ResultSet rsPrice = priceStmt.executeQuery();
				if (rsPrice.next()) {
					price = rsPrice.getDouble("product_Price");
				}

				// Thêm chi tiết đơn hàng
				PreparedStatement insertDetail = conn.prepareStatement("INSERT INTO OrderDetails VALUES (?, ?, ?, ?)");
				insertDetail.setInt(1, d.getOrder_ID());
				insertDetail.setInt(2, d.getProduct_ID());
				insertDetail.setInt(3, d.getQuantity());
				insertDetail.setDouble(4, d.getQuantity() * price);
				insertDetail.executeUpdate();

				// Trừ tồn kho
				PreparedStatement updateStock = conn.prepareStatement("UPDATE Products SET product_Stock = product_Stock - ? WHERE product_ID = ?");
				updateStock.setInt(1, d.getQuantity());
				updateStock.setInt(2, d.getProduct_ID());
				updateStock.executeUpdate();
			}

			// Ghi nhận thay đổi vào cơ sở dữ liệu
			conn.commit();
			System.out.println("Tạo đơn hàng thành công!");
		} catch (Exception e) {
			// Nếu có lỗi, quay lui (rollback) lại toàn bộ
			conn.rollback();
			System.out.println("Lỗi khi tạo đơn hàng: " + e.getMessage());
		} finally {
			// Đảm bảo đóng kết nối và bật lại chế độ tự động commit
			conn.setAutoCommit(true);
			conn.close();
		}
	}

	// Hàm hiển thị tất cả các đơn hàng và chi tiết
	public static void showOrders() throws SQLException {
		Connection conn = connect();
		Statement stmt = conn.createStatement();
		ResultSet rs = stmt.executeQuery("SELECT * FROM Orders");

		while (rs.next()) {
			int id = rs.getInt("order_ID");
			String customer = rs.getString("customer_Name");
			int customerId = rs.getInt("customer_ID");
			Date day = rs.getDate("order_Day");
			double total = rs.getDouble("total_Amount");

			System.out.println("Order ID: " + id + ", Customer: " + customer + ", Date: " + day + ", Total: " + total);

			// Hiển thị chi tiết từng đơn hàng
			PreparedStatement stmtDetail = conn.prepareStatement("SELECT * FROM OrderDetails WHERE order_ID = ?");
			stmtDetail.setInt(1, id);
			ResultSet rsDetail = stmtDetail.executeQuery();
			while (rsDetail.next()) {
				System.out.println("Product ID: " + rsDetail.getInt("product_ID") + ", Quantity: "+ rsDetail.getInt("quantity") + ", Total: " + rsDetail.getDouble("totalAmount"));
			}
		}
		conn.close();
	}
}
