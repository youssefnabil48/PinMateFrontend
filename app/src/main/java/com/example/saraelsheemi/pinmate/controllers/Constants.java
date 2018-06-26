package com.example.saraelsheemi.pinmate.controllers;


public class Constants {

    public static final String CHANNEL_ID="mychannelid";
    public static final String CHANNEL_NAME="channelname";
    public static final String BASE_URL_REMOTE = "https://pin-mate.herokuapp.com";
    public static final String BASE_URL_LOCAL = "http://localhost:3000";
    public static final String CHANNEL_DESCRIPTION="channeldescription";

    //location constants
    public static final Double DEFAULT_LATITUDE= 26.8206;
    public static final Double DEFAULT_LONTIDUE=30.8025;

    //backend urls
    public static final String BACKEND_URL="https://pin-mate.herokuapp.com/api";
    public static final String REGISTER_URL= BACKEND_URL + "/user/create";
    public static final String LOGIN_URL= BACKEND_URL + "/user/signin";
    public static final String GET_ALL_USERS= BACKEND_URL + "/user/all";
    public static final String GET_USER= BACKEND_URL + "/user/";
    public static final String GET_USER_HANGOUTS= BACKEND_URL + "/hangoutrequest/getsndrrequests/";
    public static final String UPDATE_USER= BACKEND_URL + "/user/update/";
    public static final String GET_FRIENDS= BACKEND_URL + "/user/getfriends/";
    public static final String GET_MESSAGES = BACKEND_URL + "/api/chat";

}
