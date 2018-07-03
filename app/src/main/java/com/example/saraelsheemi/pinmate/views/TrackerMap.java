package com.example.saraelsheemi.pinmate.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.saraelsheemi.pinmate.R;
import com.example.saraelsheemi.pinmate.controllers.AsynchTasks.AsynchTaskGet;
import com.example.saraelsheemi.pinmate.controllers.Constants;
import com.example.saraelsheemi.pinmate.controllers.EventListener;
import com.example.saraelsheemi.pinmate.models.Place;
import com.example.saraelsheemi.pinmate.models.Tracker;
import com.example.saraelsheemi.pinmate.models.User;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TrackerMap extends Fragment implements OnMapReadyCallback, GoogleMap.OnPolylineClickListener {
    User user;
    Tracker tracker;
    Place src, dest;
    Gson gson = new Gson();

    Polyline polyline1;
    private static final int COLOR_BLACK_ARGB = 0xff000000;
    private static final int POLYLINE_STROKE_WIDTH_PX = 12;
    Marker srcMarker, dstMarker, userMarker;


    private static final String TAG = MapsFragment.class.getSimpleName();
    private GoogleMap mMap;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.activity_tracker_map, null, false);
        getActivity().setTitle("Map");
        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.trackermap);
        mapFragment.getMapAsync(this);


        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        addUserMarker();
        getTrackerDetails();


    }

    public void getTrackerDetails() {

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("trackerInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.apply();
        String json = sharedPreferences.getString("tracker_details", "");
        tracker = gson.fromJson(json, Tracker.class);
        user = gson.fromJson(sharedPreferences.getString("tracker_user", ""), User.class);

        getSource();
        getDest();

    }

    private void getDest() {
        AsynchTaskGet asynchTaskGet = new AsynchTaskGet(getContext(), new EventListener<String>() {
            @Override
            public void onSuccess(String object) {
                JSONObject jsonObject = null;
                JSONArray jsonArray;
                String message = "";
                Boolean ok = false;

                try {
                    jsonObject = new JSONObject(object);
                    ok = jsonObject.getBoolean("ok");
                    message = jsonObject.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (ok && message.contains("Place loaded")) {
                    try {
                        String json = jsonObject.getString("data");
                        dest = gson.fromJson(json,Place.class);
                        addDstMarker();
                        addPolyLine();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("loading places", "Internal server error.");
            }
        });
        asynchTaskGet.execute(Constants.GET_PLACE + tracker.getDestination());
    }
    private void getSource() {
        AsynchTaskGet asynchTaskGet = new AsynchTaskGet(getContext(), new EventListener<String>() {
            @Override
            public void onSuccess(String object) {
                JSONObject jsonObject = null;
                JSONArray jsonArray;
                String message = "";
                Boolean ok = false;

                try {
                    jsonObject = new JSONObject(object);
                    ok = jsonObject.getBoolean("ok");
                    message = jsonObject.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (ok && message.contains("Place loaded")) {
                    try {
                        String json = jsonObject.getString("data");
                        src = gson.fromJson(json,Place.class);
                        addSrcMarker();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("loading places", "Internal server error.");
            }
        });
        asynchTaskGet.execute(Constants.GET_PLACE + tracker.getSource());
    }


    private void addSrcMarker() {
        if (srcMarker == null) {
            srcMarker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(src.getLocation().getLatitude(), src.getLocation().getLongitude()))
                    .title(src.getName())
                    .snippet(getAddress(src.getLocation().getLatitude(), src.getLocation().getLongitude()))
                    .infoWindowAnchor(0.5f, 0.5f));
                srcMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.savoryplace));

        } else
            srcMarker.setPosition(new LatLng(src.getLocation().getLatitude(), src.getLocation().getLongitude()));


    }
    private void addUserMarker() {
        if (userMarker == null) {
            userMarker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(user.getCurrent_location().getLatitude(), user.getCurrent_location().getLongitude()))
                    .title(user.getName())
                    .snippet(getAddress(user.getCurrent_location().getLatitude(), user.getCurrent_location().getLongitude()))
                    .infoWindowAnchor(0.5f, 0.5f));

            if (user.getGender().equals("male"))
                userMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.manmap));
            else if (user.getGender().equals("female"))
                userMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.womanmap));
        } else
            userMarker.setPosition(new LatLng(user.getCurrent_location().getLatitude(), user.getCurrent_location().getLongitude()));
    }

    private void addDstMarker() {

        if (dstMarker == null) {
            dstMarker = mMap.addMarker(new MarkerOptions()
                    .position(new LatLng(dest.getLocation().getLatitude(), dest.getLocation().getLongitude()))
                    .title(dest.getName())
                    .snippet(getAddress(dest.getLocation().getLatitude(), dest.getLocation().getLongitude()))
                    .infoWindowAnchor(0.5f, 0.5f));
            dstMarker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.savoryplace));

        } else
            dstMarker.setPosition(new LatLng(dest.getLocation().getLatitude(), dest.getLocation().getLongitude()));

    }
    public void addPolyLine() {

        if (src != null && dest != null) {
            polyline1 = mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(src.getLocation().getLatitude(),
                                    src.getLocation().getLongitude()),
                            new LatLng(dest.getLocation().getLatitude(),
                                    dest.getLocation().getLongitude())));
            polyline1.setStartCap(new RoundCap());
            polyline1.setEndCap(new RoundCap());
            polyline1.setWidth(POLYLINE_STROKE_WIDTH_PX);
            polyline1.setColor(COLOR_BLACK_ARGB);
            polyline1.setJointType(JointType.ROUND);
            mMap.setOnPolylineClickListener(this);
            mMap.moveCamera(CameraUpdateFactory
                    .newLatLngZoom(new LatLng(src.getLocation().getLatitude(),src.getLocation().getLongitude()), 15));
        }


    }


    @Override
    public void onPolylineClick(Polyline polyline) {
        Toast.makeText(getContext(),"From "+src.getName() +" to " +dest.getName(), Toast.LENGTH_LONG).show();
    }

    Geocoder geocoder;
    List<Address> addresses;

    private String getAddress(double lat, double lng) {
        geocoder = new Geocoder(getContext());
        try {
            addresses = geocoder.getFromLocation(lat, lng, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String address = addresses.get(0).getAddressLine(0);
        String city = addresses.get(0).getAddressLine(1);

        return address + "" + city;
    }

}
