package com.tpad.hikr.DataClasses;

/**
 * Created by oguns on 6/30/2017.
 */

public class Discover {
    String name, city, distance;

    public Discover() {
    }

    public Discover(String name, String city, String distance) {
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
}
