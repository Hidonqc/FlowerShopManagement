package FlowerShop.Model;

public class ModelOrderDetails {
	public int getorder_ID() {
        return order_ID;
    }
    public void setorder_ID(int order_ID){
        this.order_ID = order_ID;
    }


    public int getproduct_ID() {
        return product_ID;
    }
    public void setproduct_ID(int product_ID){
        this.product_ID = product_ID;
    }

    public int getquantity() {
        return quantity;
    }
    public void setquantity(int quantity){
        this.quantity = quantity;
    }

    public float gettotal() {
        return total;
    }

    public void settotal(int total){
        this.total = total;
    }
    public ModelOrderDetails() {
    }

    public ModelOrderDetails(int orderID, int productID, int quantity, float total) {
        this.order_ID = orderID;
        this.product_ID = productID;
        this.quantity = quantity ;
        this.total = total;
    }

    private int order_ID;
    private int product_ID;
    private int quantity;
    private float total;

}
