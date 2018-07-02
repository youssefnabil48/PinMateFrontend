package com.example.saraelsheemi.pinmate.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.saraelsheemi.pinmate.R;
import com.example.saraelsheemi.pinmate.controllers.MLRoundedImageView;
import com.example.saraelsheemi.pinmate.controllers.PagerAdapter;
import com.example.saraelsheemi.pinmate.models.Place;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class AnyPlace extends AppCompatActivity {
    MLRoundedImageView placePicture;
    ImageView placeCoverPicture;
    TextView placeName;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Gson gson;
    Place place;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_any_place);
        init();
    }
    private void init() {

        placeCoverPicture = findViewById(R.id.img_place_cover);
        placePicture = findViewById(R.id.imground_place_profile);
        placeName = findViewById(R.id.txt_fav_place_list_name);
        viewPager =   findViewById(R.id.place_viewpager);
        setUpViewPager(viewPager);
        tabLayout =  findViewById(R.id.place_tabs);
        tabLayout.setupWithViewPager(viewPager);
        sharedPreferences = getSharedPreferences("placeInfo", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
        getPlaceInfo();
    }
    private void getPlaceInfo() {
        String json = sharedPreferences.getString("place_details","");
        gson = new Gson();
        place = gson.fromJson(json,Place.class);
        placeName.setText(place.getName());
        if(place.getPicture()!=null)
            Picasso.get().load(place.getPicture()).into(placePicture);

        Picasso.get().load("https://www.whcc.com/assets/ee/uploads/images/cover-options/whcc_covers_large_leather_white.jpg")
                .into(placeCoverPicture);

    }
    private void setUpViewPager(ViewPager viewPager){
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(),getApplicationContext());
        viewPager.setAdapter(adapter);
        viewPager.setOffscreenPageLimit(4);
    }
}
