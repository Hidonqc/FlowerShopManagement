package FlowerShop.Model;

public class ModelLogin {
	
	public int getCustomer_ID (){
		 return customer_ID;
	}
	public int getID_Staff (){
		 return ID_Staff;
	}
	public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    
    public void setiID_Staff(int ID_Staff) {
        this.ID_Staff = ID_Staff;
    }
    
    public void setiCustomer_ID(int customer_ID) {
        this.customer_ID = customer_ID;
    }
    

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ModelLogin() {
    }

    public ModelLogin(String email, String password) {
        this.email = email;
        this.password = password;
    }

    private String email; //email
    private String password; // Mật khẩu
    private int customer_ID;
    private int ID_Staff;
}
