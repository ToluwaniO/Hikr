package com.tpad.hikr.DataClasses;

/**
 * Created by toluw on 6/17/2017.
 */

public class Hikes {

    String title, location, time;

    public Hikes(String title, String location, String time) {
        this.title = title;
        this.location = location;
        this.time = time;
    }

    public Hikes() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
