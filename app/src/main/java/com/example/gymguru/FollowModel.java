package com.example.gymguru;

public class FollowModel {
    public String ViewerId,UploaderId;

    public FollowModel(String uploaderId) { }

    public FollowModel(String ViewerId, String UploaderId) {


        this.ViewerId = ViewerId;
        this.UploaderId = UploaderId;
    }
}
