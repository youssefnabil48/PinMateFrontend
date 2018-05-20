package com.example.saraelsheemi.pinmate.models;

import java.util.ArrayList;

/**
 * Created by Sara ElSheemi on 5/12/2018.
 */

public class HangoutRequest {

    private String date;
    private String description;
    private String start_time;
    private String title;
    private User created_by;
    private ArrayList<Respondand> responded_by;

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

    public User getCreated_by() {
        return created_by;
    }

    public void setCreated_by(User created_by) {
        this.created_by = created_by;
    }

    public ArrayList<Respondand> getResponded_by() {
        return responded_by;
    }

    public void setResponded_by(ArrayList<Respondand> responded_by) {
        this.responded_by = responded_by;
    }
}
