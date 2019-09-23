package au.edu.uts.ss1a.g8shoppingapp.Model;

public class Customers {
    private String name, password, phonenumber, image;

    public Customers(){

    }

    public Customers(String name, String password, String phonenumber, String image) {
        this.name = name;
        this.password = password;
        this.phonenumber = phonenumber;
        this.image = image;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
