package com.example.saraelsheemi.pinmate.views;

import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.saraelsheemi.pinmate.R;
import com.example.saraelsheemi.pinmate.controllers.PagerAdapter;
import com.example.saraelsheemi.pinmate.views.Chats;
import com.example.saraelsheemi.pinmate.views.Map;
import com.example.saraelsheemi.pinmate.views.Notifications;
import com.example.saraelsheemi.pinmate.views.UserProfile;

public class Main extends Fragment {
    private TabLayout tabLayout;
    private ViewPager viewPager;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_main,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
           init(view);
        getActivity().setTitle("Main");
    }
    private void init(View view) {

        viewPager =  (ViewPager) view.findViewById(R.id.viewpager);
        setUpViewPager(viewPager);
        tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        setUpTabIcons();

    }
    private void setUpTabIcons(){
        tabLayout.getTabAt(0).setIcon(R.drawable.ic_person_outline_white_48dp);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_place_white_48dp);
        tabLayout.getTabAt(2).setIcon(R.drawable.ic_chat_white_48dp);
        tabLayout.getTabAt(3).setIcon(R.drawable.ic_plus_one_white_48dp);
    }
    private void setUpViewPager(ViewPager viewPager){
        PagerAdapter adapter = new PagerAdapter(getFragmentManager(),getContext());
        adapter.addFragment(new UserProfile(),"one");
        adapter.addFragment(new Map(),"one");
        adapter.addFragment(new Chats(),"one");
        adapter.addFragment(new Notifications(),"one");
        viewPager.setAdapter(adapter);
    }
}