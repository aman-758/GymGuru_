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

    public RegistrationModel() { }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getChannelName() {
        return channelName;
    }

    public void setChannelName(String channelName) {
        this.channelName = channelName;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public RegistrationModel(String uid, String username, String age, String email, String experience, String channelName, String gender, String userType) {
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
