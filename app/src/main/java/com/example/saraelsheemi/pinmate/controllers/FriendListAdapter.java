package com.example.saraelsheemi.pinmate.controllers;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;


import com.example.saraelsheemi.pinmate.R;
import com.example.saraelsheemi.pinmate.models.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


//set onclicklistener for each row

public class FriendListAdapter extends ArrayAdapter<User> {

    Context context;
    private int layoutResoureId;
    private ArrayList<User> userArrayList = null;

    public FriendListAdapter(@NonNull Context context, int resource, ArrayList<User> users) {
        super(context, resource);
        this.context = context;
        layoutResoureId = resource;
        userArrayList = users;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View row = convertView;
        final PlaceHolder holder;

        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            row = inflater.inflate(layoutResoureId, parent, false);
            holder = new PlaceHolder(row);
            row.setTag(holder);

        } else {
            holder = (PlaceHolder) row.getTag();
        }
        User user = userArrayList.get(position);
        holder.friendName.setText(user.getName());
        if(user.getPicture() != null)
        Picasso.get().load(user.getPicture()).into(holder.friendPicture);
        Log.e("row",holder.friendName.getText().toString());

        return row;
    }




    private static class PlaceHolder {
        TextView friendName;
        ImageView friendPicture;

        public PlaceHolder(View view) {
            friendName = view.findViewById(R.id.txt_friend_nname);
            friendPicture = view.findViewById(R.id.img_friend);
        }
    }

}
