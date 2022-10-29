package com.example.android.quakereport;

/*
* A custom class to display multiple text on the screen
* via the custom ArrayAdapter class called QuakeAdapter
* */

import java.util.Arrays;

public class EarthquakeInfo {
    //declaring the class instances used
    private double magnitude;
    private String location;
    private long timeInMilliseconds;
    private String webpage;

    //constructor...
    public EarthquakeInfo(double quakeMagnitude, String quakeLocation, long quakeDate, String address) {
        magnitude = quakeMagnitude;
        location = quakeLocation;
        timeInMilliseconds = quakeDate;
        webpage = address;

    }


    /*
    * Return the class variable set above
    * */
    public double getMagnitude() {
        return magnitude;
    }


    public String getLocation() {
        return location;
    }


    public long getDate() {
        return timeInMilliseconds;
    }

    public String getWebpage(){
        return webpage;
    }


}
