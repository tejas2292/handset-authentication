package com.example.demo1;

public class Upload {
    String Password, Contact, LoginID, UserNo, UserName;

    public Upload(){

    }

    public Upload(String password, String contact, String loginID, String userNo, String userName) {
        Password = password;
        Contact = contact;
        LoginID = loginID;
        UserNo = userNo;
        UserName = userName;
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

    public String getLoginID() {
        return LoginID;
    }

    public void setLoginID(String loginID) {
        LoginID = loginID;
    }

    public String getUserNo() {
        return UserNo;
    }

    public void setUserNo(String userNo) {
        UserNo = userNo;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }
}
