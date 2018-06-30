package com.example.saraelsheemi.pinmate.controllers.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.saraelsheemi.pinmate.R;
import com.example.saraelsheemi.pinmate.controllers.MLRoundedImageView;
import com.example.saraelsheemi.pinmate.models.Place;
import com.example.saraelsheemi.pinmate.views.place.FavoritePlacesFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FavoritePlacesAdapter extends ArrayAdapter<Place> {

    FavoritePlacesFragment favoritePlacesFragment;

    public FavoritePlacesAdapter(@NonNull Context context, int resource, ArrayList<Place> places, FavoritePlacesFragment favoritePlace) {
        super(context, resource, places);
        this.favoritePlacesFragment = favoritePlace;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView placeName;
        TextView placeDescription;
        MLRoundedImageView placePicture;
        final ImageButton favoritePlace;

        //convert view == one row
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.activity_favorites_list_item, parent, false);
        }


        placeName = listItemView.findViewById(R.id.txt_fav_place_list_name);
        placeDescription = listItemView.findViewById(R.id.txt_fav_place_list_description);
        placePicture = listItemView.findViewById(R.id.img_fav_place_list_picture);
        favoritePlace = listItemView.findViewById(R.id.imgbtn_favs_places);
        favoritePlace.setTag("yes");


        final Place place = getItem(position);
        placeName.setText(place.getName());
        placeDescription.setText(place.getDecsription());

        favoritePlace.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ImageButton btn = view.findViewById(R.id.imgbtn_favs_places);
                    btn.setImageResource(R.drawable.unfav2);
                    btn.setTag("no");
                    favoritePlacesFragment.unfavoritePlace(place.getId());
            }
        });

        if (place.getPicture() != null)
            Picasso.get().load(place.getPicture()).into(placePicture);


        return listItemView;
    }

}