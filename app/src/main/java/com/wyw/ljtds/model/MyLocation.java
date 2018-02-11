package com.wyw.ljtds.model;

/**
 * Created by wsy on 18-1-25.
 */

public class MyLocation {
    double latitude;
    double Longitude;
    String addrStr;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    public String getAddrStr() {
        return addrStr;
    }

    public void setAddrStr(String addrStr) {
        this.addrStr = addrStr;
    }

    public static MyLocation newInstance(double latitude, double longitude, String addrStr) {
        MyLocation location = new MyLocation();
        location.setLatitude(latitude);
        location.setLongitude(longitude);
        location.setAddrStr(addrStr);
        return location;
    }
}
