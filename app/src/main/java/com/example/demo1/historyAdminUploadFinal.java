package com.example.demo1;

public class historyAdminUploadFinal {
    String srNo, date, name, loginId, sentPoints, diductPoints, remainPointsToUser, status, shopName,transactionId;

    public historyAdminUploadFinal() {
    }

    public historyAdminUploadFinal(String srNo, String date, String name, String loginId,
                                   String sentPoints, String diductPoints, String remainPointsToUser,
                                   String status, String shopName, String transactionId) {
        this.srNo = srNo;
        this.date = date;
        this.name = name;
        this.loginId = loginId;
        this.sentPoints = sentPoints;
        this.diductPoints = diductPoints;
        this.remainPointsToUser = remainPointsToUser;
        this.status = status;
        this.shopName = shopName;
        this.transactionId = transactionId;
    }

    public String getSrNo() {
        return srNo;
    }

    public void setSrNo(String srNo) {
        this.srNo = srNo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLoginId() {
        return loginId;
    }

    public void setLoginId(String loginId) {
        this.loginId = loginId;
    }

    public String getSentPoints() {
        return sentPoints;
    }

    public void setSentPoints(String sentPoints) {
        this.sentPoints = sentPoints;
    }

    public String getDiductPoints() {
        return diductPoints;
    }

    public void setDiductPoints(String diductPoints) {
        this.diductPoints = diductPoints;
    }

    public String getRemainPointsToUser() {
        return remainPointsToUser;
    }

    public void setRemainPointsToUser(String remainPointsToUser) {
        this.remainPointsToUser = remainPointsToUser;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getShopName() {
        return shopName;
    }

    public void setShopName(String shopName) {
        this.shopName = shopName;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
