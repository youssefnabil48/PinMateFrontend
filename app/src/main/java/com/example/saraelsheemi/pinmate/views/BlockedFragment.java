package com.example.saraelsheemi.pinmate.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.example.saraelsheemi.pinmate.R;
import com.example.saraelsheemi.pinmate.models.User;
import com.google.gson.Gson;

import java.util.ArrayList;

public class BlockedFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private ListView listView;
    private ArrayList<String> blockedUsers;
    private ArrayList<User> blocks;
    SharedPreferences sharedPreferences;
    SwipeRefreshLayout swipeRefreshLayout;
    SharedPreferences.Editor editor;
    Gson gson;
    User user;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_blocked_list_view, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        getActivity().setTitle("Blocked Users");
    }

    private void init(View view) {

        listView = view.findViewById(R.id.listView_blocked);
        swipeRefreshLayout.setOnRefreshListener(this);
        sharedPreferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();

    }

    public void getBlocked ()  {

    }
    @Override
    public void onRefresh() {
        getBlocked();
    }
}
