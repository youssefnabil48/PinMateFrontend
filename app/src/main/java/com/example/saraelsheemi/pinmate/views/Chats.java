package com.example.saraelsheemi.pinmate.views;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saraelsheemi.pinmate.R;
import com.example.saraelsheemi.pinmate.controllers.AsynchTasks.AsynchTaskGet;
import com.example.saraelsheemi.pinmate.controllers.Constants;
import com.example.saraelsheemi.pinmate.controllers.EventListener;
import com.example.saraelsheemi.pinmate.controllers.OnlineUsersAdapter;
import com.example.saraelsheemi.pinmate.models.User;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class Chats extends Fragment implements AdapterView.OnItemClickListener{

    private ListView onlineUsersListView;
    private Gson gson = new Gson();
    private User user = new User();
    private ArrayList<User> users = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_chats, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        getOnlineUsers(view);
    }

    private void getOnlineUsers(final View view) {
        final AsynchTaskGet asyncGetOnlineUsers = new AsynchTaskGet(getContext(), new EventListener<String>() {
            @Override
            public void onSuccess(String object) {
                try {
                    JSONObject jsonObject = new JSONObject(object);
                    JSONArray dataArray = jsonObject.getJSONArray("data");
                    if (dataArray.length() > 0) {
                        for (int i = 0; i< dataArray.length(); i++){
                            user = gson.fromJson(jsonObject.getJSONArray("data").getJSONObject(i).toString(),User.class);
                            users.add(user);
                        }
                    }
                    OnlineUsersAdapter onlineUsersAdapter = new OnlineUsersAdapter(getActivity(), users);

                    // Get a reference to the ListView, and attach the adapter to the listView.
                    ListView listView = view.findViewById(R.id.onlineUsersContainer);
                    listView.setAdapter(onlineUsersAdapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Log.e("response", object);
            }

            @Override
            public void onFailure(Exception e) {
                showMessage("Internal error. Please retry.");

            }
        });
        asyncGetOnlineUsers.execute(Constants.GET_ALL_USERS);
    }

    private void init(View view){
        onlineUsersListView = view.findViewById(R.id.onlineUsersContainer);
        onlineUsersListView.setOnItemClickListener(this);
    }

    private void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        TextView idTextView = (TextView) view.findViewById(R.id.id);
        TextView userNameTextView = (TextView) view.findViewById(R.id.user_name);
        Intent intent = new Intent(getActivity(), SingleChat.class);
        intent.putExtra("userId", idTextView.getText());
        intent.putExtra("userName", userNameTextView.getText());
        startActivity(intent);
    }
}
