package com.tpad.hikr.DataClasses;

/**
 * Created by oguns on 6/30/2017.
 */

public class HikeLocationData {
    private String name, city, distance, address, imageUrl, phoneNumber, email;
    private double rating, latitude, longitude;

    public HikeLocationData() {
    }

    public HikeLocationData(String name, String city, String distance, String address, String imageUrl,
                            String phoneNumber, String email, double rating, double latitude, double longitude) {
        this.name = name;
        this.city = city;
        this.distance = distance;
        this.address = address;
        this.imageUrl = imageUrl;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.rating = rating;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public HikeLocationData(String name, String city, String distance) {
        this.name = name;
        this.city = city;
        this.distance = distance;
    }



    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }
}
