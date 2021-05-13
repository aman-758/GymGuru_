package com.example.gymguru;

public class FollowModel {
    public String UserId,UploaderId;

    public FollowModel(String uploaderId) { }

    public FollowModel(String UserId, String UploaderId) {


        this.UserId = UserId;
        this.UploaderId = UploaderId;
    }
}
