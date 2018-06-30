package com.example.saraelsheemi.pinmate.models;

public class Request {

    private HangoutRequest hangoutRequest;
    private User user;
    private Place place;

    public Request(HangoutRequest hangoutRequest, User user, Place place) {
        this.hangoutRequest = hangoutRequest;
        this.user = user;
        this.place = place;
    }

    public HangoutRequest getHangoutRequest() {
        return hangoutRequest;
    }

    public void setHangoutRequest(HangoutRequest hangoutRequest) {
        this.hangoutRequest = hangoutRequest;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Place getPlace() {
        return place;
    }

    public void setPlace(Place place) {
        this.place = place;
    }
}
