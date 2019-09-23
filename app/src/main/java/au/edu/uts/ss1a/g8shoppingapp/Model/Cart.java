package au.edu.uts.ss1a.g8shoppingapp.Model;

public class Cart {
    private String prodID, prodName, prodPrice, prodQuantity, prodDesc;

    public String getProdID() {
        return prodID;
    }

    public void setProdID(String prodID) {
        this.prodID = prodID;
    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getProdPrice() {
        return prodPrice;
    }

    public void setProdPrice(String prodPrice) {
        this.prodPrice = prodPrice;
    }

    public String getProdQuantity() {
        return prodQuantity;
    }

    public void setProdQuantity(String prodQuantity) {
        this.prodQuantity = prodQuantity;
    }

    public String getProdDesc() {
        return prodDesc;
    }

    public void setProdDesc(String prodDesc) {
        this.prodDesc = prodDesc;
    }

    public Cart() {

    }

    public Cart(String prodID, String prodName, String prodPrice, String prodQuantity, String prodDesc) {
        this.prodID = prodID;
        this.prodName = prodName;
        this.prodPrice = prodPrice;
        this.prodQuantity = prodQuantity;
        this.prodDesc = prodDesc;
    }
}
