package com.tpad.hikr.DataClasses;

import android.graphics.Bitmap;
import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by oguns on 6/30/2017.
 */

public class HikeLocationData implements Parcelable {
    private String name, city, distance, address, phoneNumber, email;
    private double rating, latitude, longitude;
    private Bitmap image;

    public HikeLocationData() {
    }

    public HikeLocationData(String name, String city, String distance, String address, Bitmap image,
                            String phoneNumber, String email, double rating, double latitude, double longitude) {
        this.name = name;
        this.city = city;
        this.distance = distance;
        this.address = address;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.rating = rating;
        this.latitude = latitude;
        this.longitude = longitude;
        this.image = image;
    }

    public HikeLocationData(String name, String city, String distance) {
        this.name = name;
        this.city = city;
        this.distance = distance;
    }

    public HikeLocationData(Parcel in) {
        this.name = in.readString();
        this.city = in.readString();
        this.distance = in.readString();
        this.address = in.readString();
        this.phoneNumber = in.readString();
        this.email = in.readString();
        this.rating = in.readDouble();
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
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

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }

    public static final Parcelable.Creator<HikeLocationData> CREATOR = new Parcelable.Creator<HikeLocationData>(){

        @Override
        public HikeLocationData createFromParcel(Parcel source) {
            HikeLocationData data = new HikeLocationData();
            data.name = source.readString();
            data.address = source.readString();
            data.city = source.readString();
            data.distance = source.readString();
            data.email = source.readString();
            data.latitude = source.readDouble();
            data.longitude = source.readDouble();
            data.phoneNumber = source.readString();
            data.rating = source.readDouble();

            return data;
        }

        @Override
        public HikeLocationData[] newArray(int size) {
            return new HikeLocationData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeString(address);
        dest.writeString(city);
        dest.writeString(distance);
        dest.writeString(email);
        dest.writeDouble(latitude);
        dest.writeDouble(longitude);
        dest.writeString(phoneNumber);
        dest.writeDouble(rating);
    }
}
