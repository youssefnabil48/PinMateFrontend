package com.example.saraelsheemi.pinmate.models;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Sara ElSheemi on 5/12/2018.
 */

//@SerializedName tells the json parser which element in the response matches to which attribute in the model


public class User {

    private static User userInstance = null;
    @SerializedName("_id")
    private String id;
    @SerializedName("token")
    private String user_token;
    @SerializedName("name")
    private String name;
    @SerializedName("email")
    private String email;
    @SerializedName("password")
    private String password;
    @SerializedName("gender")
    private String gender;
    @SerializedName("birth_date")
    private String birth_date;
    @SerializedName("picture")
    private String picture;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("mobile_number")
    private String mobile_number;
    @SerializedName("home_location")
    private String home_location;
    @SerializedName("location")
    private MLocation current_location;
    private Tracker tracker;
    @SerializedName("favorite_places")
    private ArrayList<String> favoritePlaces;
    @SerializedName("email_verification_tkn")
    private String email_verf_token;
    @SerializedName("tracker_id")
    private String tracker_id;
    @SerializedName("friends")
    private ArrayList<String> friendList;
    @SerializedName("blocks")
    private ArrayList<String> blockedList;
    @SerializedName("visit")
    private ArrayList<VisitedPlace> visitedPlaces;
    @SerializedName("views")
    private ArrayList<UserViewer> viewers;

    public static User getInstance()
    {
        if (userInstance == null)
        {
            userInstance = new User();
        }
        return userInstance;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getHome_location() {
        return home_location;
    }

    public void setHome_location(String home_location) {
        this.home_location = home_location;
    }

    public MLocation getCurrent_location() {
        return current_location;
    }

    public void setCurrent_location(MLocation current_location) {
        this.current_location = current_location;
    }

    public ArrayList<String> getFavoritePlaces() {
        return favoritePlaces;
    }

    public void setFavoritePlaces(ArrayList<String> favoritePlaces) {
        this.favoritePlaces = favoritePlaces;
    }

    public String getEmail_verf_token() {
        return email_verf_token;
    }

    public void setEmail_verf_token(String email_verf_token) {
        this.email_verf_token = email_verf_token;
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

    public ArrayList<String> getFriendList() {
        return friendList;
    }

    public void setFriendList(ArrayList<String> friendList) {
        this.friendList = friendList;
    }

    public ArrayList<String> getBlockedList() {
        return blockedList;
    }

    public void setBlockedList(ArrayList<String> blockedList) {
        this.blockedList = blockedList;
    }

    public String getUser_token() {
        return user_token;
    }

    public void setUser_token(String user_token) {
        this.user_token = user_token;
    }

    public String getTracker_id() {
        return tracker_id;
    }

    public void setTracker_id(String tracker_id) {
        this.tracker_id = tracker_id;
    }
}
