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
import com.example.saraelsheemi.pinmate.models.HangoutRequest;
import com.example.saraelsheemi.pinmate.models.Place;
import com.example.saraelsheemi.pinmate.models.Request;
import com.example.saraelsheemi.pinmate.models.User;
import com.example.saraelsheemi.pinmate.views.user.RequestsFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class RequestsAdapter extends ArrayAdapter<Request>{

    RequestsFragment requestsFragment;

    public RequestsAdapter(@NonNull Context context, int resource, ArrayList<Request> objects, RequestsFragment requestsFragment) {
        super(context, resource);
        this.requestsFragment = requestsFragment;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImageView userImage;
        Button accept, decline;
        TextView userName, date, placeName, at, title;


        //convert view == one row
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.activity_req_list_item, parent, false);
        }

        userImage = listItemView.findViewById(R.id.img_notf_friend);
        accept = listItemView.findViewById(R.id.btn_notf_accept);
        decline = listItemView.findViewById(R.id.btn_notf_decline);
        userName = listItemView.findViewById(R.id.txt_notf_friend_name);
        placeName = listItemView.findViewById(R.id.txt_notf_req_place);
        title = listItemView.findViewById(R.id.txt_notf_req_title);
        at = listItemView.findViewById(R.id.txt_notf_req_at);
        date = listItemView.findViewById(R.id.txt_notf_date);


        final Request request = getItem(position);

        HangoutRequest hangoutRequest = request.getHangoutRequest();
        User user = request.getUser();
        Place place = request.getPlace();
        title.setText(hangoutRequest.getTitle());

        date.setText("Date: " + hangoutRequest.getDate());
        if (user.getPicture() != null)
            Picasso.get().load(user.getPicture()).into(userImage);
        userName.setText("By: " + user.getName());
        if (place != null)
            placeName.setText(place.getName());

        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestsFragment.respondToRequest(request, true);

            }
        });
        decline = listItemView.findViewById(R.id.btn_notf_decline);
        decline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                requestsFragment.respondToRequest(request,false);
            }
        });




        return listItemView;
    }


}