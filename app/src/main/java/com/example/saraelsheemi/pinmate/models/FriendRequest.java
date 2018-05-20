package com.example.saraelsheemi.pinmate.models;

/**
 * Created by Sara ElSheemi on 5/12/2018.
 */

public class FriendRequest {

    private String created_at;
    private boolean Status;
    private User sender_id;
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
