package com.example.gymguru;

public class RegistrationModel {
    public String uid;
    public String username;
    public String age;
    public String email;
    public String experience;
    public String channelName;
    public String gender;
    public String userType;
    public String imageUrl;

    public RegistrationModel() { }

    public String getUid() {
        return uid;
    }

    public String getUsername() {
        return username;
    }

    public String getAge() {
        return age;
    }

    public String getEmail() {
        return email;
    }

    public String getExperience() {
        return experience;
    }

    public String getChannelName() {
        return channelName;
    }

    public String getGender() {
        return gender;
    }

    public String getUserType() {
        return userType;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public RegistrationModel(String uid, String username, String age, String email, String experience, String channelName, String gender, String userType, String imageUrl) {
        this.uid = uid;
        this.username = username;
        this.age = age;
        this.email = email;
        this.experience = experience;
        this.channelName = channelName;
        this.gender = gender;
        this.userType = userType;
        this.imageUrl = imageUrl;
    }
}
