package com.google.isence;

public class VehicleLocation {
    double Latitude;
    double Longitude;
    String regNo;

    public VehicleLocation(){

    }

    public VehicleLocation(double latitude, double longitude, String regNo) {
        Latitude = latitude;
        Longitude = longitude;
        this.regNo = regNo;
    }

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public String getRegNo() {
        return regNo;
    }

    public void setRegNo(String regNo) {
        this.regNo = regNo;
    }
}
