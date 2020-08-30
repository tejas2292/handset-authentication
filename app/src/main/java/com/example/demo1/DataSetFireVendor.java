package com.example.demo1;

public class DataSetFireVendor {
    String userNo;
    String ownerFirstName;
    String ownerLastName;
    String contact;
    String points;
    String shopeName;
    String gst_No;
    String activity;
    String userName;

    public DataSetFireVendor() {
    }

    public DataSetFireVendor(String userNo, String ownerFirstName, String ownerLastName, String contact,
                             String points, String shopeName, String gst_No, String activity, String userName) {
        this.userNo = userNo;
        this.ownerFirstName = ownerFirstName;
        this.ownerLastName = ownerLastName;
        this.contact = contact;
        this.points = points;
        this.shopeName = shopeName;
        this.gst_No = gst_No;
        this.activity = activity;
        this.userName = userName;
    }

    public String getUserNo() {
        return userNo;
    }

    public void setUserNo(String userNo) {
        this.userNo = userNo;
    }

    public String getOwnerFirstName() {
        return ownerFirstName;
    }

    public void setOwnerFirstName(String ownerFirstName) {
        this.ownerFirstName = ownerFirstName;
    }

    public String getOwnerLastName() {
        return ownerLastName;
    }

    public void setOwnerLastName(String ownerLastName) {
        this.ownerLastName = ownerLastName;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getShopeName() {
        return shopeName;
    }

    public void setShopeName(String shopeName) {
        this.shopeName = shopeName;
    }

    public String getGst_No() {
        return gst_No;
    }

    public void setGst_No(String gst_No) {
        this.gst_No = gst_No;
    }

    public String getActivity() {
        return activity;
    }

    public void setActivity(String activity) {
        this.activity = activity;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }
}
