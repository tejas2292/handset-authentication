package com.example.demo1;

class HistoryVendorUpload {
    String serialNo, date, invoice, transactionId;
    public HistoryVendorUpload(){

    }
    public HistoryVendorUpload(String serialNo, String date, String invoice, String transactionId) {
        this.serialNo = serialNo;
        this.date = date;
        this.invoice = invoice;
        this.transactionId = transactionId;
    }

    public String getSerialNo() {
        return serialNo;
    }

    public void setSerialNo(String serialNo) {
        this.serialNo = serialNo;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getInvoice() {
        return invoice;
    }

    public void setInvoice(String invoice) {
        this.invoice = invoice;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }
}
