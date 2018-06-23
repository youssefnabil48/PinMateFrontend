package com.example.saraelsheemi.pinmate.views;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.saraelsheemi.pinmate.R;

import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;

import java.net.URISyntaxException;

public class SingleChat extends AppCompatActivity {

    private Socket mSocket;
    {
        try {
            mSocket = IO.socket("http://localhost:4000");
        } catch (URISyntaxException e) {}
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_chat);
        mSocket.connect();
    }
}
