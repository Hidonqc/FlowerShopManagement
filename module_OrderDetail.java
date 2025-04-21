package module;

public class module_OrderDetail {
	// Lớp OrderDetail đại diện cho chi tiết từng sản phẩm trong đơn hàng

	private int order_ID; // Mã đơn hàng
	private int product_ID; // Mã sản phẩm
	private String product_Name; // Tên sản phẩm
	private int quantity; // Số lượng mua
	private double totalAmount; // Thành tiền cho sản phẩm này

	// Constructor với tham số đầy đủ
	public module_OrderDetail(int order_ID, int product_ID, String product_Name, int quantity, double totalAmount) {
		this.order_ID = order_ID;
		this.product_ID = product_ID;
		this.product_Name = product_Name;
		this.quantity = quantity;
		this.totalAmount = totalAmount;
	}

	// Auto-constructor: Constructor mặc định (không tham số)
	public module_OrderDetail() {
		this.order_ID = 0;
		this.product_ID = 0;
		this.product_Name = "";
		this.quantity = 0;
		this.totalAmount = 0.0;
	}

	// Getters
	public int getOrder_ID() {
		return order_ID;
	}

	public int getProduct_ID() {
		return product_ID;
	}

	public String getProduct_Name() {
		return product_Name;
	}

	public int getQuantity() {
		return quantity;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	// Setters
	public void setOrder_ID(int order_ID) {
		this.order_ID = order_ID;
	}

	public void setProduct_ID(int product_ID) {
		this.product_ID = product_ID;
	}

	public void setProduct_Name(String product_Name) {
		this.product_Name = product_Name;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	// Ghi đè toString() để in thông tin dễ đọc
	@Override
	public String toString() {
		return "OrderDetail{" + "order_ID=" + order_ID + ", product_ID=" + product_ID + ", product_Name='"
				+ product_Name + '\'' + ", quantity=" + quantity + ", totalAmount=" + totalAmount + '}';
	}
}
