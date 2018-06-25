package com.example.saraelsheemi.pinmate.controllers;

import com.example.saraelsheemi.pinmate.R;
import com.example.saraelsheemi.pinmate.models.Message;
import com.example.saraelsheemi.pinmate.models.User;

import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/*
 * {@link AndroidFlavorAdapter} is an {@link ArrayAdapter} that can provide the layout for each list
 * based on a data source, which is a list of {@link AndroidFlavor} objects.
 * */
public class MessagesAdapter extends ArrayAdapter<Message> {

    //private static final String LOG_TAG = MessagesAdapter.class.getSimpleName();

    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the list is the data we want
     * to populate into the lists.
     *
     * @param context        The current context. Used to inflate the layout file.
     * @param messages A List of AndroidFlavor objects to display in a list
     */
    //to dont use super
    // activity context
    //arraylist kk
    public MessagesAdapter(Activity context,int resource, ArrayList<Message> messages) {

        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, resource, messages);
        //context=context
        //kk==androidflavours
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
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.message_row, parent, false);
        }

        Message message = getItem(position);
        Log.e("message contnt", message.getContent());
        TextView senderNameView = listItemView.findViewById(R.id.sender_name);
        senderNameView.setText(message.getSenderId());

        TextView contentView = listItemView.findViewById(R.id.message);
        contentView.setText(message.getContent());

        return listItemView;
    }

}