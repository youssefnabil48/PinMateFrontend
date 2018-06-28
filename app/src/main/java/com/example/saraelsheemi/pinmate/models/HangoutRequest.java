package com.example.saraelsheemi.pinmate.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Sara ElSheemi on 5/12/2018.
 */

public class HangoutRequest {

    @SerializedName("date")
    private String date;
    @SerializedName("description")
    private String description;
    @SerializedName("start_time")
    private String start_time;
    @SerializedName("title")
    private String title;
    @SerializedName("created_by")
    private String created_by_id;
    @SerializedName("invited")
    private ArrayList<String> invited;
    @SerializedName("place")
    private String place_id;

    public String getPlace_id() {
        return place_id;
    }

    public void setPlace_id(String place_id) {
        this.place_id = place_id;
    }

    public String getCreated_by_id() {
        return created_by_id;
    }

    public void setCreated_by_id(String created_by_id) {
        this.created_by_id = created_by_id;
    }

    public ArrayList<String> getInvited() {
        return invited;
    }

    public void setInvited(ArrayList<String> invited) {
        this.invited = invited;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStart_time() {
        return start_time;
    }

    public void setStart_time(String start_time) {
        this.start_time = start_time;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


}
