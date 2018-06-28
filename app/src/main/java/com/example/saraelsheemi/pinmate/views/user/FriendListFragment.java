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
        friendsIds = user.getFriendList();

        if (!friendsIds.isEmpty())
            Log.e("IDS", friendsIds.get(0));


        //i have friends ids now i have to get each one
        for (int i = 0; i < friendsIds.size(); i++) {
            AsynchTaskGet asynchTaskGet = new AsynchTaskGet(getContext(), new EventListener<String>() {
                @Override
                public void onSuccess(String object) {
                    progressBar.setVisibility(View.INVISIBLE);
                    JSONObject jsonObject = null;
                    String message = "";
                    Boolean ok = false;
                    User user1 = null;

                    try {
                        jsonObject = new JSONObject(object);
                        ok = jsonObject.getBoolean("ok");
                        message = jsonObject.getString("message");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if (ok && message.contains("User not")) {
                        showMessage("No friends yet.");
                    } else if (ok && message.contains("User found")) {
                        try {
                            user1 = gson.fromJson(jsonObject.getString("data"), User.class);
                            friendsList = new ArrayList<>();
                            friendsList.add(user1);
                            populateFriendsArrayListAdapter(friendsList);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }

                @Override
                public void onFailure(Exception e) {
                    showMessage("Internal server error.");
                }
            });
            asynchTaskGet.execute(Constants.GET_USER + friendsIds.get(i));
        }



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
