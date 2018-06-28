package com.example.saraelsheemi.pinmate.views.place;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.saraelsheemi.pinmate.R;
import com.example.saraelsheemi.pinmate.models.Place;
import com.google.gson.Gson;

public class PlaceAboutFragment extends Fragment {
    TextView description, address, phone;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Gson gson;
    Place place;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Profile");
        return inflater.inflate(R.layout.activity_place_about, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        init(view);
    }

    private void init(View view) {
        description = view.findViewById(R.id.txt_placep_name);
        address = view.findViewById(R.id.txt_place_address);
        phone = view.findViewById(R.id.txt_place_phone);
        sharedPreferences = getActivity().getSharedPreferences("placeInfo", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
        getPlaceInfo();
        gson = new Gson();
    }
    private void getPlaceInfo() {
        String json = sharedPreferences.getString("place_details","");
        gson = new Gson();
        place = gson.fromJson(json,Place.class);
        description.setText(place.getDecsription());
        address.setText(place.getAddress());
        phone.setText(place.getMobile_number());
    }

}
