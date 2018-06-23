package com.example.saraelsheemi.pinmate.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sara ElSheemi on 5/12/2018.
 */

public class MLocation {

    @SerializedName("latitude")
    private String latitude;
    @SerializedName("longitude")
    private String longitude;


    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }
}
