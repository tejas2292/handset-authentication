package com.example.demo1;

public class VendorUpload {
    String ShopeName, OwnerFirstName, OwnerLastName, OwnerAddress, PinCode, Gst_No, Email,
            Recovery_Email, Password, Contact, UserNo, LoginID, Points, UserName, Activity;

    public VendorUpload() {

    }

    public VendorUpload(String shopeName, String ownerFirstName, String ownerLastName, String ownerAddress,
                        String pinCode, String gst_No, String email, String recovery_Email, String password,
                        String contact, String userNo, String loginID, String points, String userName, String activity) {
        ShopeName = shopeName;
        OwnerFirstName = ownerFirstName;
        OwnerLastName = ownerLastName;
        OwnerAddress = ownerAddress;
        PinCode = pinCode;
        Gst_No = gst_No;
        Email = email;
        Recovery_Email = recovery_Email;
        Password = password;
        Contact = contact;
        UserNo = userNo;
        LoginID = loginID;
        Points = points;
        UserName = userName;
        Activity = activity;
    }

    public String getShopeName() {
        return ShopeName;
    }

    public void setShopeName(String shopeName) {
        ShopeName = shopeName;
    }

    public String getOwnerFirstName() {
        return OwnerFirstName;
    }

    public void setOwnerFirstName(String ownerFirstName) {
        OwnerFirstName = ownerFirstName;
    }

    public String getOwnerLastName() {
        return OwnerLastName;
    }

    public void setOwnerLastName(String ownerLastName) {
        OwnerLastName = ownerLastName;
    }

    public String getOwnerAddress() {
        return OwnerAddress;
    }

    public void setOwnerAddress(String ownerAddress) {
        OwnerAddress = ownerAddress;
    }

    public String getPinCode() {
        return PinCode;
    }

    public void setPinCode(String pinCode) {
        PinCode = pinCode;
    }

    public String getGst_No() {
        return Gst_No;
    }

    public void setGst_No(String gst_No) {
        Gst_No = gst_No;
    }

    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getRecovery_Email() {
        return Recovery_Email;
    }

    public void setRecovery_Email(String recovery_Email) {
        Recovery_Email = recovery_Email;
    }

    public String getPassword() {
        return Password;
    }

    public void setPassword(String password) {
        Password = password;
    }

    public String getContact() {
        return Contact;
    }

    public void setContact(String contact) {
        Contact = contact;
    }

    public String getUserNo() {
        return UserNo;
    }

    public void setUserNo(String userNo) {
        UserNo = userNo;
    }

    public String getLoginID() {
        return LoginID;
    }

    public void setLoginID(String loginID) {
        LoginID = loginID;
    }

    public String getPoints() {
        return Points;
    }

    public void setPoints(String points) {
        Points = points;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getActivity() {
        return Activity;
    }

    public void setActivity(String activity) {
        Activity = activity;
    }
}
