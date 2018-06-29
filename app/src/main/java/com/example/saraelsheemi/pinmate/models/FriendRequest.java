package com.example.saraelsheemi.pinmate.models;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Sara ElSheemi on 5/12/2018.
 */

public class FriendRequest {

    @SerializedName("created_at")
    private String created_at;
    @SerializedName("status")
    private boolean Status;
    @SerializedName("sender_id")
    private User sender_id;
    @SerializedName("receiver_id")
    private User receiver_id;

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public boolean isStatus() {
        return Status;
    }

    public void setStatus(boolean status) {
        Status = status;
    }

    public User getSender_id() {
        return sender_id;
    }

    public void setSender_id(User sender_id) {
        this.sender_id = sender_id;
    }

    public User getReceiver_id() {
        return receiver_id;
    }

    public void setReceiver_id(User receiver_id) {
        this.receiver_id = receiver_id;
    }
}
