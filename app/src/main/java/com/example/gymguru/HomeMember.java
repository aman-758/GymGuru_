package com.example.gymguru;

public class HomeMember {
    String title,url;

    public HomeMember(){ }

    public String getTitle() {
        return title;
    }

    public HomeMember(String title, String url) {
        this.title = title;
        this.url = url;
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
