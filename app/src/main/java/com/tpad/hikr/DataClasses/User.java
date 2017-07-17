package com.tpad.hikr.DataClasses;

/**
 * Created by oguns on 6/25/2017.
 */

public class User {
    static String userName, email, uid, photoUrl, phoneNumber;

    public static String getUserName() {
        return userName;
    }

    public static void setUserName(String userName) {
        User.userName = userName;
    }

    public static String getEmail() {
        return email;
    }

    public static void setEmail(String email) {
        User.email = email;
    }

    public static String getUid() {
        return uid;
    }

    public static void setUid(String uid) {
        User.uid = uid;
    }

    public static String getPhotoUrl() {
        return photoUrl;
    }

    public static void setPhotoUrl(String photoUrl) {
        User.photoUrl = photoUrl;
    }

    public static String getPhoneNumber() {
        return phoneNumber;
    }

    public static void setPhoneNumber(String phoneNumber) {
        User.phoneNumber = phoneNumber;
    }
}
