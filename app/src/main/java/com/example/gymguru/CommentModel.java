package com.example.gymguru;

public class CommentModel {
    String date, imageUrl, time, uid, usermsg, username;

    public CommentModel() {
    }

    public CommentModel(String date, String imageUrl, String time, String uid, String usermsg, String username) {
        this.date = date;
        this.imageUrl = imageUrl;
        this.time = time;
        this.uid = uid;
        this.usermsg = usermsg;
        this.username = username;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsermsg() {
        return usermsg;
    }

    public void setUsermsg(String usermsg) {
        this.usermsg = usermsg;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
