package module;

public class module_OrderDetail {
//Lớp OrderDetail đại diện cho chi tiết từng sản phẩm trong đơn hàng

	private int order_ID; // Mã đơn hàng
	private int product_ID; // Mã sản phẩm
	private int quantity; // Số lượng mua
	private double totalAmount; // Thành tiền cho sản phẩm này

	// Constructor
	public module_OrderDetail(int order_ID, int product_ID, int quantity, double totalAmount) {
		this.order_ID = order_ID;
		this.product_ID = product_ID;
		this.quantity = quantity;
		this.totalAmount = totalAmount;
	}

	// Getters
	public int getOrder_ID() {
		return order_ID;
	}

	public int getProduct_ID() {
		return product_ID;
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

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	// Ghi đè toString() để in thông tin dễ đọc
	@Override
	public String toString() {
		return "OrderDetail{" + "order_ID=" + order_ID + ", product_ID=" + product_ID + ", quantity=" + quantity
				+ ", totalAmount=" + totalAmount + '}';
	}
}
