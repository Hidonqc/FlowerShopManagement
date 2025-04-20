package module;

import java.sql.Date;

// Lớp Order đại diện cho thông tin của một đơn hàng
public class module_Order {
	private int order_ID; // Mã đơn hàng
	private String customer_Name; // Tên khách hàng
	private int customer_ID; // Mã khách hàng
	private Date order_Day; // Ngày đặt hàng
	private double total_Amount; // Tổng số tiền của đơn hàng

	// Constructor khởi tạo đối tượng Order
	public module_Order(int order_ID, String customer_Name, int customer_ID, Date order_Day, double total_Amount) {
		this.order_ID = order_ID;
		this.customer_Name = customer_Name;
		this.customer_ID = customer_ID;
		this.order_Day = order_Day;
		this.total_Amount = total_Amount;
	}

	// Các getter và setter (hàm lấy và gán giá trị)
	public int getOrder_ID() {
		return order_ID;
	}

	public void setOrder_ID(int order_ID) {
		this.order_ID = order_ID;
	}

	public String getCustomer_Name() {
		return customer_Name;
	}

	public void setCustomer_Name(String customer_Name) {
		this.customer_Name = customer_Name;
	}

	public int getCustomer_ID() {
		return customer_ID;
	}

	public void setCustomer_ID(int customer_ID) {
		this.customer_ID = customer_ID;
	}

	public Date getOrder_Day() {
		return order_Day;
	}

	public void setOrder_Day(Date order_Day) {
		this.order_Day = order_Day;
	}

	public double getTotal_Amount() {
		return total_Amount;
	}

	public void setTotal_Amount(double total_Amount) {
		this.total_Amount = total_Amount;
	}

	// Hiển thị thông tin đơn hàng
	@Override
	public String toString() {
		return "Order{" + "order_ID=" + order_ID + ", customer_Name='" + customer_Name + '\'' + ", customer_ID="
				+ customer_ID + ", order_Day=" + order_Day + ", total_Amount=" + total_Amount + '}';
	}
}
