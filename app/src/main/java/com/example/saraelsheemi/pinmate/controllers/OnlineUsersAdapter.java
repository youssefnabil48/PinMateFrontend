package com.example.saraelsheemi.pinmate.controllers;

import com.example.saraelsheemi.pinmate.R;
import com.example.saraelsheemi.pinmate.models.User;
import com.squareup.picasso.Picasso;

import android.app.Activity;
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
public class OnlineUsersAdapter extends ArrayAdapter<User> {

    private static final String LOG_TAG = OnlineUsersAdapter.class.getSimpleName();

    /**
     * This is our own custom constructor (it doesn't mirror a superclass constructor).
     * The context is used to inflate the layout file, and the list is the data we want
     * to populate into the lists.
     *
     * @param context        The current context. Used to inflate the layout file.
     * @param onlineUsers A List of AndroidFlavor objects to display in a list
     */
    //to dont use super
    // activity context
    //arraylist kk
    public OnlineUsersAdapter(Activity context, ArrayList<User> onlineUsers) {

        // Here, we initialize the ArrayAdapter's internal storage for the context and the list.
        // the second argument is used when the ArrayAdapter is populating a single TextView.
        // Because this is a custom adapter for two TextViews and an ImageView, the adapter is not
        // going to use this second argument, so it can be any value. Here, we used 0.
        super(context, 0, onlineUsers);
        //context=context
        //kk==androidflavours
    }

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
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.online_users_row, parent, false);
        }

        User user = getItem(position);

        TextView idTextView = (TextView) listItemView.findViewById(R.id.id);
        idTextView.setText(user.getId());


        TextView nameTextView = listItemView.findViewById(R.id.user_name);
        nameTextView.setText(user.getName());
        MLRoundedImageView userImage = listItemView.findViewById(R.id.user_image);
        if(user.getPicture()!=null)
            Picasso.get().load(user.getPicture()).into(userImage);

        return listItemView;
    }

}
