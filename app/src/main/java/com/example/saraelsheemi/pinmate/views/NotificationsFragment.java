package com.example.saraelsheemi.pinmate.views;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.saraelsheemi.pinmate.R;
import com.example.saraelsheemi.pinmate.models.FriendRequest;
import com.example.saraelsheemi.pinmate.models.HangoutRequest;
import com.example.saraelsheemi.pinmate.models.User;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Objects;

public class NotificationsFragment extends Fragment {

    private ListView listViewNotfs;
    private ArrayList<HangoutRequest> hangoutRequests;
    private ArrayList<FriendRequest> friendRequests;
    private String user_id;
    private ArrayAdapter<Objects> notfAdapter;
    private
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressBar progressBar;
    Gson gson;
    User user;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_req_list_view, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
      //  init(view);
        super.onViewCreated(view, savedInstanceState);
    }

//    public void init(View view) {
//        sharedPreferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
//        editor = sharedPreferences.edit();
//        editor.apply();
//        hangoutRequests = new ArrayList<>();
//        friendRequests = new ArrayList<>();
//        listViewNotfs = view.findViewById(R.id.listView_notfs);
//      //  listViewNotfs.setOnItemClickListener(onItemClickListener);
//       notfAdapter = new RequestsAdapter(getActivity(), R.layout.activity_req_list_item, new ArrayList<Objects>());
//       listViewNotfs.setAdapter(notfAdapter);
//
//
//    }

//    private User getUser(String user_id) {
//        gson = new Gson();
//
//
//        AsynchTaskGet asynchTaskGet = new AsynchTaskGet(getContext(), new EventListener<String>() {
//            @Override
//            public void onSuccess(String object) {
//                JSONObject jsonObject = null;
//                String message = "";
//                JSONArray jsonArray;
//                Boolean ok = false;
//
//                try {
//                    jsonObject = new JSONObject(object);
//                    ok = jsonObject.getBoolean("ok");
//                    message = jsonObject.getString("message");
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//                if (ok && message.contains("User found")) {
//                    try {
//                        String data  = jsonObject.getString("data");
//                        user = gson.fromJson(data, User.class);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//
//                } else if (ok && message.contains("No friends")) {
//                    Log.e("Get friends", "No friends found");
//                }
//            }
//
//            @Override
//            public void onFailure(Exception e) {
//                Log.e("Get friends", "internal server error");
//            }
//        });
//
//        asynchTaskGet.execute(Constants.GET_USER + user_id);
//
//
//    }
//



}
