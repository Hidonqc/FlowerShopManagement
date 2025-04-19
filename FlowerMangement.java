package totalAmount;


	import java.sql.*;
	import java.text.SimpleDateFormat;
	import java.util.Date;
public class FlowerMangement {
	    private static final String DB_URL = "jdbc:sqlserver://localhost:1433;databaseName=FlowerMangement;user=sa;password=your_password";

	    // Hiển thị đơn hàng theo ngày và sắp xếp
	    public static void displayOrdersByDate(String orderDate, String sortOrder) {
	        try (Connection conn = DriverManager.getConnection(DB_URL)) {
	            CallableStatement stmt = conn.prepareCall("{call GetOrdersByDateSorted(?, ?)}");
	            stmt.setDate(1, java.sql.Date.valueOf(orderDate));
	            stmt.setString(2, sortOrder);

	            ResultSet rs = stmt.executeQuery();
	            System.out.println("Orders for " + orderDate + " (Sorted by total_Amount " + sortOrder + "):");
	            while (rs.next()) {
	                System.out.printf("Order ID: %d, Customer: %s, Date: %s, Total: %.2f, Product: %s, Quantity: %d%n",
	                    rs.getInt("order_ID"),
	                    rs.getString("CustomerFullName"),
	                    rs.getString("order_Day"),
	                    rs.getFloat("total_Amount"),
	                    rs.getString("product_Name"),
	                    rs.getInt("quantity"));
	            }
	            rs.close();
	            stmt.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    // Thống kê doanh thu
	    public static void displayRevenueStats(String periodType, String startDate, int year) {
	        try (Connection conn = DriverManager.getConnection(DB_URL)) {
	            CallableStatement stmt = conn.prepareCall("{call GetRevenueStats(?, ?, ?)}");
	            stmt.setString(1, periodType);
	            if (startDate != null) {
	                stmt.setDate(2, java.sql.Date.valueOf(startDate));
	            } else {
	                stmt.setNull(2, Types.DATE);
	            }
	            stmt.setInt(3, year);

	            ResultSet rs = stmt.executeQuery();
	            System.out.println("Revenue Statistics (" + periodType + "):");
	            while (rs.next()) {
	                System.out.printf("Period: %s, Total Revenue: %.2f, Order Count: %d%n",
	                    rs.getString("Period"),
	                    rs.getFloat("TotalRevenue"),
	                    rs.getInt("OrderCount"));
	            }
	            rs.close();
	            stmt.close();
	        } catch (SQLException e) {
	            e.printStackTrace();
	        }
	    }

	    public static void main(String[] args) {
	    	import java.util.Scanner;

	    	        Scanner scanner = new Scanner(System.in);

	    	        // Nhập ngày đơn hàng muốn xem
	    	        System.out.print("Nhập ngày cần xem đơn hàng (YYYY-MM-DD): ");
	    	        String orderDate = scanner.nextLine();

	    	        System.out.print("Sắp xếp theo tổng tiền (ASC/DESC): ");
	    	        String sortOrder = scanner.nextLine().toUpperCase();
	    	        
	    	        // Hiển thị danh sách đơn hàng theo ngày đã nhập
	    	        displayOrdersByDate(orderDate, sortOrder);

	    	        // Nhập dữ liệu để thống kê doanh thu
	    	        System.out.print("Nhập loại thống kê (Day/Month/Year): ");
	    	        String periodType = scanner.nextLine();

	    	        System.out.print("Nhập ngày bắt đầu (YYYY-MM-DD, có thể để trống nếu chọn Year): ");
	    	        String startDate = scanner.nextLine();

	    	        System.out.print("Nhập năm thống kê: ");
	    	        int year = scanner.nextInt();

	    	        // Hiển thị thống kê doanh thu theo dữ liệu đã nhập
	    	        displayRevenueStats(periodType, startDate.isEmpty() ? null : startDate, year);

	    	        scanner.close();
	    	    }
	    	}
	    }
	}
}
}
