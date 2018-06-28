package com.example.saraelsheemi.pinmate.views;

import android.app.Activity;
import android.content.Context;
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
import com.example.saraelsheemi.pinmate.controllers.MessagesAdapter;
import com.example.saraelsheemi.pinmate.models.Message;
import com.example.saraelsheemi.pinmate.models.User;
import com.github.nkzawa.socketio.client.IO;
import com.github.nkzawa.socketio.client.Socket;
import com.github.nkzawa.emitter.Emitter;
import com.google.gson.Gson;

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
    private String receiverName;
    private Socket mSocket;
    private EditText editText;
    private Button sendButton;
    private SharedPreferences sharedPreferences;
    private Gson gson;
    private User loggedInUser;
    private SharedPreferences.Editor editor;
    private JSONObject requestBody = new JSONObject();
    private ArrayList<Message> messages = new ArrayList<>();
    private Message message;
    private MessagesAdapter chatMessagesAdapter;
    private ListView listView;
    private Activity currentActivity = this;
    private Emitter.Listener onNewMessage = new Emitter.Listener() {
        @Override
        public void call(final Object... args) {
            currentActivity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    JSONObject data = (JSONObject) args[0];
                    String messageContent;
                    try {
                        messageContent = data.getString("message");
                        listView.smoothScrollToPosition(chatMessagesAdapter.getCount());
                    } catch (JSONException e) {
                        e.printStackTrace();
                        return;
                    }
                    User tempUser = new User();
                    tempUser.setName(receiverName);
                    Message newMessage = new Message();
                    newMessage.setContent(messageContent);
                    newMessage.setSenderId(receiverId);
                    newMessage.setReceiverId(loggedInUser.getId());
                    newMessage.setSender(tempUser);
                    messages.add(newMessage);
                    chatMessagesAdapter.refreshMessages(messages);
                    listView.smoothScrollToPosition(chatMessagesAdapter.getCount());
                }
            });
        }
    };

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
        listView.setDivider(null);
        listView.setDividerHeight(0);
        listView.setAdapter(chatMessagesAdapter);
        this.receiverId = getIntent().getStringExtra("userId");
        this.receiverName = getIntent().getStringExtra("userName");
        try {
            mSocket = IO.socket(Constants.REMOTE_SOCKET);
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        mSocket.on("message", onNewMessage);
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
                    JSONArray dataArray = jsonObject.getJSONObject("data").getJSONArray("chat");
                    User firstUser = gson.fromJson(jsonObject.getJSONObject("data").getJSONObject("firstUser").toString(),User.class);
                    User secondUser = gson.fromJson(jsonObject.getJSONObject("data").getJSONObject("secondUser").toString(),User.class);
                    if (dataArray.length() > 0) {
                        for (int i = 0; i< dataArray.length(); i++){
                            message = gson.fromJson(dataArray.getJSONObject(i).toString(),Message.class);
                            message.setUsers(firstUser, secondUser);
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
        });
        getChatMessagesTask.execute(Constants.GET_MESSAGES);
    }


    public void populateMessagesArrayListAdapter(ArrayList<Message> messages) {
        chatMessagesAdapter.addAll(messages);
        chatMessagesAdapter.notifyDataSetChanged();
    }
    private void showMessage(String message) {
        Toast.makeText(getParent(), message, Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.button_chatbox_send: {
                String messageContent = editText.getText().toString();
                if(messageContent.length() == 0){
                    showMessage("cannot send empty message");
                    return;
                }
                JSONObject message = new JSONObject();
                try {
                    message.put("receiverId", receiverId);
                    message.put("message", messageContent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                mSocket.emit("message", message);
                editText.setText("");
                Message messageObj = new Message();
                messageObj.setContent(messageContent);
                messageObj.setSender(loggedInUser);
                messageObj.setSenderId(loggedInUser.getId());
                messageObj.setReceiverId(receiverId);
                messages.add(messageObj);
                chatMessagesAdapter.refreshMessages(messages);
                listView.smoothScrollToPosition(chatMessagesAdapter.getCount());
            }
            break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mSocket.disconnect();
        mSocket.off("message", onNewMessage);
    }
}
