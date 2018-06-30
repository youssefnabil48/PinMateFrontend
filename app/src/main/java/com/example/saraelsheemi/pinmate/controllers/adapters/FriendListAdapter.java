package com.example.saraelsheemi.pinmate.controllers.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.saraelsheemi.pinmate.R;
import com.example.saraelsheemi.pinmate.controllers.MLRoundedImageView;
import com.example.saraelsheemi.pinmate.models.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


//set onclicklistener for each row

public class FriendListAdapter extends ArrayAdapter<User> {


    public FriendListAdapter(@NonNull Context context, int resource, ArrayList<User> users) {
        super(context, resource,users);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView friendName;
        MLRoundedImageView friendPicture;

        //convert view == one row
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.activity_friends_list_item, parent, false);
        }


        friendName = listItemView.findViewById(R.id.txt_notf_friend_name);
        friendPicture = listItemView.findViewById(R.id.img_notf_friend);

        User user =getItem(position);
        friendName.setText(user.getName());
        if(user.getPicture() != null)
        Picasso.get().load(user.getPicture()).into(friendPicture);
        else if(user.getGender().contains("female"))
            Picasso.get().load(R.drawable.female_user_profile).into(friendPicture);
        else if(user.getGender().contains("male"))
            Picasso.get().load(R.drawable.male_user_picture).into(friendPicture);


        return listItemView;
    }

}
