package com.example.saraelsheemi.pinmate.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
import com.example.saraelsheemi.pinmate.controllers.HangoutRequestsAdapter;
import com.example.saraelsheemi.pinmate.models.HangoutRequest;
import com.example.saraelsheemi.pinmate.models.User;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class HangoutRequestsFragment extends Fragment {

    private ListView listViewHangouts;
    private ArrayList<HangoutRequest> hangoutRequests;
    private String user_id;
    private ArrayAdapter<HangoutRequest> hangoutRequestArrayAdapter;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressBar progressBar;
    Gson gson;
    User user;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_hangouts_list_view, container, false);

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
        hangoutRequests = new ArrayList<>();
        progressBar = view.findViewById(R.id.progressbar_loading_hangouts);
        listViewHangouts = view.findViewById(R.id.listView_hangouts);
        listViewHangouts.setOnItemClickListener(onItemClickListener);


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
                progressBar.setVisibility(View.INVISIBLE);
                JSONObject jsonObject = null;
                JSONArray jsonArray = null;
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
                    showMessage("No Requests yet.");

                } else if (ok && message.contains("Requests loaded")) {
                    showMessage("success");
                    try {
                        jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            HangoutRequest hangoutRequest = gson.fromJson(jsonArray.get(0).toString(), HangoutRequest.class);
                            hangoutRequests.add(hangoutRequest);
                        }
                        hangoutRequestArrayAdapter = new HangoutRequestsAdapter(getActivity(), R.layout.activity_hangouts_list_item, hangoutRequests);
                        listViewHangouts.setAdapter(hangoutRequestArrayAdapter);
                        showMessage(hangoutRequestArrayAdapter.getCount()+"");

                        //   populateFriendsArrayListAdapter(hangoutRequests);

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
        asynchTaskGet.execute(Constants.GET_USER_HANGOUTS + user_id);

    }

    public void populateFriendsArrayListAdapter(ArrayList<HangoutRequest> allHangoutsArrayList) {
        hangoutRequestArrayAdapter.addAll(allHangoutsArrayList);
        hangoutRequestArrayAdapter.notifyDataSetChanged();
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
