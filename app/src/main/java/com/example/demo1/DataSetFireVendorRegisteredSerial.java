package com.example.demo1;

class DataSetFireVendorRegisteredSerial {
    String serialNo, date;

    public DataSetFireVendorRegisteredSerial() {
    }

    public DataSetFireVendorRegisteredSerial(String serialNo, String date) {
        this.serialNo = serialNo;
        this.date = date;
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
}
