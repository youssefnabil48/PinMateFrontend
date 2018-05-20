package com.example.saraelsheemi.pinmate.models;
import java.util.ArrayList;

/**
 * Created by Sara ElSheemi on 5/12/2018.
 */



public class User {

    private String id;
    private String name;
    private String username;
    private String email;
    private String password;
    private String gender;
    private String birth_date;
    private String picture;
    private String avatar;
    private String mobile_number;
    private Location home_location;
    private Location current_location;
    private Tracker tracker;
    private ArrayList<Place> favoritePlaces;
    private String email_verf_token;
    private ArrayList<User> friendList;
    private ArrayList<User> blockedList;
    private ArrayList<VisitedPlace> visitedPlaces;
    private ArrayList<UserViewer> viewers;
    private ArrayList<Story> stories;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getBirth_date() {
        return birth_date;
    }

    public void setBirth_date(String birth_date) {
        this.birth_date = birth_date;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public Location getHome_location() {
        return home_location;
    }

    public void setHome_location(Location home_location) {
        this.home_location = home_location;
    }

    public Location getCurrent_location() {
        return current_location;
    }

    public void setCurrent_location(Location current_location) {
        this.current_location = current_location;
    }

    public ArrayList<Place> getFavoritePlaces() {
        return favoritePlaces;
    }

    public void setFavoritePlaces(ArrayList<Place> favoritePlaces) {
        this.favoritePlaces = favoritePlaces;
    }

    public String getEmail_verf_token() {
        return email_verf_token;
    }

    public void setEmail_verf_token(String email_verf_token) {
        this.email_verf_token = email_verf_token;
    }

    public ArrayList<User> getFriendList() {
        return friendList;
    }

    public void setFriendList(ArrayList<User> friendList) {
        this.friendList = friendList;
    }

    public ArrayList<User> getBlockedList() {
        return blockedList;
    }

    public void setBlockedList(ArrayList<User> blockedList) {
        this.blockedList = blockedList;
    }

    public ArrayList<VisitedPlace> getVisitedPlaces() {
        return visitedPlaces;
    }

    public void setVisitedPlaces(ArrayList<VisitedPlace> visitedPlaces) {
        this.visitedPlaces = visitedPlaces;
    }

    public ArrayList<UserViewer> getViewers() {
        return viewers;
    }

    public void setViewers(ArrayList<UserViewer> viewers) {
        this.viewers = viewers;
    }

    public ArrayList<Story> getStories() {
        return stories;
    }

    public void setStories(ArrayList<Story> stories) {
        this.stories = stories;
    }

    public Tracker getTracker() {
        return tracker;
    }

    public void setTracker(Tracker tracker) {
        this.tracker = tracker;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
