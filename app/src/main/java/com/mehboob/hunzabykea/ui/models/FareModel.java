package com.mehboob.hunzabykea.ui.models;

public class FareModel {

    private String vehicle;
    private String fare;
    private String nearBy;

    public FareModel(String vehicle, String fare, String nearBy) {
        this.vehicle = vehicle;
        this.fare = fare;
        this.nearBy = nearBy;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }

    public String getFare() {
        return fare;
    }

    public void setFare(String fare) {
        this.fare = fare;
    }

    public String getNearBy() {
        return nearBy;
    }

    public void setNearBy(String nearBy) {
        this.nearBy = nearBy;
    }
}
