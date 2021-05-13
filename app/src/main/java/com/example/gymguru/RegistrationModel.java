package com.example.gymguru;

public class RegistrationModel {
    public String uid, username, age, email, experience, channelName, gender, userType;

    public RegistrationModel() { }

    public RegistrationModel(String uid, String username, String age, String email, String experience, String channelName, String gender,String userType) {
        this.uid = uid;
        this.username = username;
        this.age = age;
        this.email = email;
        this.experience = experience;
        this.channelName = channelName;
        this.gender = gender;
        this.userType = userType;
    }
}
