package com.example.saraelsheemi.pinmate.controllers.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.saraelsheemi.pinmate.R;
import com.example.saraelsheemi.pinmate.controllers.MLRoundedImageView;
import com.example.saraelsheemi.pinmate.controllers.OnlineUsersAdapter;
import com.example.saraelsheemi.pinmate.models.Event;
import com.example.saraelsheemi.pinmate.models.Post;
import com.example.saraelsheemi.pinmate.models.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class EventsAdapter extends ArrayAdapter<Event> {
    private static final String LOG_TAG = EventsAdapter.class.getSimpleName();

    public EventsAdapter(Activity context, ArrayList<Event> events) {
        super(context, 0, events);
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        //convert view == one row
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if(listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.activity_event_list_item, parent, false);
        }
        Event event = getItem(position);

        TextView nameTextView = listItemView.findViewById(R.id.event_name);
        nameTextView.setText(event.getName());


        TextView descTextView = listItemView.findViewById(R.id.description);
        descTextView.setText(event.getDescription());

        TextView startDateTextView = listItemView.findViewById(R.id.start_date);
        startDateTextView.setText(event.getStart_date());

        MLRoundedImageView mlRoundedImageView = listItemView.findViewById(R.id.eventpic);
        Picasso.get().load(R.drawable.fireworks).into(mlRoundedImageView);

        TextView endDateTextView = listItemView.findViewById(R.id.end_date);
        endDateTextView.setText(event.getEnd_date());

        return listItemView;
    }
}
