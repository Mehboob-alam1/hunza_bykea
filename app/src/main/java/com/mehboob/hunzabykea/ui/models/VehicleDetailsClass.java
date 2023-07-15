package com.mehboob.hunzabykea.ui.models;

public class VehicleDetailsClass {
   private String vehicleType,vehicleBrand,vehicleModel,vehicleColor;
    private String userId;

    public VehicleDetailsClass(String vehicleType, String vehicleBrand, String vehicleModel, String vehicleColor, String userId) {
        this.vehicleType = vehicleType;
        this.vehicleBrand = vehicleBrand;
        this.vehicleModel = vehicleModel;
        this.vehicleColor = vehicleColor;
        this.userId = userId;
    }

    public VehicleDetailsClass() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVehicleType() {
        return vehicleType;
    }

    public void setVehicleType(String vehicleType) {
        this.vehicleType = vehicleType;
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
}
