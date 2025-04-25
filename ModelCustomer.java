package FlowerShop.Model;

public class ModelCustomer extends ModelUser {
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
    public void setCustomer_ID( int customer_ID) {
        this.customer_ID = customer_ID;
    }


    public String getJoinDate() {
        return joinDate;
    }

    public ModelCustomer() {
    }

    // Constructor
    public ModelCustomer( int customer_ID, String customerName, String dateJoin) {
        this.customer_ID = customer_ID;
        this.customer_Name = customerName;
        this.joinDate = dateJoin;
    }
}
