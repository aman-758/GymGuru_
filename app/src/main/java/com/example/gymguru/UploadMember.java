package com.example.gymguru;

public class UploadMember {

    public long duration;
    public  String uploaderId;
    public  String date;
    public String title;
    public String url;

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getUploaderId() {
        return uploaderId;
    }

    public void setUploaderId(String uploaderId) {
        this.uploaderId = uploaderId;
    }



    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }




    public UploadMember(String name, String videoUri, String uid, String strDate, long duration){
        if(name.trim().equals("")){
            name = "not available";
        }
        title = name;
        url = videoUri;
        this.uploaderId = uid;
        this.date = strDate;
        this.duration = duration;

    }


    //Empty Constructor
    public UploadMember() { }


}
