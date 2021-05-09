package com.example.gymguru;

public class RegistrationModel {
    public String uid, username, age, email, experience, gender, userType;

    public RegistrationModel() { }

    public RegistrationModel(String uid, String username, String age, String email, String experience, String gender,String userType) {
        this.uid = uid;
        this.username = username;
        this.age = age;
        this.email = email;
        this.experience = experience;
        this.gender = gender;
        this.userType = userType;
    }
}
