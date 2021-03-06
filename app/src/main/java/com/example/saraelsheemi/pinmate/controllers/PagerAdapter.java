package com.example.saraelsheemi.pinmate.controllers;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.saraelsheemi.pinmate.views.place.EventFragment;
import com.example.saraelsheemi.pinmate.views.place.PlaceAboutFragment;
import com.example.saraelsheemi.pinmate.views.place.PlaceProfile;
import com.example.saraelsheemi.pinmate.views.place.PostsFragment;
import com.example.saraelsheemi.pinmate.views.place.ReviewFragment;
import com.example.saraelsheemi.pinmate.views.user.FriendListFragment;
import com.example.saraelsheemi.pinmate.views.user.HangoutRequestsFragment;
import com.example.saraelsheemi.pinmate.views.user.RequestsFragment;
import com.example.saraelsheemi.pinmate.views.user.UserInfoFragment;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sara ElSheemi on 3/5/2018.
 */

public class PagerAdapter extends FragmentPagerAdapter {
    private Context context;

    public PagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new PlaceAboutFragment();
            case 1:
                return new PostsFragment();
            case 2:
                return new ReviewFragment();
            case 3 :
                return new EventFragment();

        }
        return null;
    }

    @Override
    public int getCount () {
        return 4;
    }

    @Override
    public CharSequence getPageTitle ( int position){
        switch (position) {
            case 0 : return "ABOUT";
            case 1 : return "POSTS";
            case 2 : return "REVIEWS";
            case 3 : return "EVENTS";
        }
        return null;
    }
}

