package com.example.blooddonation;

public class Donor {

    private String userName,userGender,userPhone,userImage;

    public Donor() {
    }

    public Donor(String userName, String userGender, String userPhone, String userImage) {
        this.userName = userName;
        this.userGender = userGender;
        this.userPhone = userPhone;
        this.userImage = userImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserGender() {
        return userGender;
    }

    public void setUserGender(String userGender) {
        this.userGender = userGender;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }
}
