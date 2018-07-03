package com.example.saraelsheemi.pinmate.models;

public class TrackerResponse {

    Tracker tracker;
    User user;

    public TrackerResponse(Tracker tracker, User user) {
        this.tracker = tracker;
        this.user = user;
    }

    public Tracker getTracker() {
        return tracker;
    }

    public void setTracker(Tracker tracker) {
        this.tracker = tracker;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
