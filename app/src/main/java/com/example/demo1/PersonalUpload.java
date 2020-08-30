package com.example.demo1;

public class PersonalUpload {
    String OwnerFirstName,OwnerLastName, Email, Recovery_Email, Password, Contact, UserNo, LoginID,
            UserName, Points, Activity;

    public PersonalUpload(){

    }

    public PersonalUpload(String ownerFirstName, String ownerLastName, String email, String recovery_Email, String password,
                          String contact, String userNo, String loginID, String userName, String points, String activity) {
        OwnerFirstName = ownerFirstName;
        OwnerLastName = ownerLastName;
        Email = email;
        Recovery_Email = recovery_Email;
        Password = password;
        Contact = contact;
        UserNo = userNo;
        LoginID = loginID;
        UserName = userName;
        Points = points;
        Activity = activity;
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

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getPoints() {
        return Points;
    }

    public void setPoints(String points) {
        Points = points;
    }

    public String getActivity() {
        return Activity;
    }

    public void setActivity(String activity) {
        Activity = activity;
    }
}
