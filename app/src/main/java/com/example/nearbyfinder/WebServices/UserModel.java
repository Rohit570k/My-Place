package com.example.nearbyfinder.WebServices;

public class UserModel {
    String email;
    String username;
    String phonenumber;
    boolean isNotificationEnable;

    public UserModel() {
    }


    public UserModel(String email, String username, String phonenumber,boolean isNotificationEnable) {
        this.email = email;
        this.username = username;
        this.phonenumber = phonenumber;
//        this.image = image;
        this.isNotificationEnable = isNotificationEnable;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

//    public String getImage() {
//        return image;
//    }
//
//    public void setImage(String image) {
//        this.image = image;
//    }

    public boolean isNotificationEnable() {
        return isNotificationEnable;
    }

    public void setNotificationEnable(boolean notificationEnable) {
        isNotificationEnable = notificationEnable;
    }
}
