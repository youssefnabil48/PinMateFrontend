package com.example.saraelsheemi.pinmate.models;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Sara ElSheemi on 5/12/2018.
 */

public class Post {

    @SerializedName("_id")
    private String id;
    @SerializedName("user")
    private String user;
    @SerializedName("content")
    private String content;
    @SerializedName("created_at")
    private String created_at;
    @SerializedName("likes")
    private ArrayList<Like> likes;
    @SerializedName("user_name")
    private String user_name;
    @SerializedName("user_pic")
    private String user_pic;

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_pic() {
        return user_pic;
    }

    public void setUser_pic(String user_pic) {
        this.user_pic = user_pic;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public ArrayList<Like> getLikes() {
        return likes;
    }

    public void setLikes(ArrayList<Like> likes) {
        this.likes = likes;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
