package SP;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductDAO {
	private Connection connection;
	
	public ProductDAO(Connection connection) {
        this.connection = connection;
	}
	
	//Thêm sản phẩm
	public void addProduct(Product product) throws SQLException {
        String sql = "INSERT INTO Products (id, name, type, season, price, stock) VALUES (?, ?, ?, ?, ?, ?)";
        // dùng PreparedStatement để truyền dữ liệu 
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
        	
            stmt.setInt(1, product.getId());
            stmt.setString(2, product.getName());
            stmt.setString(3, product.getType());
            stmt.setString(4, product.getSeason());
            stmt.setDouble(5, product.getPrice());
            stmt.setInt(6, product.getStock());
            
            int rowsAffected = stmt.executeUpdate(); // tiến hành thêm dữ liệu
            
            if (rowsAffected > 0) {
                System.out.println("Thêm sản phẩm thành công!");
            } else {
                System.out.println("Thêm sản phẩm thất bại.");
            }
      }
	}
     // sửa sản phẩm
        public void updateProduct(Product product) throws SQLException {
            // cập nhật sp dự vào id
        	String sql = "UPDATE Products SET name=?, type=?, season=?, price=?, stock=? WHERE id=?";
            
            try (PreparedStatement stmt = connection.prepareStatement(sql)) {
                // truyền dữ liệu vào các dấu ?
            	stmt.setString(1, product.getName());
                stmt.setString(2, product.getType());
                stmt.setString(3, product.getSeason());
                stmt.setDouble(4, product.getPrice());
                stmt.setInt(5, product.getStock());
                stmt.setInt(6, product.getId());
                int rows = stmt.executeUpdate();
                if (rows > 0) {
                    System.out.println("Sửa sản phẩm thành công");
                } else {
                    System.out.println("Không tìm thấy sản phẩm để sửa");
                }
            }   
        }
        // Hiển thị sản phẩm
        public List<Product> getAllProducts() throws SQLException {
            // khởi tạo ds để chứa sp
        	List<Product> products = new ArrayList<>();
            String sql = "SELECT * FROM Products";
            
            try (Statement stmt = connection.createStatement(); 
            	 ResultSet rs = stmt.executeQuery(sql)) {
                while (rs.next()) {
                	Product p = new Product(
                            rs.getInt("id"),
                            rs.getString("name"),
                            rs.getDouble("price"),
                            rs.getInt("stock"),
                            rs.getString("type"),
                            rs.getString("season")
                        );
                        products.add(p);
                }
            }
            return products;
        }
        
        // Tìm kiếm sản phẩm theo tên/ mùa / loại
        public List<Product> searchProducts(String name, String type, String season) throws SQLException {
            List<Product> results = new ArrayList<>();
            // tạo câu lệnh sql động, cho 1=1 để dễ nối điều kiện bằng AND
            StringBuilder sql = new StringBuilder("SELECT * FROM Products WHERE 1=1");
            
            if (name != null && !name.isEmpty()) {
                sql.append(" AND name LIKE ?");
            }
            if (type != null && !type.isEmpty()) {
                sql.append(" AND type LIKE ?");
            }
            if (season != null && !season.isEmpty()) {
                sql.append(" AND season LIKE ?");
            }

            try (PreparedStatement stmt = connection.prepareStatement(sql.toString())) {
                int index = 1;
                if (name != null && !name.isEmpty()) 
                	stmt.setString(index++, "%" + name + "%");
                if (type != null && !type.isEmpty()) 
                	stmt.setString(index++, "%" + type + "%");
                if (season != null && !season.isEmpty()) 
                	stmt.setString(index++, "%" + season + "%");

                try (ResultSet rs = stmt.executeQuery()) {
                    while (rs.next()) {
                    	Product p = new Product(
                                rs.getInt("id"),
                                rs.getString("name"),
                                rs.getDouble("price"),
                                rs.getInt("stock"),
                                rs.getString("type"),
                                rs.getString("season")
                            );
                            results.add(p);
                    }
                }
            }
            return results;
        
    }
}	