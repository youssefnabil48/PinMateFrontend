package com.example.saraelsheemi.pinmate.controllers;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.saraelsheemi.pinmate.R;
import com.example.saraelsheemi.pinmate.models.Place;
import com.example.saraelsheemi.pinmate.models.SearchItem;
import com.example.saraelsheemi.pinmate.models.User;
import com.example.saraelsheemi.pinmate.views.place.AllPlacesFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class SearchAdapter extends ArrayAdapter<SearchItem> {

    public SearchAdapter (Activity context, ArrayList<SearchItem> searchItems) {
        super(context, 0, searchItems);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //convert view == one row
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.activity_searchable_item, parent, false);
        }

        SearchItem item = getItem(position);

        TextView idTextView = listItemView.findViewById(R.id.search_item_id);
        idTextView.setText(item.getId());

        TextView typeTextView = listItemView.findViewById(R.id.search_item_type);
        typeTextView.setText(item.getItemType());

        TextView nameTextView = listItemView.findViewById(R.id.search_item_name);
        nameTextView.setText(item.getName());

        TextView hintTextView = listItemView.findViewById(R.id.search_item_hint);
        hintTextView.setText(item.getHint());

        MLRoundedImageView itemPicture = listItemView.findViewById(R.id.search_item_picture);
        if (item.getPicURL() != null)
            Picasso.get().load(item.getPicURL()).into(itemPicture);

        return listItemView;

    }
}
