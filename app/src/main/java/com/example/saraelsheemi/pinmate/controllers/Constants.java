package com.example.saraelsheemi.pinmate.controllers;


public class Constants {

    public static final String CHANNEL_ID="mychannelid";
    public static final String CHANNEL_NAME="channelname";
    public static final String CHANNEL_DESCRIPTION="channeldescription";


    //location constants
    public static final Double DEFAULT_LATITUDE= 26.8206;
    public static final Double DEFAULT_LONTIDUE=30.8025;

    //backend urls
   // public static final String BACKEND_URL="https://pin-mate.herokuapp.com/api";
    public static final String BACKEND_URL="http://192.168.1.6:3000/api";

    //user urls
    public static final String REGISTER_URL= BACKEND_URL + "/user/create";
    public static final String LOGIN_URL= BACKEND_URL + "/user/signin";
    public static final String GET_ALL_USERS= BACKEND_URL + "/user/all";
    public static final String GET_USER= BACKEND_URL + "/user/";
    public static final String GET_USER_HANGOUTS= BACKEND_URL + "/hangoutrequest/getsndrrequests/";
    public static final String UPDATE_USER= BACKEND_URL + "/user/update/";
    public static final String GET_FRIENDS= BACKEND_URL + "/user/friends/";
    public static final String GET_USER_FAVS_PLACES = BACKEND_URL + "/user/favoriteplaces/";

    //places url
    public static final String GET_PLACES= BACKEND_URL + "/place/all";
    public static final String GET_PLACE= BACKEND_URL + "/place/getbyid/";
    public static final String FAVORITE_PLACE= BACKEND_URL + "/place/favorite";
    public static final String UNFAVORITE_PLACE= BACKEND_URL + "/place/unfavorite";

    //post urls
    public static final String CREATE_POST= BACKEND_URL + "/place/post/create";
    public static final String DELETE_POST= BACKEND_URL + "/place/post/delete/";


}
