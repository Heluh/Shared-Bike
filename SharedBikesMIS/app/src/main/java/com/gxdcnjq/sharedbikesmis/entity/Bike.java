package com.gxdcnjq.sharedbikesmis.entity;

public class Bike {
    private int id;
    private String bikeNumber;
    private double longitude;

    private double latitude;
    private Integer status;

    public int getBikeId() {
        return id;
    }

    public void setBikeId(int id) {
        this.id = id;
    }

    public String getBikeNumber() {
        return bikeNumber;
    }

    public void setBikeNumber(String bikeNumber) {
        this.bikeNumber = bikeNumber;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
