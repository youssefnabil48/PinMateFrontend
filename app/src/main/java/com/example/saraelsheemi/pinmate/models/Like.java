package com.example.saraelsheemi.pinmate.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sara ElSheemi on 5/12/2018.
 */

public class Like {

    @SerializedName("_id")
    private String id;
    @SerializedName("user_id")
    private String user_id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }
}
