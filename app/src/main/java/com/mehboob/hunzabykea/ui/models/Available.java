package com.mehboob.hunzabykea.ui.models;

public class Available {


    private boolean isAvailable;
    private String userId;


    public Available() {
    }

    public Available(boolean isAvailable, String userId) {
        this.isAvailable = isAvailable;
        this.userId = userId;
    }

    public boolean isAvailable() {
        return isAvailable;
    }

    public void setAvailable(boolean available) {
        isAvailable = available;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
