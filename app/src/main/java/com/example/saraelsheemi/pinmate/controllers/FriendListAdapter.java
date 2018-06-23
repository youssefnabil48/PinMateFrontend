package com.example.saraelsheemi.pinmate.controllers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import com.example.saraelsheemi.pinmate.models.User;

import java.util.ArrayList;

public class FriendListAdapter extends ArrayAdapter<User> {

    private int layoutResoureId;
    private ArrayList<User> userArrayList = null;


    public FriendListAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }
}
