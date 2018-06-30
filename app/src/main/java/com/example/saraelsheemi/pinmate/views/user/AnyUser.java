package com.example.saraelsheemi.pinmate.views.user;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saraelsheemi.pinmate.R;
import com.example.saraelsheemi.pinmate.controllers.AsynchTaskGet;
import com.example.saraelsheemi.pinmate.controllers.AsynchTaskPost;
import com.example.saraelsheemi.pinmate.controllers.Constants;
import com.example.saraelsheemi.pinmate.controllers.EventListener;
import com.example.saraelsheemi.pinmate.controllers.MLRoundedImageView;
import com.example.saraelsheemi.pinmate.controllers.OnlineUsersAdapter;
import com.example.saraelsheemi.pinmate.models.User;
import com.example.saraelsheemi.pinmate.views.Home;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AnyUser extends AppCompatActivity {

    MLRoundedImageView userPicture;
    ImageView userCoverPicture;
    Gson gson;
    User user;
    User loggedInUser;
    TextView name,userName, birthDate, gender, email, password, home;
    ImageButton editInfo;
    Button btnAddFriend;
    RelativeLayout lowerHalf;
    RelativeLayout upperHalf;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_any_user);
        init();
        populateData();
    }

    private void init(){
        userCoverPicture = findViewById(R.id.img_userp_cover);
        userPicture = findViewById(R.id.imground_user_profile);
        userName = findViewById(R.id.txt_placep_username);
        name = findViewById(R.id.txt_placep_name);
        email = findViewById(R.id.txt_place_address);
        password = findViewById(R.id.txt_place_phone);
        birthDate = findViewById(R.id.txt_user_birthday);
        gender = findViewById(R.id.txt_user_gender);
        home= findViewById(R.id.txt_user_home);
        editInfo= findViewById(R.id.btn_edit_userinfo);
        btnAddFriend = findViewById(R.id.add_friend_btn);
        lowerHalf = findViewById(R.id.lower_half);
        upperHalf = findViewById(R.id.upper_half);
        loggedInUser = getLoggedinUser();
        gson = new Gson();
        user = gson.fromJson(getIntent().getStringExtra("user"), User.class);
        getFriendRequests();
        if (checkIfFriends(loggedInUser, user)){
            hideAddFriendBtn();
        }
        btnAddFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendFriendRequest();
            }
        });
    }
    private void hideAddFriendBtn(){
        btnAddFriend.setVisibility(View.INVISIBLE);
        RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) lowerHalf.getLayoutParams();
        lp.addRule(RelativeLayout.BELOW, upperHalf.getId());
    }
    private void populateData(){
        userName.setText(user.getName());
        name.setText(user.getName());
        email.setText(user.getEmail());
        birthDate.setText(user.getBirth_date());
        gender.setText(user.getGender());
        if(user.getPicture() != null)
            Picasso.get().load(user.getPicture()).into(userPicture);
        Picasso.get().load("https://www.whcc.com/assets/ee/uploads/images/cover-options/whcc_covers_large_leather_white.jpg").into(userCoverPicture);

    }

    private User getLoggedinUser(){
        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;
        sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
        String json = sharedPreferences.getString("user_info","");
        gson = new Gson();
        return gson.fromJson(json,User.class);
    }

    private boolean checkIfFriends(User user1, User user2){
        ArrayList<String> friendList = user1.getFriendList();
        return friendList.contains(user2.getId());
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void sendFriendRequest() {

        JSONObject requestBody = new JSONObject();
        try {
            requestBody.put("sender_id", loggedInUser.getId());
            requestBody.put("receiver_id", user.getId());
            requestBody.put("status", "false");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        AsynchTaskPost asynchTaskPost = new AsynchTaskPost(requestBody.toString(), getApplicationContext(), new EventListener<String>() {

            @Override
            public void onSuccess(String object) {
                JSONObject responseBody = null;
                String message = "";
                Boolean ok = false;

                try {
                    responseBody = new JSONObject(object);
                    ok = responseBody.getBoolean("ok");
                    message = responseBody.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if(ok && message.contains("Request sent")){
                    showMessage("Friend Request Sent");
                    btnAddFriend.setText("Request Sent");
                    btnAddFriend.setEnabled(false);
                }
                else {
                    showMessage("Error in sending the request");
                }
            }

            @Override
            public void onFailure(Exception e) {
                showMessage("Internal error. Please retry.");
            }
        });
        asynchTaskPost.execute(Constants.SEND_FRIEND_REQUEST);
    }

    private void getFriendRequests() {
        final AsynchTaskGet asyncTaskPost = new AsynchTaskGet(this, new EventListener<String>() {
            @Override
            public void onSuccess(String object) {
                try {
                    JSONObject responseBody = new JSONObject(object);
                    JSONArray dataArray = responseBody.getJSONArray("data");
                    //String message = responseBody.getString("message");
                    //boolean ok = responseBody.getBoolean("ok");

                    if (dataArray.length() > 0) {
                        for(int i = 0; i< dataArray.length(); i++){
                            JSONObject frequest = responseBody.getJSONArray("data").getJSONObject(i);
                            if(frequest.getString("sender_id").equals(loggedInUser.getId()) && frequest.getString("receiver_id").equals(user.getId())){
                                btnAddFriend.setText("Request Sent");
                                btnAddFriend.setEnabled(false);
                            }
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Exception e) {
                showMessage("Internal error. Please retry.");

            }
        });
        asyncTaskPost.execute(Constants.GET_FRIEND_REQUESTS+user.getId());
    }
}
