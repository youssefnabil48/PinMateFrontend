package com.example.saraelsheemi.pinmate.controllers;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.saraelsheemi.pinmate.views.Chats;
import com.example.saraelsheemi.pinmate.views.user.FriendRequestsFragment;
import com.example.saraelsheemi.pinmate.views.user.UserProfile;

public class HomePager extends FragmentPagerAdapter {
    private Context context;

    public HomePager(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new UserProfile();
            case 1:
                return new Chats();
            case 2:
                return new FriendRequestsFragment();

        }
        return null;
    }

        @Override
        public int getCount () {
            return 3;
        }

        @Override
        public CharSequence getPageTitle ( int position){
//            switch (position) {
//                case 0 : return "Profile";
//                case 1 : return "Chats";
//                case 2 : return "Friend Requests";
//            }
            return null;
        }
    }
