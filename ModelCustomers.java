package FlowerShop.Model;

public class ModelCustomers {
    private int customer_ID;
    private String customer_Name;
    private String joinDate;

    // Getter and Setter
    public int getCustomer_ID() {
        return customer_ID;
    }

    public String getCustomer_Name() {
        return customer_Name;
    }

    public void setCustomer_Name(String customer_Name) {
        this.customer_Name = customer_Name;
    }

    public String getJoinDate() {
        return joinDate;
    }

    public ModelCustomers() {
    }

    // Constructor
    public ModelCustomers( int customerID, String customerName, String dateJoin) {
        this.customer_ID = customerID;
        this.customer_Name = customerName;
        this.joinDate = dateJoin;
    }
}
