package totalAmount;

import java.math.BigDecimal;
import java.time.LocalDate; // Sử dụng thư viện thời gian mới
import java.util.List;

public class DonHang {
    private int orderId;
    
    private String customerName;
    
    private double totalAmount;
    
    private LocalDate orderDate; // Lưu ngày đặt hàng
    
    private List<MatHang> DanhsachMatHang; // Danh sách chi tiết mặt hàng

    // Constructors
    public DonHang() {
        this.orderDate = LocalDate.now(); // Mặc định là ngày hiện tại khi tạo
    }

    public DonHang(int orderId, String customerName, double totalAmount, LocalDate orderDate, List<MatHang> DanhsachMatHang) {
        this.orderId = orderId;
        this.customerName = customerName;
        this.totalAmount = totalAmount; // Giá trị này nên được tính toán và GÁN vào khi tạo đơn hàng
        this.orderDate = orderDate;
        this.DanhsachMatHang = DanhsachMatHang;
    }

     // --- Getters ---
    public int getOrderId() {
        return orderId;
    }

    public String getCustomerName() {
        return customerName;
    }

    public double getTotalAmount() {
      
        return totalAmount;
        
    }


    public LocalDate getOrderDate() {
        return orderDate;
    }

    public List<MatHang> getDanhSachMatHang() {
        return DanhsachMatHang;
    }

    // --- Setters ---
    public void setOrderDate(LocalDate orderDate) {
        this.orderDate = orderDate;
    }
     public void setTotalAmount(double totalAmount) { 
        this.totalAmount = totalAmount;
    }
         

	public void setDanhsachMatHang(List<MatHang> DanhsachMatHang) {
		DanhsachMatHang = DanhsachMatHang;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

    private double calculateTotalAmount() {
        double tong = 0.0;
         if (danhSachMatHang != null) {
            for (MatHang matHang : danhSachMatHang) {
                // Cần đảm bảo matHang.getDonGia() là giá tại thời điểm mua
                 tong += matHang.getSoLuong() * matHang.getDonGia();
            }
         }
        return tong;
    }

    

    @Override
    public String toString() {
        return "Don hang{" +
               "Ma don hang=" + orderId +
               ", Ten khach hang='" + customerName + '\'' +
               ", Tong doanh thu=" + totalAmount +
               ", Ngay dat hang=" + orderDate +
               
               '}';
    }
}
