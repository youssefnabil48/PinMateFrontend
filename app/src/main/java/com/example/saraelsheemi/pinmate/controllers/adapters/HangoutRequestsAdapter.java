package com.example.saraelsheemi.pinmate.controllers.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.saraelsheemi.pinmate.R;
import com.example.saraelsheemi.pinmate.controllers.MLRoundedImageView;
import com.example.saraelsheemi.pinmate.models.HangoutRequest;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HangoutRequestsAdapter extends ArrayAdapter<HangoutRequest> {


    public HangoutRequestsAdapter(@NonNull Context context, int resource, ArrayList<HangoutRequest> hangoutRequests) {
        super(context, resource,hangoutRequests);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView hangoutTitle;
        TextView hangoutDescription;
        TextView hangoutDate;
        //convert view == one row
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.activity_hangouts_list_item, parent, false);
        }

        // Get the {@link AndroidFlavor} object located at this position in the list
        HangoutRequest hangoutRequest = getItem(position);


        hangoutTitle = listItemView.findViewById(R.id.txt_hangout_title);
        hangoutDescription = listItemView.findViewById(R.id.txt_hangout_description);
        hangoutDate = listItemView.findViewById(R.id.txt_post_date);
        MLRoundedImageView mlRoundedImageView = listItemView.findViewById(R.id.hngoutpic);
        Picasso.get().load(R.drawable.reunion).into(mlRoundedImageView);


        hangoutTitle.setText(hangoutRequest.getTitle());
        hangoutDescription.setText(hangoutRequest.getDescription());
        hangoutDate.setText(hangoutRequest.getDate());


        return listItemView;
    }


}
