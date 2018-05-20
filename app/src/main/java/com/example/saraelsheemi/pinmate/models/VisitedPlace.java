package com.example.saraelsheemi.pinmate.models;

/**
 * Created by Sara ElSheemi on 5/12/2018.
 */

public class VisitedPlace {

    private Place place;
    private int count;
    private String timestamp;

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
