package com.example.saraelsheemi.pinmate.views;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.saraelsheemi.pinmate.R;

import com.example.saraelsheemi.pinmate.models.User;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;
import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class SingleChat extends AppCompatActivity implements View.OnClickListener {
    private String receiverId;
    private Socket mSocket;
    EditText editText;
    Button sendButton;
    SharedPreferences sharedPreferences;
    Gson gson;
    User loggedInUser;
    SharedPreferences.Editor editor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_chat);
        init();
    }

    private void init(){
        editText = findViewById(R.id.edittext_chatbox);
        sendButton = findViewById(R.id.button_chatbox_send);
        sendButton.setOnClickListener(this);
        this.receiverId = getIntent().getStringExtra("userId");
        try {
            mSocket = IO.socket("http://192.168.1.104:4000");
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        mSocket.connect();
        JSONObject message = new JSONObject();
        sharedPreferences = this.getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
        String json = sharedPreferences.getString("user_info","");
        gson = new Gson();
        loggedInUser = gson.fromJson(json,User.class);
        try {
            message.put("email", loggedInUser.getEmail());
            message.put("userid", loggedInUser.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSocket.emit("login", message);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_chatbox_send: {
                String messageContent = editText.getText().toString();
                JSONObject message = new JSONObject();
                try {
                    message.put("receiverId", receiverId);
                    message.put("message", messageContent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mSocket.emit("message", message);
                editText.setText("");
            }
            break;
        }
    }
}
