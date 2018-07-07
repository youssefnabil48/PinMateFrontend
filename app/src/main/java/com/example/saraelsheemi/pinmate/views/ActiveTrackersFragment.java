package com.example.saraelsheemi.pinmate.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.example.saraelsheemi.pinmate.R;
import com.example.saraelsheemi.pinmate.controllers.AsynchTasks.AsynchTaskGet;
import com.example.saraelsheemi.pinmate.controllers.Constants;
import com.example.saraelsheemi.pinmate.controllers.EventListener;
import com.example.saraelsheemi.pinmate.controllers.adapters.RequestsAdapter;
import com.example.saraelsheemi.pinmate.controllers.adapters.TrackersAdapter;
import com.example.saraelsheemi.pinmate.models.HangoutRequest;
import com.example.saraelsheemi.pinmate.models.Place;
import com.example.saraelsheemi.pinmate.models.Request;
import com.example.saraelsheemi.pinmate.models.Tracker;
import com.example.saraelsheemi.pinmate.models.TrackerResponse;
import com.example.saraelsheemi.pinmate.models.User;
import com.example.saraelsheemi.pinmate.views.place.PlaceProfile;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import static android.content.ContentValues.TAG;

public class ActiveTrackersFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private ListView listViewTrackers;
    private String user_id;
    private ArrayAdapter<TrackerResponse> adapter;
    SwipeRefreshLayout swipeRefreshLayout;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Gson gson;
    User user;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_trackers_list_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        init(view);
        getTrackers();
        getActivity().setTitle("Active Trackers");
        super.onViewCreated(view, savedInstanceState);
    }

    private void getTrackers() {
        String json = sharedPreferences.getString("user_info", "");
        gson = new Gson();
        user = gson.fromJson(json, User.class);
        user_id = user.getId();
        Log.e("user id ", user_id);

        AsynchTaskGet asynchTaskGet = new AsynchTaskGet(getContext(), new EventListener<String>() {
            @Override
            public void onSuccess(String object) {

                JSONObject jsonObject = null;
                JSONArray jsonArrayHangs, jsonArrayUsers, jsonArrayPlaces;
                String message = "";
                Boolean ok = false;

                try {
                    jsonObject = new JSONObject(object);
                    ok = jsonObject.getBoolean("ok");
                    Log.d(TAG, "onSuccess: " + ok);
                    message = jsonObject.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (ok && message.contains("No trackers")) {
                    swipeRefreshLayout.setRefreshing(false);


                } else if (ok && message.contains("Trackers loaded")) {
                    swipeRefreshLayout.setRefreshing(false);

                    try {
                        jsonArrayHangs = jsonObject.getJSONArray("data");
                        jsonArrayUsers = jsonObject.getJSONArray("friends");

                        ArrayList<TrackerResponse> trackerResponses = new ArrayList<>();

                        for (int i = 0; i < jsonArrayHangs.length(); i++) {
                            Tracker tracker = gson.fromJson(jsonArrayHangs.get(i).toString(), Tracker.class);
                            User user = gson.fromJson(jsonArrayUsers.get(i).toString(), User.class);
                            TrackerResponse response = new TrackerResponse(tracker, user);
                            trackerResponses.add(response);
                        }

                        populateTrackersArrayListAdapter(trackerResponses);

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
        asynchTaskGet.execute(Constants.GET_TRACKERS + user_id);
    }

    public void init(View view) {
        sharedPreferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh_tracker);
        swipeRefreshLayout.setOnRefreshListener(this);
        listViewTrackers = view.findViewById(R.id.listView_tracker);
        adapter = new TrackersAdapter(getActivity(), R.layout.activity_tracker_list_item, new ArrayList<TrackerResponse>());
        listViewTrackers.setAdapter(adapter);
        listViewTrackers.setOnItemClickListener(onItemClickListener);
    }

    private void showMessage(String message) {
        if (getActivity() != null)
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    public void populateTrackersArrayListAdapter(ArrayList<TrackerResponse> trackerResponses) {
        adapter.clear();
        adapter.addAll(trackerResponses);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onRefresh() {
        getTrackers();
    }

    String trackerUser;
    User tUser;
    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

            //add place info in shared preferences to pass it to next fragment
            TrackerResponse trackerResponse = (TrackerResponse) adapterView.getItemAtPosition(position);
            Tracker tracker = trackerResponse.getTracker();
            User user = trackerResponse.getUser();
            sharedPreferences = getActivity().getSharedPreferences("trackerInfo", Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
            editor.apply();
            trackerUser = tracker.getUser_id();
            String jsonData = gson.toJson(tracker, Tracker.class);
            editor.putString("tracker_details", jsonData);
            String jsonData2 = gson.toJson(user, User.class);
            editor.putString("tracker_user", jsonData2);
            editor.apply();
            Log.e("user",sharedPreferences.getString("tracker_user",""));
            TrackerMap profile = new TrackerMap();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_frame, profile); // give your fragment container id in first parameter
            transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
            transaction.commit();

        }
    };

}
