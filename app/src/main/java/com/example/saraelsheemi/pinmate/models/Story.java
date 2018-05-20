package com.example.saraelsheemi.pinmate.models;

import java.util.ArrayList;

/**
 * Created by Sara ElSheemi on 5/12/2018.
 */

public class Story {

    private String id;
    private String posted_at;
    private String caption;
    private String file;
    private Place place;
    private ArrayList<User> viewers;

    public String getPosted_at() {
        return posted_at;
    }

    public void setPosted_at(String posted_at) {
        this.posted_at = posted_at;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getFile() {
        return file;
    }

    public void setFile(String file) {
        this.file = file;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }

    public ArrayList<User> getViewers() {
        return viewers;
    }

    public void setViewers(ArrayList<User> viewers) {
        this.viewers = viewers;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
