package com.example.saraelsheemi.pinmate.controllers.adapters;

import com.example.saraelsheemi.pinmate.R;
import com.example.saraelsheemi.pinmate.controllers.MLRoundedImageView;
import com.example.saraelsheemi.pinmate.models.Message;
import com.example.saraelsheemi.pinmate.models.User;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
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
    SharedPreferences sharedPreferences;
    Gson gson;
    User loggedInUser;
    SharedPreferences.Editor editor;
    ArrayList<Message> messages;
    public MessagesAdapter(Activity context,int resource, ArrayList<Message> messages) {
        super(context, resource, messages);
        sharedPreferences = getContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
        String json = sharedPreferences.getString("user_info","");
        gson = new Gson();
        loggedInUser = gson.fromJson(json,User.class);
        this.messages = messages;
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
        Message message = getItem(position);
        if(listItemView == null) {
            if(message.getSenderId().equals(loggedInUser.getId())){
                listItemView = LayoutInflater.from(getContext()).inflate(R.layout.message_row_send, parent, false);
            }else {
                listItemView = LayoutInflater.from(getContext()).inflate(R.layout.message_row_receive, parent, false);
               // TextView senderNameView = listItemView.findViewById(R.id.sender_name);
                MLRoundedImageView senderPic = listItemView.findViewById(R.id.image_message_profile);
                if(message.getSender().getPicture() != null)
                    Picasso.get().load(message.getSender().getPicture()).into(senderPic);
              //  senderNameView.setText(message.getSender().getName());

            }
            TextView contentView = listItemView.findViewById(R.id.message_content);
            contentView.setText(message.getContent());

            TextView dataView = listItemView.findViewById(R.id.text_message_time);
            dataView.setText(message.getCreatedAt().substring(11, 16));

        }
        return listItemView;
    }

    public void refreshMessages(ArrayList<Message> messages) {
        this.messages.clear();
        this.messages.addAll(messages);
        this.notifyDataSetChanged();
    }

}