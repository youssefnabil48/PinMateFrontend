package com.example.saraelsheemi.pinmate.views.place;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saraelsheemi.pinmate.R;
import com.example.saraelsheemi.pinmate.controllers.MLRoundedImageView;
import com.example.saraelsheemi.pinmate.controllers.PagerAdapter;
import com.example.saraelsheemi.pinmate.models.Place;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

public class PlaceProfile extends Fragment {
    MLRoundedImageView placePicture;
    ImageView placeCoverPicture;
    TextView placeName;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Gson gson;
    Place place;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       getActivity().setTitle("Profile");
        return inflater.inflate(R.layout.activity_place_profile,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        init(view);
    }
    private void init(View view) {

        placeCoverPicture = view.findViewById(R.id.img_place_cover);
        placePicture = view.findViewById(R.id.imground_place_profile);
        placeName = view.findViewById(R.id.txt_fav_place_list_name);
        viewPager =   view.findViewById(R.id.place_viewpager);
        setUpViewPager(viewPager);
        tabLayout =  view.findViewById(R.id.place_tabs);
        tabLayout.setupWithViewPager(viewPager);
        setUpTabIcons();
        sharedPreferences = getActivity().getSharedPreferences("placeInfo", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
        getUserInfo();

    }

    private void getUserInfo() {
        String json = sharedPreferences.getString("place_details","");
        gson = new Gson();
        place = gson.fromJson(json,Place.class);
        placeName.setText(place.getName());
        if(place.getPicture()!=null)
            Picasso.get().load(place.getPicture()).into(placePicture);

        Picasso.get().load("https://www.whcc.com/assets/ee/uploads/images/cover-options/whcc_covers_large_leather_white.jpg")
                .into(placeCoverPicture);

    }
    private void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
    private void setUpTabIcons(){
        tabLayout.getTabAt(0).setText("ABOUT");
        tabLayout.getTabAt(1).setText("POSTS");
        tabLayout.getTabAt(2).setText("REVIEWS");
        tabLayout.getTabAt(3).setText("EVENTS");
    }
    private void setUpViewPager(ViewPager viewPager){
        PagerAdapter adapter = new PagerAdapter(getFragmentManager(),getContext());
        adapter.addFragment(new PlaceAboutFragment(),"one");
        adapter.addFragment(new PostsFragment(),"one");
        adapter.addFragment(new ReviewFragment(),"one");
        adapter.addFragment(new EventFragment(),"one");
        viewPager.setAdapter(adapter);
    }
}

