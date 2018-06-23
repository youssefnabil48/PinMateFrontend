package com.example.saraelsheemi.pinmate.controllers;

import android.content.Context;
import android.support.annotation.NonNull;
import android.widget.ArrayAdapter;

import com.example.saraelsheemi.pinmate.models.User;

public class FriendListAdapter extends ArrayAdapter<User> {


    public FriendListAdapter(@NonNull Context context, int resource) {
        super(context, resource);
    }
}
