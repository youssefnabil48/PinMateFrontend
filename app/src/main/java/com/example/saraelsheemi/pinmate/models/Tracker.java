package com.example.saraelsheemi.pinmate.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sara ElSheemi on 5/12/2018.
 */

public class Tracker {

    @SerializedName("_id")
    private String id;
    @SerializedName("source")
    private MLocation source;
    @SerializedName("destination")
    private MLocation destination;
    @SerializedName("created_at")
    private String created_at;
    @SerializedName("user_id")
    private String user_id;
    @SerializedName("eta")
    private int ETA;
    @SerializedName("destination_id")
    private String destination_id;

    public String getDestination_id() {
        return destination_id;
    }

    public void setDestination_id(String destination_id) {
        this.destination_id = destination_id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public MLocation getSource() {
        return source;
    }

    public void setSource(MLocation source) {
        this.source = source;
    }

    public MLocation getDestination() {
        return destination;
    }

    public void setDestination(MLocation destination) {
        this.destination = destination;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getETA() {
        return ETA;
    }

    public void setETA(int ETA) {
        this.ETA = ETA;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
