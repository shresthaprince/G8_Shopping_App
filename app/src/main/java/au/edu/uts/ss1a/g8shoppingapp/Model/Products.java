package au.edu.uts.ss1a.g8shoppingapp.Model;

public class Products {
    private String name, description, price, prodID, category, date, image, time, prodName, prodDesc, prodPrice, prodStock;

    public Products() {

    }

    public Products(String name, String description, String price, String prodID, String category, String date, String image, String time, String prodName, String prodDesc, String prodPrice, String prodStock) {
        this.name = name;
        this.description = description;
        this.price = price;
        this.prodID = prodID;
        this.category = category;
        this.date = date;
        this.image = image;
        this.time = time;
        this.prodName = prodName;
        this.prodDesc = prodDesc;
        this.prodPrice = prodPrice;
        this.prodStock = prodStock;

    }

    public String getProdName() {
        return prodName;
    }

    public void setProdName(String prodName) {
        this.prodName = prodName;
    }

    public String getProdDesc() {
        return prodDesc;
    }

    public void setProdDesc(String prodDesc) {
        this.prodDesc = prodDesc;
    }

    public String getProdPrice() {
        return prodPrice;
    }

    public void setProdPrice(String prodPrice) {
        this.prodPrice = prodPrice;
    }

    public String getProdStock() {
        return prodStock;
    }

    public void setProdStock(String prodStock) {
        this.prodStock = prodStock;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getProdID() {
        return prodID;
    }

    public void setProdID(String prodID) {
        this.prodID = prodID;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
