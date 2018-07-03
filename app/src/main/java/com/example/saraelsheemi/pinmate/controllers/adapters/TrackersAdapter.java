package com.example.saraelsheemi.pinmate.controllers.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.saraelsheemi.pinmate.R;
import com.example.saraelsheemi.pinmate.controllers.MLRoundedImageView;
import com.example.saraelsheemi.pinmate.models.Tracker;
import com.example.saraelsheemi.pinmate.models.TrackerResponse;
import com.example.saraelsheemi.pinmate.models.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TrackersAdapter  extends ArrayAdapter<TrackerResponse> {


    public TrackersAdapter(@NonNull Context context, int resource, ArrayList<TrackerResponse> trackerResponses) {
        super(context, resource, trackerResponses);

    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView userName;
        TextView src;
        TextView dst;
        MLRoundedImageView userPicture;
        ImageView openTracker;


        //convert view == one row
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.activity_tracker_list_item, parent, false);
        }

        userName = listItemView.findViewById(R.id.txt_tracker_friend_name);
        src = listItemView.findViewById(R.id.tracker_src);
        dst = listItemView.findViewById(R.id.tracker_dst);
        userPicture = listItemView.findViewById(R.id.img_tracker_friend);
        openTracker= listItemView.findViewById(R.id.open);
        TrackerResponse trackerResponse = getItem(position);
        User user = trackerResponse.getUser();
        Tracker tracker = trackerResponse.getTracker();

        userName.setText(user.getName());
        src.setText("Created at: " + tracker.getCreated_at());

        if(user.getPicture() != null)
            Picasso.get().load(user.getPicture()).into(userPicture);



        return listItemView;
    }


}

