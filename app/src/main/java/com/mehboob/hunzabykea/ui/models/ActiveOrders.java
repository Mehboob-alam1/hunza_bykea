package com.mehboob.hunzabykea.ui.models;

public class ActiveOrders {
    private String userId;
    private String driverUserId;
    private String driverName;
    private String driverAddress;
    private String driverPhoneNumber;
   private String name,email,phone;
   private boolean status;

    public ActiveOrders() {
    }

    public ActiveOrders(String userId, String driverUserId, String driverName, String driverAddress, String driverPhoneNumber, String name, String email, String phone, boolean status) {
        this.userId = userId;
        this.driverUserId = driverUserId;
        this.driverName = driverName;
        this.driverAddress = driverAddress;
        this.driverPhoneNumber = driverPhoneNumber;
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.status = status;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
