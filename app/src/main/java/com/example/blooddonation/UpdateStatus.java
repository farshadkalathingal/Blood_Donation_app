package com.example.blooddonation;

public class UpdateStatus {
    public  String userName;
    public String userEmail;
    public String userPhone;
    public String userDob;
    public String userGender;
    public String userBlood;
    public String userStatus;
    public String userImage;
    public String userDonationDate;

    public UpdateStatus() {
    }

    public UpdateStatus(String userName, String userEmail, String userPhone, String userDob, String userGender, String userBlood, String userStatus, String userImage, String userDonationDate) {
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
