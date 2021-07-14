package com.example.blooddonation;

public class User {

    public  String userName,userEmail,userPhone,userDob,userGender,userBlood, userStatus , userImage, userDonationDate;

    public User(){

    }

    public User(String userName, String userEmail, String userPhone, String userDob, String userGender, String userBlood, String userStatus, String userImage, String userDonationDate) {
        this.userName = userName;
        this.userEmail = userEmail;
        this.userPhone = userPhone;
        this.userDob = userDob;
        this.userGender = userGender;
        this.userBlood = userBlood;
        this.userStatus = userStatus;
        this.userImage = userImage;
        this.userDonationDate = userDonationDate;
    }
}
