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
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.saraelsheemi.pinmate.controllers.AsynchTasks.AsynchTaskPut;
import com.example.saraelsheemi.pinmate.controllers.Constants;
import com.example.saraelsheemi.pinmate.controllers.EventListener;
import com.example.saraelsheemi.pinmate.models.User;
import com.example.saraelsheemi.pinmate.views.place.AllPlacesFragment;
import com.example.saraelsheemi.pinmate.views.place.FavoritePlacesFragment;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.messaging.RemoteMessage;
import com.google.gson.Gson;
import com.pusher.pushnotifications.PushNotificationReceivedListener;
import com.pusher.pushnotifications.PushNotifications;

import com.example.saraelsheemi.pinmate.R;

import org.json.JSONException;
import org.json.JSONObject;

public class Home extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    DrawerLayout drawer;
    NotificationManager notificationManager;
    NotificationChannel notificationChannel;
    Toolbar toolbar;
    NavigationView navigationView;
    SearchView searchView;
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
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        searchView = findViewById(R.id.search_bar);

        //setup notification
        notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        if (Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            notificationChannel = new NotificationChannel(Constants.CHANNEL_ID, Constants.CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            notificationChannel.setDescription(Constants.CHANNEL_DESCRIPTION);
            notificationChannel.enableLights(true);
            notificationChannel.setLightColor(Color.RED);
            notificationManager.createNotificationChannel(notificationChannel);
        }
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d("myfirebaseinstance", "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();     // Close keyboard on pressing IME_ACTION_SEARCH option
                Intent intent = new Intent(getApplicationContext(), SearchableActivity.class);
                intent.putExtra("query", query);
                startActivity(intent);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                //showMessage(newText);
                return false;
            }
        });
    }


    private void sendRegistrationToServer(String refreshedToken) {
        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.apply();
        Gson gson = new Gson();
        String json = sharedPreferences.getString("user_info", "");
        User user = gson.fromJson(json, User.class);

        String token = "{\"notification_token\":\"" + refreshedToken + "\"}";

        AsynchTaskPut asynchTaskPut = new AsynchTaskPut(token, getApplicationContext(), new EventListener<String>() {
            @Override
            public void onSuccess(String object) {
                JSONObject jsonObject;
                Boolean ok = false;
                try {
                    jsonObject = new JSONObject(object);
                    ok = jsonObject.getBoolean("ok");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (ok) {
                    Log.e("Update", "notification updated successfully");
                } else {
                    Log.e("Update failed", "notification not updated");
                }
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("Update failed", "server error");
            }
        });
        asynchTaskPut.execute(Constants.UPDATE_USER + user.getId());
    }

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

        } else if (id == R.id.nav_map) {
            fragment = new MapsFragment();

        } else if (id == R.id.nav_active_trackers) {
            fragment = new ActiveTrackersFragment();

        } else if (id == R.id.nav_settings) {
            fragment = new AccountSettingsFragment();

        } else if (id == R.id.nav_fav_places) {
            fragment = new FavoritePlacesFragment();
        } else if (id == R.id.nav_logout) {
            clearSharedPreferences();
            Intent intent = new Intent(this, Login.class);
            startActivity(intent);
            finish();
        }
        if (fragment != null) {
            FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.content_frame, fragment);
            //  ft.addToBackStack(null);
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

    private void clearSharedPreferences() {
        SharedPreferences sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }

}
