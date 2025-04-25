package FlowerShop.Model;

public class ModelProduct {
	 public int getProductID() {
	        return productID;
	    }

	    public void setProductID(int productID) {
	        this.productID = productID;
	    }

	    public String getProductName() {
	        return productName;
	    }

	    public void setProductName(String productName) {
	        this.productName = productName;
	    }

	    public String getType() {
	        return type;
	    }

	    public void setType(String type) {
	        this.type = type;
	    }

	    public int getPrice() {
	        return price;
	    }

	    public void setPrice(int price) {
	        this.price = price;
	    }

	    public int getStock() {
	        return stock;
	    }

	    public void setStock(int stock) {
	        this.stock = stock;
	    }

	    public String getSeason() {
	        return season;
	    }

	    public void setSeason(String season) {
	        this.season = season;
	    }


	    public ModelProduct() {
	    }

	    public ModelProduct(int productID, String productName, int price, String type, int stock, String season) {
	        this.productID = productID;
	        this.productName = productName;
	        this.type = type;
	        this.price = price;
	        this.stock = stock;
	        this.season = season;
	    }

	    private int productID;
	    private String productName; //Tên loại hoa
	    private int price; //Đơn giá
	    private String type;// Loài hoa
	    private int stock;
	    private String season;
}
