package FlowerShop.Model;

public class ModelOrders {

    public int getorder_ID() {
        return order_ID;
    }

    public int getCustomer_ID() {
        return customer_ID;
    }
    public String getCustomer_Name(){
        return customer_Name;
    }

    public String getorder_Day(){
        return order_Day;
    }
    public int getTotal_Amount() {
        return total_Amount;
    }

    public ModelOrders(int order_ID, int customer_ID, String customer_Name,String order_Day, int total_Amount){
        this.order_ID = order_ID;
        this.customer_ID = customer_ID;
        this.customer_Name = customer_Name;
        this.order_Day = order_Day;
        this.total_Amount = total_Amount;
    }

    private int order_ID;
    private String customer_Name;
    private int customer_ID;
    private String order_Day;
    private int total_Amount;
}
