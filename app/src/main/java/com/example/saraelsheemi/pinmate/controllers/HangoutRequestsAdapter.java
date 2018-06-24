package com.example.saraelsheemi.pinmate.controllers;

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
import com.example.saraelsheemi.pinmate.models.HangoutRequest;
import com.example.saraelsheemi.pinmate.models.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class HangoutRequestsAdapter extends ArrayAdapter<HangoutRequest> {

    Context context;
    private int layoutResoureId;
    public ArrayList<HangoutRequest> hangoutRequests = null;

    public HangoutRequestsAdapter(@NonNull Context context, int resource, ArrayList<HangoutRequest> hangoutRequestArrayList) {
        super(context, resource);
        this.context = context;
        layoutResoureId = resource;
        hangoutRequests = hangoutRequestArrayList;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View row = convertView;
        PlaceHolder holder;

        if (row == null) {
            LayoutInflater inflater = LayoutInflater.from(context);
            row = inflater.inflate(layoutResoureId, parent, false);
            holder = new PlaceHolder(row);
            row.setTag(holder);

        } else {
            holder = (PlaceHolder) row.getTag();
        }
        HangoutRequest hangoutRequest = hangoutRequests.get(position);
        holder.hangoutTitle.setText(hangoutRequest.getTitle());
        holder.hangoutDescription.setText(hangoutRequest.getDescription());
        holder.hangoutDate.setText(hangoutRequest.getDate());


        Log.e("row",holder.hangoutTitle.getText().toString());

        return row;
    }




    private static class PlaceHolder {
        TextView hangoutTitle;
        TextView hangoutDescription;
        TextView hangoutDate;


        public PlaceHolder(View view) {
            hangoutTitle = view.findViewById(R.id.txt_hangout_title);
            hangoutDescription = view.findViewById(R.id.txt_hangout_description);
            hangoutDate = view.findViewById(R.id.txt_hangout_date);
        }
    }

}
