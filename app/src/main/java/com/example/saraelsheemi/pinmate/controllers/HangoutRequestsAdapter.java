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

    public HangoutRequestsAdapter(@NonNull Context context, ArrayList<HangoutRequest> hangoutRequestArrayList) {

        this.context = context;

        hangoutRequests = hangoutRequestArrayList;
    }

    public HangoutRequestsAdapter(@NonNull Context context, int resource, ArrayList<HangoutRequest> hangoutRequests) {
        super(context, resource);
        this.hangoutRequests = hangoutRequests;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }
//    @Override
//    public View getView(int position, View convertView, ViewGroup parent) {
//
//        View row = convertView;
//        PlaceHolder holder;
//
//        if (row == null) {
//            LayoutInflater inflater = LayoutInflater.from(context);
//            row = inflater.inflate(layoutResoureId, parent, false);
//            holder = new PlaceHolder(row);
//            row.setTag(holder);
//
//        } else {
//            holder = (PlaceHolder) row.getTag();
//        }
//        HangoutRequest hangoutRequest = hangoutRequests.get(position);
//        holder.hangoutTitle.setText(hangoutRequest.getTitle());
//        holder.hangoutDescription.setText(hangoutRequest.getDescription());
//        holder.hangoutDate.setText(hangoutRequest.getDate());
//
//
//        Log.e("row",holder.hangoutTitle.getText().toString());
//
//        return row;
//    }

    /**
     * Provides a view for an AdapterView (ListView, GridView, etc.)
     *
     * @param position The position in the list of data that should be displayed in the
     *                 list item view.
     * @param convertView The recycled view to populate.
     * @param parent The parent ViewGroup that is used for inflation.
     * @return The View for the position in the AdapterView.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //convert view == one row
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.activity_hangouts_list_item, parent, false);
        }

        // Get the {@link AndroidFlavor} object located at this position in the list
        User user = getItem(position);

        // Find the TextView in the list_item.xml layout with the ID version_name
        TextView idTextView = (TextView) listItemView.findViewById(R.id.id);
        // Get the version name from the current AndroidFlavor object and
        // set this text on the name TextView
        idTextView.setText(user.getId());

        // Find the TextView in the list_item.xml layout with the ID version_number
        TextView nameTextView = (TextView) listItemView.findViewById(R.id.name);
        // Get the version number from the current AndroidFlavor object and
        // set this text on the number TextView
        nameTextView.setText(user.getName());

        // Return the whole list item layout (containing 2 TextViews and an ImageView)
        // so that it can be shown in the ListView
        return listItemView;
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
