package au.edu.uts.ss1a.g8shoppingapp.Model;

public class Customers {
    private String name, password, phonenumber;

    public Customers(){

    }

    public Customers(String name, String password, String phonenumber) {
        this.name = name;
        this.password = password;
        this.phonenumber = phonenumber;

    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }
}
