package com.example.saraelsheemi.pinmate.views;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.saraelsheemi.pinmate.controllers.Constants;
import com.example.saraelsheemi.pinmate.views.place.AllPlacesFragment;
import com.example.saraelsheemi.pinmate.views.place.FavoritePlacesFragment;
import com.google.firebase.messaging.RemoteMessage;
import com.pusher.pushnotifications.PushNotificationReceivedListener;
import com.pusher.pushnotifications.PushNotifications;

import com.example.saraelsheemi.pinmate.R;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawer;
    NotificationManager notificationManager;
    NotificationChannel notificationChannel;
    Toolbar toolbar;
    NavigationView navigationView;
//    HomeTabLayout tabLayout;
//    ViewPager viewPager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        init();

        Fragment fragment = new Main();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.replace(R.id.content_frame, fragment);
        ft.addToBackStack(null);
        ft.commit();

    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void init() {
        //Pusher notification subscription
        PushNotifications.start(getApplicationContext(), "27e97326-f21c-4a92-8713-1dda5cbc88e3");
        PushNotifications.subscribe("hello");

        setTitle("Home");
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        //setup notification
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(Constants.CHANNEL_ID, Constants.CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription(Constants.CHANNEL_DESCRIPTION);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationManager.createNotificationChannel(notificationChannel);
        }

//        viewPager =  findViewById(R.id.viewpager);
//        setUpViewPager(viewPager);
//        tabLayout = findViewById(R.id.tabs);
//        tabLayout.setupWithViewPager(viewPager);
//        setUpTabIcons();
    }

    //    private void setUpTabIcons(){
//        tabLayout.getTabAt(0).setIcon(R.drawable.ic_person_outline_white_48dp);
//        tabLayout.getTabAt(1).setIcon(R.drawable.ic_place_white_48dp);
//        tabLayout.getTabAt(2).setIcon(R.drawable.ic_chat_white_48dp);
//        tabLayout.getTabAt(3).setIcon(R.drawable.ic_plus_one_white_48dp);
//    }
//    private void setUpViewPager(ViewPager viewPager){
//        PagerAdapter adapter = new PagerAdapter(getSupportFragmentManager(),getApplicationContext());
//        adapter.addFragment(new UserProfile(),"one");
//        adapter.addFragment(new Map(),"one");
//        adapter.addFragment(new Chats(),"one");
//        adapter.addFragment(new Notifications(),"one");
//        viewPager.setAdapter(adapter);
//    }
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            if (getSupportFragmentManager().getBackStackEntryCount() == 0) {
                finish();
            } else {
                getSupportFragmentManager().popBackStack();
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        //    getMenuInflater().inflate(R.menu.activity_settings_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                drawer.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        Fragment fragment = null;


        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            fragment = new CameraFragment();
        } else if (id == R.id.nav_home) {
            fragment = new Main();
//            Intent intent = new Intent(this,HomeTabLayout.class);
//            startActivity(intent);
        } else if (id == R.id.nav_place) {
            fragment = new AllPlacesFragment();

        } else if (id == R.id.nav_trackers) {

        } else if (id == R.id.nav_map) {
            fragment = new MapsFragment();

        } else if (id == R.id.nav_settings) {
            fragment = new AccountSettingsFragment();

        } else if (id == R.id.nav_fav_places) {
            fragment = new FavoritePlacesFragment();
        } else if (id == R.id.nav_logout) {
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
        }
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            ft.addToBackStack(null);
            ft.commit();
            item.setChecked(true);
        }
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();

        PushNotifications.setOnMessageReceivedListenerForVisibleActivity(this, new PushNotificationReceivedListener() {
            @Override
            public void onMessageReceived(RemoteMessage remoteMessage) {
                String messagePayload = remoteMessage.getData().get("myMessagePayload");
                if (messagePayload == null) {
                    // Message payload was not set for this notification
                    Log.i("MyActivity", "Payload was missing");
                } else {
                    Log.i("MyActivity", messagePayload);
                    // Now update the UI based on your message payload!
                }
            }
        });
    }

    private boolean checkIfLoggedIn(){
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        if (sharedPreferences.getString("logged_in", "").contains("true"))
            return true;
        return false;
    }
}
