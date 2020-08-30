package com.example.demo1;

public class historyDealerUploadFinal {
    String srNo, date, vendorName, vendorId, receivedPoints, deductedPoints, status, vendorShopName, transactionId;

    public historyDealerUploadFinal() {
    }

    public historyDealerUploadFinal(String srNo, String date, String vendorName, String vendorId,
                                    String receivedPoints, String deductedPoints, String status,
                                    String vendorShopName, String transactionId) {
        this.srNo = srNo;
        this.date = date;
        this.vendorName = vendorName;
        this.vendorId = vendorId;
        this.receivedPoints = receivedPoints;
        this.deductedPoints = deductedPoints;
        this.status = status;
        this.vendorShopName = vendorShopName;
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

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getVendorId() {
        return vendorId;
    }

    public void setVendorId(String vendorId) {
        this.vendorId = vendorId;
    }

    public String getReceivedPoints() {
        return receivedPoints;
    }

    public void setReceivedPoints(String receivedPoints) {
        this.receivedPoints = receivedPoints;
    }

    public String getDeductedPoints() {
        return deductedPoints;
    }

    public void setDeductedPoints(String deductedPoints) {
        this.deductedPoints = deductedPoints;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getVendorShopName() {
        return vendorShopName;
    }

    public void setVendorShopName(String vendorShopName) {
        this.vendorShopName = vendorShopName;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
