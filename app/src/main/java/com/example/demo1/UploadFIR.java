package com.example.demo1;

public class UploadFIR {
    String FIRNo, PoliceStationAddress;

    public UploadFIR() {
    }

    public UploadFIR(String FIRNo, String policeStationAddress) {
        this.FIRNo = FIRNo;
        PoliceStationAddress = policeStationAddress;
    }

    public String getFIRNo() {
        return FIRNo;
    }

    public void setFIRNo(String FIRNo) {
        this.FIRNo = FIRNo;
    }

    public String getPoliceStationAddress() {
        return PoliceStationAddress;
    }

    public void setPoliceStationAddress(String policeStationAddress) {
        PoliceStationAddress = policeStationAddress;
    }
}
