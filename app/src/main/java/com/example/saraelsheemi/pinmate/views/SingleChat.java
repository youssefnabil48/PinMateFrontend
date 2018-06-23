package com.example.saraelsheemi.pinmate.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.saraelsheemi.pinmate.R;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;

public class SingleChat extends AppCompatActivity implements View.OnClickListener {
    private String userId;
    private Socket mSocket;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_chat);
        init();
    }

    private void init(){
        EditText editText = (EditText) findViewById(R.id.edittext_chatbox);
        Button sendButton = (Button) findViewById(R.id.button_chatbox_send);
        sendButton.setOnClickListener(this);
        this.userId = getIntent().getStringExtra("userId");
        try {
            mSocket = IO.socket("http://192.168.1.104:4000");
        } catch (URISyntaxException e) {}
        mSocket.connect();
        JSONObject message = new JSONObject();
        try {
            message.put("email", "user1@user.com");
            message.put("userid", "5b2ce17913c7ef00147e5aa9");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        mSocket.emit("login", message);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_chatbox_send: {
                // send message to user
            }
            break;
        }
    }
}
