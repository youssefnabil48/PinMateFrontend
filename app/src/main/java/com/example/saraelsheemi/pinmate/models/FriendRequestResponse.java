package com.example.saraelsheemi.pinmate.models;

public class FriendRequestResponse {

    private FriendRequest friendRequest;
    private User user;

    public FriendRequestResponse(FriendRequest friendRequest, User user) {
        this.friendRequest = friendRequest;
        this.user = user;
    }

    public FriendRequest getFriendRequest() {
        return friendRequest;
    }

    public void setFriendRequest(FriendRequest friendRequest) {
        this.friendRequest = friendRequest;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
