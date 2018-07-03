package com.example.saraelsheemi.pinmate.views.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.saraelsheemi.pinmate.R;
import com.example.saraelsheemi.pinmate.controllers.AsynchTasks.AsynchTaskGet;
import com.example.saraelsheemi.pinmate.controllers.AsynchTasks.AsynchTaskPost;
import com.example.saraelsheemi.pinmate.controllers.Constants;
import com.example.saraelsheemi.pinmate.controllers.EventListener;
import com.example.saraelsheemi.pinmate.controllers.adapters.FriendRequestsAdapter;
import com.example.saraelsheemi.pinmate.models.FriendRequest;
import com.example.saraelsheemi.pinmate.models.FriendRequestResponse;
import com.example.saraelsheemi.pinmate.models.HangoutRequest;
import com.example.saraelsheemi.pinmate.models.Place;
import com.example.saraelsheemi.pinmate.models.Request;
import com.example.saraelsheemi.pinmate.models.User;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FriendRequestsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private ListView listView;
    private String user_id;
    private ArrayAdapter<FriendRequestResponse> adapter;
    private
    SharedPreferences sharedPreferences;
    SwipeRefreshLayout swipeRefreshLayout;
    SharedPreferences.Editor editor;
    Gson gson;
    User user;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_friendreq_list_view, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        init(view);
        getRequests();
        super.onViewCreated(view, savedInstanceState);
    }

    public void init(View view) {
        sharedPreferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
        listView = view.findViewById(R.id.listView_frreq);
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh_frreq);
        swipeRefreshLayout.setOnRefreshListener(this);
        adapter = new FriendRequestsAdapter(getActivity(), R.layout.activity_friendreq_list_item,
                new ArrayList<FriendRequestResponse>(), this);
        listView.setAdapter(adapter);


    }

    public void populateFriendsArrayListAdapter(ArrayList<FriendRequestResponse> responseArrayList) {
        adapter.clear();
        adapter.addAll(responseArrayList);
        adapter.notifyDataSetChanged();
    }

    private void getRequests() {
        String json = sharedPreferences.getString("user_info", "");
        gson = new Gson();
        user = gson.fromJson(json, User.class);
        user_id = user.getId();
        Log.e("user id ", user_id);

        AsynchTaskGet asynchTaskGet = new AsynchTaskGet(getContext(), new EventListener<String>() {
            @Override
            public void onSuccess(String object) {

                JSONObject jsonObject = null;
                JSONArray jsonArrayHangs, jsonArrayUsers;
                String message = "";
                Boolean ok = false;

                try {
                    jsonObject = new JSONObject(object);
                    ok = jsonObject.getBoolean("ok");
                    message = jsonObject.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (ok && message.contains("No requests")) {
                    swipeRefreshLayout.setRefreshing(false);
                    populateFriendsArrayListAdapter(new ArrayList<FriendRequestResponse>());

                } else if (ok && message.contains("Requests loaded")) {
                    swipeRefreshLayout.setRefreshing(false);
                    try {
                        jsonArrayHangs = jsonObject.getJSONArray("data");
                        jsonArrayUsers = jsonObject.getJSONArray("sender");
                        ArrayList<FriendRequestResponse> requests = new ArrayList<>();

                        for (int i = 0; i < jsonArrayHangs.length(); i++) {
                            FriendRequest friendRequest = gson.fromJson(jsonArrayHangs.get(i).toString(), FriendRequest.class);
                            User user = gson.fromJson(jsonArrayUsers.get(i).toString(), User.class);
                            FriendRequestResponse request = new FriendRequestResponse(friendRequest, user);
                            requests.add(request);
                        }

                        populateFriendsArrayListAdapter(requests);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Exception e) {
                swipeRefreshLayout.setRefreshing(false);
                showMessage("Internal server error.");
            }
        });
        asynchTaskGet.execute(Constants.GET_FRIEND_REQUESTS + user_id);

    }


    public void respondToRequest(FriendRequestResponse request, boolean response) {
        String data = "{ \"status\": " + response
                + ",\"request_id\":\"" + request.getFriendRequest().getId() + "\","
                + "\"sender_id\":\"" + request.getFriendRequest().getSender_id() +"\","
                + "\"receiver_id\":\"" + user_id + "\"}";

        Log.e("data", data);
        AsynchTaskPost asynchTaskPost = new AsynchTaskPost(data, getContext(), new EventListener<String>() {
            @Override
            public void onSuccess(String object) {
                JSONObject jsonObject = null;
                String message = "";
                Boolean ok = false;

                try {
                    jsonObject = new JSONObject(object);
                    ok = jsonObject.getBoolean("ok");
                    message = jsonObject.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (ok && message.contains("You responded")) {
                    showMessage("Response sent.");
                    getRequests();
                }else if (ok && message.contains("Response not")) {
                    showMessage("Response not sent. Retry");
                }

            }

            @Override
            public void onFailure(Exception e) {
                showMessage("Internal server error");
            }
        });
        asynchTaskPost.execute(Constants.RESPOND_FRIEND_REQUEST);

    }

    private void showMessage(String message) {
        if (getActivity() != null)
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }


    @Override
    public void onRefresh() {
        getRequests();
    }
}
