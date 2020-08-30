package com.example.demo1;

public class RegisterNewSerialUpload {
    String invoiceNo, buyerFirstName,buyerLastName,contactFirst, contactSecond, brandName, modelNo,
            warrantyDate, modelSerial, url1, url2, vendorLoginID, vendorShopName, vendorContact,
            vendorAddress, email, recoveryEmail, date;

    public RegisterNewSerialUpload(){

    }

    public RegisterNewSerialUpload(String invoiceNo, String buyerFirstName,String buyerLastName,
                                   String contactFirst, String contactSecond, String brandName, String modelNo,
                                   String warrantyDate, String modelSerial, String url1,String url2,
                                   String vendorLoginID, String vendorShopName, String vendorContact,
                                   String vendorAddress, String email , String recoveryEmail, String date) {
        this.invoiceNo = invoiceNo;
        this.buyerFirstName = buyerFirstName;
        this.buyerLastName = buyerLastName;
        this.contactFirst = contactFirst;
        this.contactSecond = contactSecond;
        this.brandName = brandName;
        this.modelNo = modelNo;
        this.warrantyDate = warrantyDate;
        this.modelSerial = modelSerial;
        this.url1 = url1;
        this.url2 = url2;
        this.vendorLoginID = vendorLoginID;
        this.vendorShopName = vendorShopName;
        this.vendorContact = vendorContact;
        this.vendorAddress = vendorAddress;
        this.email = email;
        this.recoveryEmail = recoveryEmail;
        this.date = date;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getBuyerFirstName() {
        return buyerFirstName;
    }

    public void setBuyerFirstName(String buyerFirstName) {
        this.buyerFirstName = buyerFirstName;
    }

    public String getBuyerLastName() {
        return buyerLastName;
    }

    public void setBuyerLastName(String buyerLastName) {
        this.buyerLastName = buyerLastName;
    }

    public String getContactFirst() {
        return contactFirst;
    }

    public void setContactFirst(String contactFirst) {
        this.contactFirst = contactFirst;
    }

    public String getContactSecond() {
        return contactSecond;
    }

    public void setContactSecond(String contactSecond) {
        this.contactSecond = contactSecond;
    }

    public String getBrandName() {
        return brandName;
    }

    public void setBrandName(String brandName) {
        this.brandName = brandName;
    }

    public String getModelNo() {
        return modelNo;
    }

    public void setModelNo(String modelNo) {
        this.modelNo = modelNo;
    }

    public String getWarrantyDate() {
        return warrantyDate;
    }

    public void setWarrantyDate(String warrantyDate) {
        this.warrantyDate = warrantyDate;
    }

    public String getModelSerial() {
        return modelSerial;
    }

    public void setModelSerial(String modelSerial) {
        this.modelSerial = modelSerial;
    }

    public String getUrl1() {
        return url1;
    }

    public void setUrl1(String url1) {
        this.url1 = url1;
    }

    public String getUrl2() {
        return url2;
    }

    public void setUrl2(String url2) {
        this.url2 = url2;
    }

    public String getVendorLoginID() {
        return vendorLoginID;
    }

    public void setVendorLoginID(String vendorLoginID) {
        this.vendorLoginID = vendorLoginID;
    }

    public String getVendorShopName() {
        return vendorShopName;
    }

    public void setVendorShopName(String vendorShopName) {
        this.vendorShopName = vendorShopName;
    }

    public String getVendorContact() {
        return vendorContact;
    }

    public void setVendorContact(String vendorContact) {
        this.vendorContact = vendorContact;
    }

    public String getVendorAddress() {
        return vendorAddress;
    }

    public void setVendorAddress(String vendorAddress) {
        this.vendorAddress = vendorAddress;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getRecoveryEmail() {
        return recoveryEmail;
    }

    public void setRecoveryEmail(String recoveryEmail) {
        this.recoveryEmail = recoveryEmail;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String toString(){
        return this.modelSerial;
    }
}
