package com.example.saraelsheemi.pinmate.models;

import android.support.annotation.NonNull;
import android.util.Log;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

public class Message implements Comparable<Message>{
    @SerializedName("sender_id")
    private String senderId;
    @SerializedName("receiver_id")
    private String receiverId;
    @SerializedName("content")
    private String content;
    @SerializedName("created_at")
    private String createdAt;
    private User sender;
    private User receiver;

    public Message() {
    }
    public User getSender() {
        return sender;
    }

    public void setSender(User sender) {
        this.sender = sender;
    }

    public User getReceiver() {
        return receiver;
    }

    public void setReceiver(User receiver) {
        this.receiver = receiver;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public void setUsers(User first, User second){
        if(first.getId() == senderId){
            this.setSender(first);
            this.setReceiver(second);
        }else{
            this.setSender(second);

            this.setReceiver(first);
        }
    }

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

    @Override
    public int compareTo(@NonNull Message message) {
        return this.createdAt.compareTo(message.getCreatedAt());
    }
}
