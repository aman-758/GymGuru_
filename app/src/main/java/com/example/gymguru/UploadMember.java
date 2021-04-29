package com.example.gymguru;

public class UploadMember {

    private String title;
    private String url;

    //Empty Constructor

    private UploadMember(){ }

    public UploadMember(String name, String videoUri){
        if(name.trim().equals("")){
            name = "not available";
        }
        title = name;
        url = videoUri;
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
}
