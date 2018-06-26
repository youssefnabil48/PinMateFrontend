package com.example.saraelsheemi.pinmate.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sara ElSheemi on 5/12/2018.
 */

public class MLocation {

    @SerializedName("latitude")
    private double latitude;
    @SerializedName("longitude")
    private double longitude;

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
