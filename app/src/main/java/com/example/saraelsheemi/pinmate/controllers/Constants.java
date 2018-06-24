package com.example.saraelsheemi.pinmate.controllers;


public class Constants {

    public static final String CHANNEL_ID="mychannelid";
    public static final String CHANNEL_NAME="channelname";
    public static final String BASE_URL_REMOTE = "https://pin-mate.herokuapp.com";
    public static final String BASE_URL_LOCAL = "http://localhost:3000";
    public static final String CHANNEL_DESCRIPTION="channeldescription";
    public static final String REGISTER_URL= BASE_URL_REMOTE + "/api/user/create";
    public static final String LOGIN_URL= BASE_URL_REMOTE + "/api/user/signin";
    public static final String GET_ALL_USERS=BASE_URL_REMOTE + "/api/user/all";
    public static final String GET_MESSAGES = BASE_URL_REMOTE + "/api/chat";


}
