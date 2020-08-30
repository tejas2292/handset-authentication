package com.example.demo1;

public class DataSetFirePersonal {
    String userNo;
    String ownerFirstName;
    String ownerLastName;
    String contact;
    String activity;
    String userName;

    public DataSetFirePersonal() {
    }

    public DataSetFirePersonal(String userNo, String ownerFirstName, String ownerLastName,
                               String contact, String activity, String userName) {
        this.userNo = userNo;
        this.ownerFirstName = ownerFirstName;
        this.ownerLastName = ownerLastName;
        this.contact = contact;
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
