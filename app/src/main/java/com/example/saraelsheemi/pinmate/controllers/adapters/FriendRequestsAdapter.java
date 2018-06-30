package com.example.saraelsheemi.pinmate.controllers.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.saraelsheemi.pinmate.R;
import com.example.saraelsheemi.pinmate.controllers.MLRoundedImageView;
import com.example.saraelsheemi.pinmate.models.FriendRequestResponse;
import com.example.saraelsheemi.pinmate.models.User;
import com.example.saraelsheemi.pinmate.views.user.FriendRequestsFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FriendRequestsAdapter extends ArrayAdapter<FriendRequestResponse> {

    FriendRequestsFragment friendRequestsFragment;

    public FriendRequestsAdapter(@NonNull Context context, int resource, ArrayList<FriendRequestResponse> objects,
                                 FriendRequestsFragment friendRequestsFragment) {
        super(context, resource);
        this.friendRequestsFragment = friendRequestsFragment;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        MLRoundedImageView userImage;
        Button accept, decline;
        TextView userName;


        //convert view == one row
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.activity_friendreq_list_item, parent, false);
        }

        userImage = listItemView.findViewById(R.id.img_frreq_friend);
        accept = listItemView.findViewById(R.id.btn_frreq_accept);
        decline = listItemView.findViewById(R.id.btn_frreq_decline);
        userName = listItemView.findViewById(R.id.txt_frreq_friend_name);


        final FriendRequestResponse request = getItem(position);
        User user = request.getUser();

        userName.setText(user.getName());

        if(user.getPicture() != null )
            Picasso.get().load(user.getPicture()).into(userImage);

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                friendRequestsFragment.respondToRequest(request, true);

            }
        });
        decline = listItemView.findViewById(R.id.btn_frreq_decline);
        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                friendRequestsFragment.respondToRequest(request, false);
            }
        });


        return listItemView;
    }
}

