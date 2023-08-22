package com.mehboob.hunzabykea.ui.models;

public class CompletedRides {

   private ActiveRides rides;
    private String pushId;


    public CompletedRides() {
    }

    public CompletedRides(ActiveRides rides, String pushId) {
        this.rides = rides;
        this.pushId = pushId;
    }

    public ActiveRides getRides() {
        return rides;
    }

    public void setRides(ActiveRides rides) {
        this.rides = rides;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }
}
