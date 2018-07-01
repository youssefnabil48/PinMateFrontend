package com.example.saraelsheemi.pinmate.controllers;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.saraelsheemi.pinmate.views.user.FriendListFragment;
import com.example.saraelsheemi.pinmate.views.user.HangoutRequestsFragment;
import com.example.saraelsheemi.pinmate.views.user.RequestsFragment;
import com.example.saraelsheemi.pinmate.views.user.UserInfoFragment;

public class UserProfilePager extends FragmentPagerAdapter {
    private Context context;

    public UserProfilePager(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new UserInfoFragment();
            case 1:
                return new FriendListFragment();
            case 2:
                return new HangoutRequestsFragment();
            case 3 :
                return new RequestsFragment();

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
            case 0 : return "BIO";
            case 1 : return "FRIENDS";
            case 2 : return "HANGOUTS";
            case 3 : return "Requests";
        }
        return null;
    }
}

