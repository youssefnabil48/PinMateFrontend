package com.example.saraelsheemi.pinmate.controllers;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.saraelsheemi.pinmate.views.user.FFListFragment;
import com.example.saraelsheemi.pinmate.views.user.FriendInfoFragment;
import com.example.saraelsheemi.pinmate.views.user.FriendListFragment;
import com.example.saraelsheemi.pinmate.views.user.UserInfoFragment;

public class FriendPager   extends FragmentPagerAdapter {
    private Context context;

    public FriendPager(FragmentManager fm, Context context) {
        super(fm);
        this.context = context;
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: {
                return new FriendInfoFragment();
            }
            case 1: {
                return new FFListFragment();
            }
        }
        return null;
    }

    @Override
    public int getCount () {
        return 2;
    }

    @Override
    public CharSequence getPageTitle ( int position){
            switch (position) {
                case 0 : return "BIO";
                case 1 : return "FRIENDS";
            }
        return null;
    }
}

