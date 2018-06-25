package com.example.saraelsheemi.pinmate.views;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.saraelsheemi.pinmate.R;

import com.example.saraelsheemi.pinmate.controllers.AsynchTaskPost;
import com.example.saraelsheemi.pinmate.controllers.Constants;
import com.example.saraelsheemi.pinmate.controllers.EventListener;
import com.example.saraelsheemi.pinmate.controllers.FriendListAdapter;
import com.example.saraelsheemi.pinmate.controllers.MessagesAdapter;
import com.example.saraelsheemi.pinmate.controllers.OnlineUsersAdapter;
import com.example.saraelsheemi.pinmate.models.Message;
import com.example.saraelsheemi.pinmate.models.User;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import android.content.SharedPreferences;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;

public class SingleChat extends AppCompatActivity implements View.OnClickListener {
    private String receiverId;
    private Socket mSocket;
    private EditText editText;
    Button sendButton;
    SharedPreferences sharedPreferences;
    Gson gson;
    User loggedInUser;
    SharedPreferences.Editor editor;
    JSONObject requestBody = new JSONObject();
    private ArrayList<Message> messages = new ArrayList<>();
    Message message;
    MessagesAdapter chatMessagesAdapter;
    ListView listView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_chat);
        init();
        getChatMessages();
    }

    private void init(){
        editText = findViewById(R.id.edittext_chatbox);
        sendButton = findViewById(R.id.button_chatbox_send);
        sendButton.setOnClickListener(this);
        chatMessagesAdapter = new MessagesAdapter(this,R.id.messages_list,new ArrayList<Message>());
        listView = findViewById(R.id.messages_list);
        listView.setAdapter(chatMessagesAdapter);
        this.receiverId = getIntent().getStringExtra("userId");
        try {
            mSocket = IO.socket("http://192.168.1.24:4000");
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


    private void getChatMessages (){

        try {
            requestBody.put("secondId", receiverId);
            requestBody.put("firstId", loggedInUser.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        AsynchTaskPost getChatMessagesTask = new AsynchTaskPost(requestBody.toString(), getApplicationContext(), new EventListener<String>() {

            @Override
            public void onSuccess(String object) {
                try {
                    JSONObject jsonObject = new JSONObject(object);
                    JSONArray dataArray = jsonObject.getJSONArray("data");
                    Log.e("response", dataArray.toString());
                    if (dataArray.length() > 0) {
                        for (int i = 0; i< dataArray.length(); i++){
                            message = gson.fromJson(jsonObject.getJSONArray("data").getJSONObject(i).toString(),Message.class);
                            messages.add(message);
                        }
                    }
                    Log.e("messages size", String.valueOf(messages.size()));
                    populateMessagesArrayListAdapter(messages);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }

            @Override
            public void onFailure(Exception e) {
                showMessage("Internal error. Please retry.");
            }

            private void showMessage(String message) {
                Toast.makeText(getParent(), message, Toast.LENGTH_SHORT).show();
            }

        });
        getChatMessagesTask.execute(Constants.GET_MESSAGES);
    }


    public void populateMessagesArrayListAdapter(ArrayList<Message> messages) {
        chatMessagesAdapter.addAll(messages);
        chatMessagesAdapter.notifyDataSetChanged();
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
