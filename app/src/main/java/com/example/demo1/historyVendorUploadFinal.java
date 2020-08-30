package com.example.demo1;

public class historyVendorUploadFinal {
    String srNo, date, dealerName, dealerId, invoice, receivedPoints, deductedPoints, status, transactionId;

    public historyVendorUploadFinal() {
    }

    public historyVendorUploadFinal(String srNo, String date, String dealerName, String dealerId,
                                    String invoice, String receivedPoints, String deductedPoints,
                                    String status, String transactionId) {
        this.srNo = srNo;
        this.date = date;
        this.dealerName = dealerName;
        this.dealerId = dealerId;
        this.invoice = invoice;
        this.receivedPoints = receivedPoints;
        this.deductedPoints = deductedPoints;
        this.status = status;
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

    public String getDealerName() {
        return dealerName;
    }

    public void setDealerName(String dealerName) {
        this.dealerName = dealerName;
    }

    public String getDealerId() {
        return dealerId;
    }

    public void setDealerId(String dealerId) {
        this.dealerId = dealerId;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
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

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
