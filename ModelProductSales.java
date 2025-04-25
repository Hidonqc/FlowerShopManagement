package FlowerShop.Model;

public class ModelProductSales {
	private int productId;
    private String productName;
    private double totalSales;

    public ModelProductSales(int productId, String productName, double totalSales) {
        this.productId = productId;
        this.productName = productName;
        this.totalSales = totalSales;
    }

    // Getters
    public int getProductId() { return productId; }
    public String getProductName() { return productName; }
    public double getTotalSales() { return totalSales; }
}
