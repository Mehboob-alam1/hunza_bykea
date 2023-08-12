package com.mehboob.hunzabykea.ui.models;

public class ActiveRides {


    private String vehicleType,vehicleBrand,vehicleModel,vehicleColor;

    private String driverUserId;
    private String driverName;
    private String driverAddress;
    private String driverPhoneNumber;
    private String riderName,riderEmail,riderPhone;


    private String riderVehicle;
    private String fare;
    private String nearBy;

    private String totalDistance;
    private String currentTime;
    private String paymentMethod;
    private String userOriginLatitude;
    private String userOriginLongitude;
    private String userDestLatitude;
    private String userDestLongitude;

    private String userId;
    private boolean status;
    private String driverImage;


    public ActiveRides() {
    }

    public ActiveRides(String vehicleType, String vehicleBrand, String vehicleModel, String vehicleColor, String driverUserId, String driverName, String driverAddress, String driverPhoneNumber, String riderName, String riderEmail, String riderPhone, String riderVehicle, String fare, String nearBy, String totalDistance, String currentTime, String paymentMethod, String userOriginLatitude, String userOriginLongitude, String userDestLatitude, String userDestLongitude, String userId, boolean status, String driverImage) {
        this.vehicleType = vehicleType;
        this.vehicleBrand = vehicleBrand;
        this.vehicleModel = vehicleModel;
        this.vehicleColor = vehicleColor;
        this.driverUserId = driverUserId;
        this.driverName = driverName;
        this.driverAddress = driverAddress;
        this.driverPhoneNumber = driverPhoneNumber;
        this.riderName = riderName;
        this.riderEmail = riderEmail;
        this.riderPhone = riderPhone;
        this.riderVehicle = riderVehicle;
        this.fare = fare;
        this.nearBy = nearBy;
        this.totalDistance = totalDistance;
        this.currentTime = currentTime;
        this.paymentMethod = paymentMethod;
        this.userOriginLatitude = userOriginLatitude;
        this.userOriginLongitude = userOriginLongitude;
        this.userDestLatitude = userDestLatitude;
        this.userDestLongitude = userDestLongitude;
        this.userId = userId;
        this.status = status;
        this.driverImage = driverImage;
    }

    public String getVehicleType() {
        return vehicleType;
    }



    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
    }

    public String getDriverImage() {
        return driverImage;
    }

    public void setDriverImage(String driverImage) {
        this.driverImage = driverImage;
    }

    public String getVehicleBrand() {
        return vehicleBrand;
    }

    public void setVehicleBrand(String vehicleBrand) {
        this.vehicleBrand = vehicleBrand;
    }

    public String getVehicleModel() {
        return vehicleModel;
    }

    public void setVehicleModel(String vehicleModel) {
        this.vehicleModel = vehicleModel;
    }

    public String getVehicleColor() {
        return vehicleColor;
    }

    public void setVehicleColor(String vehicleColor) {
        this.vehicleColor = vehicleColor;
    }

    public String getDriverUserId() {
        return driverUserId;
    }

    public void setDriverUserId(String driverUserId) {
        this.driverUserId = driverUserId;
    }

    public String getDriverName() {
        return driverName;
    }

    public void setDriverName(String driverName) {
        this.driverName = driverName;
    }

    public String getDriverAddress() {
        return driverAddress;
    }

    public void setDriverAddress(String driverAddress) {
        this.driverAddress = driverAddress;
    }

    public String getDriverPhoneNumber() {
        return driverPhoneNumber;
    }

    public void setDriverPhoneNumber(String driverPhoneNumber) {
        this.driverPhoneNumber = driverPhoneNumber;
    }

    public String getRiderName() {
        return riderName;
    }

    public void setRiderName(String riderName) {
        this.riderName = riderName;
    }

    public String getRiderEmail() {
        return riderEmail;
    }

    public void setRiderEmail(String riderEmail) {
        this.riderEmail = riderEmail;
    }

    public String getRiderPhone() {
        return riderPhone;
    }

    public void setRiderPhone(String riderPhone) {
        this.riderPhone = riderPhone;
    }

    public String getRiderVehicle() {
        return riderVehicle;
    }

    public void setRiderVehicle(String riderVehicle) {
        this.riderVehicle = riderVehicle;
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

    public String getTotalDistance() {
        return totalDistance;
    }

    public void setTotalDistance(String totalDistance) {
        this.totalDistance = totalDistance;
    }

    public String getCurrentTime() {
        return currentTime;
    }

    public void setCurrentTime(String currentTime) {
        this.currentTime = currentTime;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public String getUserOriginLatitude() {
        return userOriginLatitude;
    }

    public void setUserOriginLatitude(String userOriginLatitude) {
        this.userOriginLatitude = userOriginLatitude;
    }

    public String getUserOriginLongitude() {
        return userOriginLongitude;
    }

    public void setUserOriginLongitude(String userOriginLongitude) {
        this.userOriginLongitude = userOriginLongitude;
    }

    public String getUserDestLatitude() {
        return userDestLatitude;
    }

    public void setUserDestLatitude(String userDestLatitude) {
        this.userDestLatitude = userDestLatitude;
    }

    public String getUserDestLongitude() {
        return userDestLongitude;
    }

    public void setUserDestLongitude(String userDestLongitude) {
        this.userDestLongitude = userDestLongitude;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }
}
