package SP;

public class Product {
	private int id;
    private String name;
    private double price;
    private int stock;
    private String type;     
    private String season;
    
    public Product() {
		// TODO Auto-generated constructor stub
	}

	public Product(int id, String name, double price, int stock, String type, String season) {
		super();
		this.id = id;
		this.name = name;
		this.price = price;
		this.stock = stock;
		this.type = type;
		this.season = season;
	}
	// getter & setter
	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getPrice() {
		return price;
	}

	public void setPrice(double price) {
		this.price = price;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getSeason() {
		return season;
	}

	public void setSeason(String season) {
		this.season = season;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", price=" + price + ", stock=" + stock + ", type=" + type
				+ ", season=" + season + "]";
	}
    
	
}
