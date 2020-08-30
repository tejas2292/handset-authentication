package com.example.demo1;

public class HistoryAdminUploadVendor {
    String transferPoints, vendorId, vendorName, vendorShopName, date, time, status;

    public HistoryAdminUploadVendor() {
    }

    public HistoryAdminUploadVendor(String transferPoints, String vendorId, String vendorName,
                                    String vendorShopName, String date, String time, String status) {
        this.transferPoints = transferPoints;
        this.vendorId = vendorId;
        this.vendorName = vendorName;
        this.vendorShopName = vendorShopName;
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

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getVendorShopName() {
        return vendorShopName;
    }

    public void setVendorShopName(String vendorShopName) {
        this.vendorShopName = vendorShopName;
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
