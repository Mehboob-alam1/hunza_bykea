package com.mehboob.hunzabykea.ui.models;

public class OrderPlace {

    private String userId;
    private String latitude;
    private String longitude;
    private String vehicle;
    private String fare;
    private String nearBy;
    private String paymentMethod;
    private String pushId;
    private String time;

    public OrderPlace(String userId, String latitude, String longitude, String vehicle, String fare, String nearBy, String paymentMethod, String pushId, String time) {
        this.userId = userId;
        this.latitude = latitude;
        this.longitude = longitude;
        this.vehicle = vehicle;
        this.fare = fare;
        this.nearBy = nearBy;
        this.paymentMethod = paymentMethod;
        this.pushId = pushId;
        this.time = time;
    }

    public OrderPlace() {
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
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

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }
}
