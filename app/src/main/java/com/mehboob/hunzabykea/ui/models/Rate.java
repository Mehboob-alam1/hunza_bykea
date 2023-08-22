package com.mehboob.hunzabykea.ui.models;

public class Rate {
    String rating;
    String userGivenRating;
    String userName;
    String pushId;

    public Rate() {
    }

    public Rate(String rating, String userGivenRating, String userName, String pushId) {
        this.rating = rating;
        this.userGivenRating = userGivenRating;
        this.userName = userName;
        this.pushId = pushId;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }

    public String getUserGivenRating() {
        return userGivenRating;
    }

    public void setUserGivenRating(String userGivenRating) {
        this.userGivenRating = userGivenRating;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }
}
