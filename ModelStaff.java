package FlowerShop.Model;

public class ModelStaff {

    public int getID_Staff() {
        return ID_Staff;
    }

    public void setID_Staff(int ID_Staff) {
        this.ID_Staff = ID_Staff;
    }

    public String getNameStaff() {
        return NameStaff;
    }

    public void setNameStaff(String nameStaff) {
        this.NameStaff = nameStaff;
    }

    public String getJoinDate() {
        return JoinDate;
    }

    public void setDateJoin(String JoinDate) {
        this.JoinDate = JoinDate;
    }

    public String getPhoneNumber() {
        return PhoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.PhoneNumber = phoneNumber;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getID_Manager() {
        return ID_Manager;
    }

    public void setID_Manager(int ID_Manager) {
        this.ID_Manager = ID_Manager;
    }

    public ModelStaff() {
        this.ID_Staff = ID_Staff;
        this.NameStaff = NameStaff;
        this.JoinDate = JoinDate;
        this.PhoneNumber = PhoneNumber;
        this.role = role;
        this.ID_Manager = ID_Manager;
    }


    private int ID_Staff;
    private String NameStaff;
    private String JoinDate;
    private String PhoneNumber;
    private String role;
    private int ID_Manager;
}

