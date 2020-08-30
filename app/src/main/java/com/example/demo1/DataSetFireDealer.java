package com.example.demo1;

public class DataSetFireDealer {
    String userNo;
    String ownerFirstName;
    String ownerLastName;
    String contact;
    String points;
    String activity;
    String userName;

    public DataSetFireDealer() {
    }

    public DataSetFireDealer(String userNo, String ownerFirstName, String ownerLastName, String contact,
                             String points, String activity, String userName) {
        this.userNo = userNo;
        this.ownerFirstName = ownerFirstName;
        this.ownerLastName = ownerLastName;
        this.contact = contact;
        this.points = points;
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
