package com.example.saraelsheemi.pinmate.models;

import com.google.gson.annotations.SerializedName;

public class Message {
    @SerializedName("sender_id")
    private String senderId;
    @SerializedName("receiver_id")
    private String receiverId;
    @SerializedName("content")
    private String content;


    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }



}
