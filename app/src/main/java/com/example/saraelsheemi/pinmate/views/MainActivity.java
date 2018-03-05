package com.example.saraelsheemi.pinmate.views;

import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

import com.example.saraelsheemi.pinmate.R;
import com.example.saraelsheemi.pinmate.controllers.PagerAdapter;

public class MainActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setUpViewPager(viewPager);
        tabLayout = (TabLayout) findViewById(R.id.tabs);
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
        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(),getApplicationContext());
        adapter.addFragment(new UserProfile(),"one");
        adapter.addFragment(new Map(),"one");
        adapter.addFragment(new Chats(),"one");
        adapter.addFragment(new Notifications(),"one");
        viewPager.setAdapter(adapter);
    }

}
