package com.example.saraelsheemi.pinmate.controllers;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import com.example.saraelsheemi.pinmate.views.Chats;
import com.example.saraelsheemi.pinmate.views.Map;
import com.example.saraelsheemi.pinmate.views.Notifications;
import com.example.saraelsheemi.pinmate.views.UserProfile;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sara ElSheemi on 3/5/2018.
 */

public class PagerAdapter extends FragmentPagerAdapter {
    private Context context;
    private final List<Fragment> mFragmentList = new ArrayList<>();
    private final List<String> mFragmentTitleList = new ArrayList<>();

    public PagerAdapter(FragmentManager fm, Context context) {
        super(fm);
        this.context=context;
    }

    @Override
    public Fragment getItem(int position) {
        return mFragmentList.get(position);
    }

    @Override
    public int getCount() {
        return mFragmentList.size();
    }
    public void addFragment(Fragment fragment, String title){
        mFragmentList.add(fragment);
        mFragmentTitleList.add(title);
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // to display icons only
        return null;
    }
}
