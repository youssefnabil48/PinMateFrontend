package com.example.saraelsheemi.pinmate.views.user;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.saraelsheemi.pinmate.R;
import com.example.saraelsheemi.pinmate.controllers.AsynchTaskGet;
import com.example.saraelsheemi.pinmate.controllers.Constants;
import com.example.saraelsheemi.pinmate.controllers.EventListener;
import com.example.saraelsheemi.pinmate.controllers.FriendListAdapter;
import com.example.saraelsheemi.pinmate.models.User;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FriendListFragment extends Fragment {
    private ListView listViewUsers;
    private ArrayList<String> friendsIds;
    private ArrayList<User> friendsList;
    private ArrayAdapter<User> userArrayAdapter;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressBar progressBar;
    Gson gson;
    User user;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_friend_list_view, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        init(view);
        getFriendsIds();
        super.onViewCreated(view, savedInstanceState);
    }

    public void init(View view) {
        sharedPreferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
        friendsList = new ArrayList<>();
        progressBar = view.findViewById(R.id.progressbar_loading);
        listViewUsers = view.findViewById(R.id.listView_users);
        listViewUsers.setOnItemClickListener(onItemClickListener);
        userArrayAdapter = new FriendListAdapter(getContext(), R.layout.activity_friends_list_item, new ArrayList<User>());
        listViewUsers.setAdapter(userArrayAdapter);

    }


    private void getFriendsIds() {
        String json = sharedPreferences.getString("user_info", "");
        gson = new Gson();
        user = gson.fromJson(json, User.class);

        AsynchTaskGet asynchTaskGet = new AsynchTaskGet(getContext(), new EventListener<String>() {
            @Override
            public void onSuccess(String object) {
                JSONObject jsonObject = null;
                String message = "";
                JSONArray jsonArray;
                Boolean ok = false;

                try {
                    jsonObject = new JSONObject(object);
                    ok = jsonObject.getBoolean("ok");
                    message = jsonObject.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (ok && message.contains("friends loaded")) {
                    try {
                        jsonArray = jsonObject.getJSONArray("data");
                        friendsList = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            User u = gson.fromJson(jsonArray.get(i).toString(), User.class);
                            friendsList.add(u);
                        }
                        populateFriendsArrayListAdapter(friendsList);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else if (ok && message.contains("No friends")) {
                    Log.e("Get friends", "No friends found");
                }
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("Get friends", "internal server error");
            }
        });

        asynchTaskGet.execute(Constants.GET_FRIENDS + user.getId());


    }


    public void populateFriendsArrayListAdapter(ArrayList<User> allFriendsArrayList) {
        userArrayAdapter.addAll(allFriendsArrayList);
        userArrayAdapter.notifyDataSetChanged();
    }


    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            User user = (User) adapterView.getItemAtPosition(position);
            showMessage(user.getName());

        }
    };

    private void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
