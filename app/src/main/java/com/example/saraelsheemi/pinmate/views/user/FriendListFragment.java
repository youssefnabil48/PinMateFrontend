package com.example.saraelsheemi.pinmate.views.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
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
import com.example.saraelsheemi.pinmate.controllers.AsynchTasks.AsynchTaskGet;
import com.example.saraelsheemi.pinmate.controllers.Constants;
import com.example.saraelsheemi.pinmate.controllers.EventListener;
import com.example.saraelsheemi.pinmate.controllers.adapters.FriendListAdapter;
import com.example.saraelsheemi.pinmate.models.Place;
import com.example.saraelsheemi.pinmate.models.User;
import com.example.saraelsheemi.pinmate.views.place.PlaceProfile;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FriendListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private ListView listViewUsers;
    private ArrayList<String> friendsIds;
    private ArrayList<User> friendsList;
    private ArrayAdapter<User> userArrayAdapter;
    SharedPreferences sharedPreferences;
    SwipeRefreshLayout swipeRefreshLayout;
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
        getFriendsIds("user_info");
        super.onViewCreated(view, savedInstanceState);
    }

    public void init(View view) {
        sharedPreferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
        friendsList = new ArrayList<>();
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(this);
        progressBar = view.findViewById(R.id.progressbar_loading);
        listViewUsers = view.findViewById(R.id.listView_users);
        listViewUsers.setOnItemClickListener(onItemClickListener);
        userArrayAdapter = new FriendListAdapter(getContext(), R.layout.activity_friends_list_item, new ArrayList<User>());
        listViewUsers.setAdapter(userArrayAdapter);

    }


    public void getFriendsIds(String shared) {
        String json = sharedPreferences.getString(shared, "");
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
                    swipeRefreshLayout.setRefreshing(false);
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
                    swipeRefreshLayout.setRefreshing(false);

                    Log.e("Get friends", "No friends found");
                }
            }

            @Override
            public void onFailure(Exception e) {
                swipeRefreshLayout.setRefreshing(false);

                Log.e("Get friends", "internal server error");
            }
        });

        asynchTaskGet.execute(Constants.GET_FRIENDS + user.getId());


    }


    public void populateFriendsArrayListAdapter(ArrayList<User> allFriendsArrayList) {
        userArrayAdapter.clear();
        userArrayAdapter.addAll(allFriendsArrayList);
        userArrayAdapter.notifyDataSetChanged();
    }


    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

            User u = (User) adapterView.getItemAtPosition(position);
            String jsonData = gson.toJson(u, User.class);
            editor.putString("friend_details", jsonData);
            editor.apply();
            //open user profile
            Fragment profile = new FriendProfileFragment();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_frame, profile); // give your fragment container id in first parameter
            transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
            transaction.commit();

        }
    };

    private void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onRefresh() {
        getFriendsIds("user_info");
    }
}
