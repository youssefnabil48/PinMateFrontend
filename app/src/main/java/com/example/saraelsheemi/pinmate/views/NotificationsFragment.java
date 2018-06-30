package com.example.saraelsheemi.pinmate.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.saraelsheemi.pinmate.R;
import com.example.saraelsheemi.pinmate.controllers.HangoutRequestsAdapter;
import com.example.saraelsheemi.pinmate.models.FriendRequest;
import com.example.saraelsheemi.pinmate.models.HangoutRequest;
import com.example.saraelsheemi.pinmate.models.User;
import com.google.gson.Gson;

import java.util.ArrayList;

public class NotificationsFragment extends Fragment {

    private ListView listViewNotfs;
    private ArrayList<HangoutRequest> hangoutRequests;
    private ArrayList<FriendRequest> friendRequests;
    private String user_id;
   // private ArrayAdapter<HangoutRequest> hangoutRequestArrayAdapter;
    private
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressBar progressBar;
    Gson gson;
    User user;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_notf_list_view, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        init(view);
        super.onViewCreated(view, savedInstanceState);
    }

    public void init(View view) {
        sharedPreferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
        hangoutRequests = new ArrayList<>();
        friendRequests = new ArrayList<>();
        listViewNotfs = view.findViewById(R.id.listView_notfs);
      //  listViewNotfs.setOnItemClickListener(onItemClickListener);
    //    hangoutRequestArrayAdapter = new HangoutRequestsAdapter(getActivity(), R.layout.activity_hangouts_list_item, new ArrayList<HangoutRequest>());
     //   listViewNotfs.setAdapter(hangoutRequestArrayAdapter);


    }

}
