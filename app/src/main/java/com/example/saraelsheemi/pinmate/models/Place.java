package com.example.saraelsheemi.pinmate.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Sara ElSheemi on 5/12/2018.
 */

public class Place {

    @SerializedName("_id")
    private String id;
    @SerializedName("name")
    private String name;
    @SerializedName("avatar")
    private String avatar;
    @SerializedName("picture")
    private String picture;
    @SerializedName("description")
    private String decsription;
    @SerializedName("location")
    private MLocation location;
    @SerializedName("gallery")
    private ArrayList<String> gallery;
    @SerializedName("mobile_number")
    private String mobile_number;
    @SerializedName("managed_by")
    private User manager;
    @SerializedName("events")
    private ArrayList<Event> events;
    @SerializedName("posts")
    private ArrayList<Post> posts;
    @SerializedName("reviews")
    private ArrayList<Review> reviews;
    @SerializedName("story_id")
    private ArrayList<Story> stories;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getDecsription() {
        return decsription;
    }

    public void setDecsription(String decsription) {
        this.decsription = decsription;
    }

    public MLocation getLocation() {
        return location;
    }

    public void setLocation(MLocation location) {
        this.location = location;
    }

    public ArrayList<String> getGallery() {
        return gallery;
    }

    public void setGallery(ArrayList<String> gallery) {
        this.gallery = gallery;
    }

    public String getMobile_number() {
        return mobile_number;
    }

    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }

    public User getManager() {
        return manager;
    }

    public void setManager(User manager) {
        this.manager = manager;
    }

    public ArrayList<Event> getEvents() {
        return events;
    }

    public void setEvents(ArrayList<Event> events) {
        this.events = events;
    }

    public ArrayList<Post> getPosts() {
        return posts;
    }

    public void setPosts(ArrayList<Post> posts) {
        this.posts = posts;
    }

    public ArrayList<Review> getReviews() {
        return reviews;
    }

    public void setReviews(ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Story> getStories() {
        return stories;
    }

    public void setStories(ArrayList<Story> stories) {
        this.stories = stories;
    }
}
