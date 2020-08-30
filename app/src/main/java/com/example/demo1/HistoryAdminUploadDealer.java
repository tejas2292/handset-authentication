package com.example.demo1;

public class HistoryAdminUploadDealer {
    String transferPoints, dealerId, dealerName, date, time, status;

    public HistoryAdminUploadDealer() {
    }

    public HistoryAdminUploadDealer(String transferPoints, String dealerId, String dealerName,
                                    String date, String time, String status) {
        this.transferPoints = transferPoints;
        this.dealerId = dealerId;
        this.dealerName = dealerName;
        this.date = date;
        this.time = time;
        this.status = status;
    }

    public String getTransferPoints() {
        return transferPoints;
    }

    public void setTransferPoints(String transferPoints) {
        this.transferPoints = transferPoints;
    }

    public String getDealerId() {
        return dealerId;
    }

    public void setDealerId(String dealerId) {
        this.dealerId = dealerId;
    }

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
