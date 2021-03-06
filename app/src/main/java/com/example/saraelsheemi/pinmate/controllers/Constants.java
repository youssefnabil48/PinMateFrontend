package com.example.saraelsheemi.pinmate.controllers;


public class Constants {

    public static final String CHANNEL_ID="mychannelid";
    public static final String CHANNEL_NAME="channelname";
    public static final String CHANNEL_DESCRIPTION="channeldescription";


    //location constants
    public static final Double DEFAULT_LATITUDE= 26.8206;
    public static final Double DEFAULT_LONTIDUE=30.8025;

    //backend urls
    public static final String BACKEND_URL="https://pin-mate.herokuapp.com/api";
  //  public static final String BACKEND_URL="http://192.168.2.2:3000/api";
    public static final String REMOTE_SOCKET="wws://pin-mate.herokuapp.com";

    //user urls
    public static final String REGISTER_URL= BACKEND_URL + "/user/create";
    public static final String LOGIN_URL= BACKEND_URL + "/user/signin";
    public static final String GET_ALL_USERS= BACKEND_URL + "/user/all";
    public static final String GET_USER= BACKEND_URL + "/user/";
    public static final String UPDATE_USER= BACKEND_URL + "/user/update/";
    public static final String GET_MESSAGES = BACKEND_URL + "/chat";
    public static final String GET_FRIENDS= BACKEND_URL + "/user/friends/";
    public static final String GET_USER_FAVS_PLACES = BACKEND_URL + "/user/favoriteplaces/";
    public static final String DELETE_USER = BACKEND_URL + "/user/delete/";
    public static final String GET_BLOCKED = BACKEND_URL + "/user/blocks/";

    //places url
    public static final String GET_PLACES= BACKEND_URL + "/place/all";
    public static final String GET_PLACE= BACKEND_URL + "/place/getbyid/";
    public static final String FAVORITE_PLACE= BACKEND_URL + "/place/favorite";
    public static final String UNFAVORITE_PLACE= BACKEND_URL + "/place/unfavorite";

    //post urls
    public static final String CREATE_POST= BACKEND_URL + "/place/post/create";
    public static final String DELETE_POST= BACKEND_URL + "/place/post/delete/";

    //hangout requests urls
    public static final String CREATE_HANGOUT= BACKEND_URL + "/hangoutRequest/create";
    public static final String GET_USER_HANGOUTS= BACKEND_URL + "/hangoutrequest/getsndrrequests/";
    public static final String GET_HANGOUTS= BACKEND_URL + "/hangoutRequest/getrcvrrequests/";
    public static final String RESPOND_HANGOUT_REQUEST = BACKEND_URL + "/hangoutRequest/respond";
    public static final String DELETE_HANGOUT_REQUEST = BACKEND_URL + "/hangoutRequest/delete/";

    //friend requests urls
    public static final String SEND_FRIEND_REQUEST  = BACKEND_URL + "/friendRequest/create";
    public static final String RESPOND_FRIEND_REQUEST = BACKEND_URL + "/friendRequest/respond";
    public static final String GET_FRIEND_REQUESTS = BACKEND_URL + "/friendRequest/getrcvrrequests/";


    //search urls
    public static final String SEARCH = BACKEND_URL + "/search/";

    //tracker urls
    public static final String CREATE_TRACKER = BACKEND_URL + "/tracker/create";
    public static final String DELETE_TRACKER = BACKEND_URL + "/tracker/delete/";
    public static final String GET_TRACKERS = BACKEND_URL + "/tracker/getfriendstracker/";

    //events requests urls
    public static final String DELETE_EVENT= BACKEND_URL + "/place/event/delete/";
    public static final String CREATE_EVENT= BACKEND_URL + "/place/event/create/";

    //reviews
    public static final String CREATE_REVIEW= BACKEND_URL + "/place/review/create";
    public static final String DELETE_Review= BACKEND_URL + "/place/review/delete/";

    //recommendation
    public static final String RECOMMEND_PLACE= BACKEND_URL + "/recommend/place";
}
