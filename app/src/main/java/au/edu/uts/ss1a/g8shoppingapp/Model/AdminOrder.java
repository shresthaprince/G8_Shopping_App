package au.edu.uts.ss1a.g8shoppingapp.Model;

public class AdminOrder {
    private String TotalAmount, userName, userPhonenumber, userAddressStreet, userAddressCity, userAddressPostcode, date, time, tracker;

    public AdminOrder() {
    }

    public AdminOrder(String totalAmount, String userName, String userPhonenumber, String userAddressStreet, String userAddressCity, String userAddressPostcode, String date, String time, String tracker) {
        TotalAmount = totalAmount;
        this.userName = userName;
        this.userPhonenumber = userPhonenumber;
        this.userAddressStreet = userAddressStreet;
        this.userAddressCity = userAddressCity;
        this.userAddressPostcode = userAddressPostcode;
        this.date = date;
        this.time = time;
        this.tracker = tracker;
    }

    public String getTotalAmount() {
        return TotalAmount;
    }

    public void setTotalAmount(String totalAmount) {
        TotalAmount = totalAmount;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhonenumber() {
        return userPhonenumber;
    }

    public void setUserPhonenumber(String userPhonenumber) {
        this.userPhonenumber = userPhonenumber;
    }

    public String getUserAddressStreet() {
        return userAddressStreet;
    }

    public void setUserAddressStreet(String userAddressStreet) {
        this.userAddressStreet = userAddressStreet;
    }

    public String getUserAddressCity() {
        return userAddressCity;
    }

    public void setUserAddressCity(String userAddressCity) {
        this.userAddressCity = userAddressCity;
    }

    public String getUserAddressPostcode() {
        return userAddressPostcode;
    }

    public void setUserAddressPostcode(String userAddressPostcode) {
        this.userAddressPostcode = userAddressPostcode;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getTracker() {
        return tracker;
    }

    public void setTracker(String tracker) {
        this.tracker = tracker;
    }
}
