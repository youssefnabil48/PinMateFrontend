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
import android.widget.Toast;

import com.example.saraelsheemi.pinmate.R;
import com.example.saraelsheemi.pinmate.controllers.AsynchTasks.AsynchTaskGet;
import com.example.saraelsheemi.pinmate.controllers.AsynchTasks.AsynchTaskPost;
import com.example.saraelsheemi.pinmate.controllers.Constants;
import com.example.saraelsheemi.pinmate.controllers.EventListener;
import com.example.saraelsheemi.pinmate.controllers.adapters.RequestsAdapter;
import com.example.saraelsheemi.pinmate.models.HangoutRequest;
import com.example.saraelsheemi.pinmate.models.Place;
import com.example.saraelsheemi.pinmate.models.Request;
import com.example.saraelsheemi.pinmate.models.User;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class RequestsFragment extends Fragment implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {

    private ListView listViewRequests;
    private String user_id;
    private ArrayAdapter<Request> hangoutRequestArrayAdapter;
    SwipeRefreshLayout swipeRefreshLayout;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Gson gson;
    User user;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_req_list_view, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        init(view);
        getHangouts();
        super.onViewCreated(view, savedInstanceState);
    }

    public void init(View view) {
        sharedPreferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh_req);
        swipeRefreshLayout.setOnRefreshListener(this);
        listViewRequests = view.findViewById(R.id.listView_req);
        hangoutRequestArrayAdapter = new RequestsAdapter(getActivity(), R.layout.activity_req_list_item, new ArrayList<Request>(),this);
        listViewRequests.setAdapter(hangoutRequestArrayAdapter);
    }

    private void getHangouts() {
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
                    message = jsonObject.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (ok && message.contains("No requests")) {
                    swipeRefreshLayout.setRefreshing(false);


                } else if (ok && message.contains("Requests loaded")) {
                    swipeRefreshLayout.setRefreshing(false);
                    try {
                        jsonArrayHangs = jsonObject.getJSONArray("data");
                        jsonArrayUsers = jsonObject.getJSONArray("sender");
                        jsonArrayPlaces = jsonObject.getJSONArray("place");
                        ArrayList<Request> requests = new ArrayList<>();

                        for (int i = 0; i < jsonArrayHangs.length(); i++) {
                            HangoutRequest hangoutRequest = gson.fromJson(jsonArrayHangs.get(i).toString(), HangoutRequest.class);
                            User user = gson.fromJson(jsonArrayUsers.get(i).toString(), User.class);
                            Place place = gson.fromJson(jsonArrayPlaces.get(i).toString(), Place.class);
                            Request request = new Request(hangoutRequest,user,place);
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
        asynchTaskGet.execute(Constants.GET_HANGOUTS + user_id);

    }

    private void showMessage(String message) {
        if (getActivity() != null)
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    public void populateFriendsArrayListAdapter(ArrayList<Request> allHangoutsArrayList) {
        hangoutRequestArrayAdapter.clear();
        hangoutRequestArrayAdapter.addAll(allHangoutsArrayList);
        hangoutRequestArrayAdapter.notifyDataSetChanged();
    }

    @Override
    public void onClick(View view) {

    }


    @Override
    public void onRefresh() {
        getHangouts();
    }

    public void respondToRequest(Request request, boolean response) {
        String data="{ \"status\": " + response
                + ",\"request_id\":\""+request.getHangoutRequest().getId() +"\","
                + "\"receiver_id\":\""+user_id+"\"}";

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

                if (ok) {
                        showMessage("Response sent.");
                }
                getHangouts();

            }
            @Override
            public void onFailure(Exception e) {
                    showMessage("Internal server error");
            }
        });
        asynchTaskPost.execute(Constants.RESPOND_HANGOUT_REQUEST);

    }
}