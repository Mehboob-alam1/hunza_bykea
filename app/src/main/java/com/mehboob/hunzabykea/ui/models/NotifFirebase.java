package com.mehboob.hunzabykea.ui.models;

public class NotifFirebase {

    private String from;
    private String title;
    private String message;
    private String time;
    private String pushId;


    public NotifFirebase(String from, String title, String message, String time, String pushId) {
        this.from = from;
        this.title = title;
        this.message = message;
        this.time = time;
        this.pushId = pushId;
    }

    public NotifFirebase() {
    }


    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPushId() {
        return pushId;
    }

    public void setPushId(String pushId) {
        this.pushId = pushId;
    }
}
