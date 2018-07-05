package com.example.saraelsheemi.pinmate.views;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.saraelsheemi.pinmate.R;
import com.example.saraelsheemi.pinmate.controllers.AsynchTasks.AsynchTaskDelete;
import com.example.saraelsheemi.pinmate.controllers.AsynchTasks.AsynchTaskGet;
import com.example.saraelsheemi.pinmate.controllers.AsynchTasks.AsynchTaskPost;
import com.example.saraelsheemi.pinmate.controllers.AsynchTasks.AsynchTaskPut;
import com.example.saraelsheemi.pinmate.controllers.Constants;
import com.example.saraelsheemi.pinmate.controllers.EventListener;
import com.example.saraelsheemi.pinmate.models.MLocation;
import com.example.saraelsheemi.pinmate.models.Place;
import com.example.saraelsheemi.pinmate.models.Post;
import com.example.saraelsheemi.pinmate.models.Tracker;
import com.example.saraelsheemi.pinmate.models.User;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.PlaceDetectionClient;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.gson.Gson;
import com.google.maps.android.SphericalUtil;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;


public class MapsFragment extends Fragment implements OnMapReadyCallback,
        GoogleMap.OnMyLocationButtonClickListener, GoogleMap.OnPolylineClickListener {


    private static final String TAG = MapsFragment.class.getSimpleName();
    private GoogleMap mMap;
    private CameraPosition mCameraPosition;

    // The entry points to the Places API.
    private GeoDataClient mGeoDataClient;
    private PlaceDetectionClient mPlaceDetectionClient;

    // The entry point to the Fused Location Provider.
    private FusedLocationProviderClient mFusedLocationProviderClient;

    // A default location (Sydney, Australia) and default zoom to use when location permission is
    // not granted.
    private final LatLng mDefaultLocation = new LatLng(-33.8523341, 151.2106085);
    private static final int DEFAULT_ZOOM = 20;
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1;
    private boolean mLocationPermissionGranted;

    // The geographical location where the device is currently located. That is, the last-known
    // location retrieved by the Fused Location Provider.
    private Location mLastKnownLocation;

    // Keys for storing activity state.
    private static final String KEY_CAMERA_POSITION = "camera_position";
    private static final String KEY_LOCATION = "location";

    //marker settings
    ArrayList<Marker> friendsMarkers;
    ArrayList<Marker> placesMarkers;
    Marker userMarker;

    //update location
    private AsynchTaskPut asynchTaskPut;
    private SharedPreferences sharedPreferences;
    private SharedPreferences.Editor editor;
    private User user;

    //friends locations
    private ArrayList<User> friends;
    private ArrayList<Place> places;
    Gson gson;

    //user address from location
    Geocoder geocoder;
    List<Address> addresses;

    //Tracker
    Button btntracker;
    ArrayList<Place> placesObjects = new ArrayList<>();
    Tracker tracker;

    //check in
    Button btnCheckIn;
    final double CHECK_IN_RADIUS = 15;

    Polyline polyline1;
    private static final int COLOR_BLACK_ARGB = 0xff000000;
    private static final int POLYLINE_STROKE_WIDTH_PX = 12;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        // Retrieve location and camera position from saved instance state.
        if (savedInstanceState != null) {
            mLastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            mCameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
            currentLoc.setLocation(new MLocation());
            currentLoc.getLocation().setLatitude(mLastKnownLocation.getLatitude());
            currentLoc.getLocation().setLongitude(mLastKnownLocation.getLongitude());
        }

        View view = inflater.inflate(R.layout.activity_maps, null, false);
        getActivity().setTitle("Map");


        // Construct a GeoDataClient.
        mGeoDataClient = Places.getGeoDataClient(getActivity(), null);

        // Construct a PlaceDetectionClient.
        mPlaceDetectionClient = Places.getPlaceDetectionClient(getActivity(), null);

        // Construct a FusedLocationProviderClient.
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(getActivity());

        geocoder = new Geocoder(getContext());

        SupportMapFragment mapFragment = (SupportMapFragment) this.getChildFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        init();

        return view;

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        btntracker = view.findViewById(R.id.create_tracker);
        btntracker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createTrackerPopup();
            }
        });
        btnCheckIn = view.findViewById(R.id.btn_chkin);
        btnCheckIn.setVisibility(View.GONE);

        btnCheckIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnCheckIn.setVisibility(View.GONE);
                addCheckInPost();
            }
        });
        super.onViewCreated(view, savedInstanceState);

    }

    private void addCheckInPost() {

        SimpleDateFormat mdformat = new SimpleDateFormat("dd MM yyyy, HH:mm");
        String strDate = mdformat.format(Calendar.getInstance().getTime());

        Post post = new Post();
        post.setUser(user.getId());
        post.setContent(user.getName() + " checked in." );
        post.setUser_pic(user.getPicture());
        post.setCreated_at(strDate);
        post.setUser_name(user.getName());

        String postData = "{ \"place_id\":" + "\"" + selectedMarker.getTag() + "\","
                + "\"user\":" + "\"" + user.getId() + "\","
                + "\"user_name\":\"" + user.getName() + "\","
                + "\"user_pic\":\"" + user.getPicture() + "\","
                + "\"created_at\":" + "\"" + strDate + "\","
                + "\"content\":" + "\"" + user.getName() + " checked in at " + strDate + "\""
                + "}";

        Log.e("checkked in data", postData);

        AsynchTaskPost asynchTaskPost = new AsynchTaskPost(postData, getContext(), new EventListener<String>() {
            @Override
            public void onSuccess(String object) {
                JSONObject jsonObject = null;
                String message = "";
                Boolean ok = false;
                try {
                    jsonObject = new JSONObject(object);
                    ok = jsonObject.getBoolean("ok");
                    message = jsonObject.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (ok && message.contains("Post was not added")) {
                    showMessage("Error. Please Retry.");
                } else if (ok && message.contains("Post added")) {
                    showMessage("Checked in!");
                }
            }

            @Override
            public void onFailure(Exception e) {
                showMessage("Post not added. Internal server error");
            }
        });
        asynchTaskPost.execute(Constants.CREATE_POST);
    }

    private void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    Spinner src, dest;

    private void createTrackerPopup() {
        final LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //Inflate the view from a predefined XML layout (no need for root id, using entire layout)
        final View layout = inflater.inflate(R.layout.activity_create_tracker, null);
        Button start;

        src = layout.findViewById(R.id.spinner_src);
        dest = layout.findViewById(R.id.spinner_dst);
        start = layout.findViewById(R.id.btn_start);
        final PopupWindow p = setUpPopup(layout);
        getPlacesNames();

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createTracker();
                p.dismiss();

            }
        });


    }

    Place currentLoc = new Place();
    Place sourceSelected;
    Place destinationSelected;

    private void getPlacesNames() {
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

                if (ok && message.contains("Places loaded")) {
                    try {
                        jsonArray = jsonObject.getJSONArray("data");
                        ArrayList<String> cLoc = new ArrayList<>();
                        ArrayList<String> places = new ArrayList<>();
                        cLoc.add("Current Location");
                        currentLoc.setName("Current Location");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            Place place = gson.fromJson(jsonArray.get(i).toString(), Place.class);
                            placesObjects.add(place);
                            places.add(place.getName());
                        }
                        Log.e("places size array",places.size()+"");
                        ArrayAdapter adapter2 = new ArrayAdapter<>(getContext(),
                                android.R.layout.simple_spinner_item, cLoc);
                        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        src.setAdapter(adapter2);
                        src.setOnItemSelectedListener(onSourceSelected);

                        ArrayAdapter adapter = new ArrayAdapter<>(getContext(),
                                android.R.layout.simple_spinner_item, places);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        dest.setAdapter(adapter);
                        dest.setOnItemSelectedListener(onDestinationSelected);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Exception e) {
            }
        });
        asynchTaskGet.execute(Constants.GET_PLACES);
    }


    AdapterView.OnItemSelectedListener onSourceSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            sourceSelected = currentLoc;
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };
    AdapterView.OnItemSelectedListener onDestinationSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            destinationSelected = placesObjects.get(i);
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

    public void addPolyLine() {

        if (sourceSelected != null && destinationSelected != null) {
            if(polyline1 != null)
                polyline1.remove();
            polyline1 = mMap.addPolyline(new PolylineOptions()
                    .clickable(true)
                    .add(
                            new LatLng(user.getCurrent_location().getLatitude(),
                                    user.getCurrent_location().getLongitude()),
                            new LatLng(destinationSelected.getLocation().getLatitude(),
                                    destinationSelected.getLocation().getLongitude())));
            polyline1.setStartCap(new RoundCap());
            polyline1.setEndCap(new RoundCap());
            polyline1.setWidth(POLYLINE_STROKE_WIDTH_PX);
            polyline1.setColor(COLOR_BLACK_ARGB);
            polyline1.setJointType(JointType.ROUND);
            mMap.setOnPolylineClickListener(this);
        }


    }

    private PopupWindow setUpPopup(View view) {
        //Get the devices screen density to calculate correct pixel sizes
        float density = getActivity().getResources().getDisplayMetrics().density;
        // create a focusable PopupWindow with the given layout and correct size
        final PopupWindow pw = new PopupWindow(view, (int) density * 250, (int) density * 220, true);
        //Set up touch closing outside of pop-up
        pw.setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));
        pw.setTouchInterceptor(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    pw.dismiss();
                    return true;
                }
                return false;
            }
        });
        pw.setOutsideTouchable(true);
        // display the pop-up in the center
        pw.showAtLocation(view, Gravity.CENTER, 0, 0);
        return pw;
    }

    /**
     * Saves the state of the map when the activity is paused.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (mMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, mMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, mLastKnownLocation);
            super.onSaveInstanceState(outState);
        }
    }

    private void init() {
        sharedPreferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
        gson = new Gson();

        String json = sharedPreferences.getString("user_info", "");
        user = gson.fromJson(json, User.class);
        friendsMarkers = new ArrayList<>();
        placesMarkers = new ArrayList<>();
    }

    private void updateLocation(double lat, double lng) {

        String user_id = user.getId();
        //update user location
        user.getCurrent_location().setLatitude(lat);
        user.getCurrent_location().setLongitude(lng);

        String newData = " {\"current_location\":{ " +
                "\"longitude\" : " + user.getCurrent_location().getLongitude() + "," +
                "\"latitude\" :" + user.getCurrent_location().getLatitude() + "}} ";


        asynchTaskPut = new AsynchTaskPut(newData, getContext(), new EventListener<String>() {
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
                    addUserMarker();
                    Log.e("Update", "location updated successfully");
                } else {
                    Log.e("Update failed", "location not updated");
                }
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("Update failed", "server error");
            }
        });
        asynchTaskPut.execute(Constants.UPDATE_USER + user_id);
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera. In this case,
     * we just add a marker near Sydney, Australia.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    /**
     * Manipulates the map when it's available.
     * This callback is triggered when the map is ready to be used.
     */

    Marker selectedMarker;

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;
        mMap.setOnMyLocationButtonClickListener(this);
        // Prompt the user for permission.
        getLocationPermission();

        // Turn on the My Location layer and the related control on the map.
        updateLocationUI();

        // Get the current location of the device and set the position of the map.
        getDeviceLocation();

        mMap.setOnInfoWindowClickListener(new GoogleMap.OnInfoWindowClickListener() {
            @Override
            public void onInfoWindowClick(Marker marker) {
                selectedMarker = marker;
                checkIfinRadius(marker);

            }
        });

    }

    private void checkIfinRadius(Marker marker) {
        LatLng latLng = marker.getPosition();
        LatLng userLat = new LatLng(user.getCurrent_location().getLatitude(), user.getCurrent_location().getLongitude());
        if(SphericalUtil.computeDistanceBetween(latLng,userLat) <= CHECK_IN_RADIUS) {
            btnCheckIn.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (mLocationPermissionGranted) {
                Task<Location> locationResult = mFusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(getActivity(), new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            mLastKnownLocation = task.getResult();
                            if (mLastKnownLocation != null) {
                                mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                        new LatLng(mLastKnownLocation.getLatitude(),
                                                mLastKnownLocation.getLongitude()), DEFAULT_ZOOM));
                                updateLocation(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());
                                getFriends();
                                getPlaces();
                                currentLoc.setLocation(new MLocation());
                                currentLoc.getLocation().setLatitude(mLastKnownLocation.getLatitude());
                                currentLoc.getLocation().setLongitude(mLastKnownLocation.getLongitude());
                            }
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            mMap.moveCamera(CameraUpdateFactory
                                    .newLatLngZoom(mDefaultLocation, DEFAULT_ZOOM));
                            mMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    @Override
    public void startIntentSenderForResult(IntentSender intent, int requestCode, @Nullable Intent fillInIntent, int flagsMask, int flagsValues, int extraFlags, Bundle options) throws IntentSender.SendIntentException {
        super.startIntentSenderForResult(intent, requestCode, fillInIntent, flagsMask, flagsValues, extraFlags, options);
    }

    private void getPlaces() {
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

                if (ok && message.contains("Places loaded")) {
                    try {
                        jsonArray = jsonObject.getJSONArray("data");
                        places = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Place place = gson.fromJson(jsonArray.get(i).toString(), Place.class);
                            places.add(place);
                        }
                         addPlacessMarker();
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
        asynchTaskGet.execute(Constants.GET_PLACES);
    }

    private void getFriends() {
        AsynchTaskGet asynchTaskGet = new AsynchTaskGet(getContext(), new EventListener<String>() {
            @Override
            public void onSuccess(String object) {
                JSONObject jsonObject = null;
                String message = "";
                JSONArray jsonArray;
                Boolean ok = false;

                try {
                    jsonObject = new JSONObject(object);
                    ok = jsonObject.getBoolean("ok");
                    message = jsonObject.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (ok && message.contains("friends loaded")) {
                    try {
                        jsonArray = jsonObject.getJSONArray("data");
                        friends = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            User u = gson.fromJson(jsonArray.get(i).toString(), User.class);
                            friends.add(u);
                        }
                        addFriendsMarker();
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                } else if (ok && message.contains("No friends")) {
                    Log.e("Get friends", "No friends found");
                }
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("Get friends", "internal server error");
            }
        });

        asynchTaskGet.execute(Constants.GET_FRIENDS + user.getId());

    }

    private String getAddress(double lat, double lng) {
        try {
            addresses = geocoder.getFromLocation(lat, lng, 1);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String address = addresses.get(0).getAddressLine(0);
        String city = addresses.get(0).getAddressLine(1);

        return address + "" + city;
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

    private void addPlacessMarker() {

        if (places.size() == 0)
            return;
        for (int i = 0; i < places.size(); i++) {
            if (placesMarkers.size() == 0 || placesMarkers.size() < places.size()) {

                if (places.get(i).getLocation() != null) {
                    Marker marker = mMap.addMarker(new MarkerOptions()
                            .position(new LatLng(places.get(i).getLocation().getLatitude(),
                                    places.get(i).getLocation().getLongitude()))
                            .title(places.get(i).getName())
                            .snippet(getAddress(places.get(i).getLocation().getLatitude(),
                                    places.get(i).getLocation().getLongitude()))
                            .infoWindowAnchor(0.5f, 0.5f));
                    marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.savoryplace));
                    marker.setTag(places.get(i).getId());
                    placesMarkers.add(marker);
                } else
                    placesMarkers.get(i).setPosition(new LatLng(places.get(i).getLocation().getLatitude(),
                            places.get(i).getLocation().getLongitude()));
            } else {
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(Constants.DEFAULT_LATITUDE,
                                Constants.DEFAULT_LONTIDUE))
                        .title(places.get(i).getName())
                        .infoWindowAnchor(0.5f, 0.5f));
                marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.savoryplace));

                placesMarkers.add(marker);
            }
        }
    }

    private void addFriendsMarker() {

        if (friends.size() == 0)
            return;
        for (int i = 0; i < friends.size(); i++) {
            if (friendsMarkers.size() == 0 || friendsMarkers.size() < friends.size()) {
                Marker marker = mMap.addMarker(new MarkerOptions()
                        .position(new LatLng(friends.get(i).getCurrent_location().getLatitude(),
                                friends.get(i).getCurrent_location().getLongitude()))
                        .title(friends.get(i).getName())
                        .snippet(getAddress(friends.get(i).getCurrent_location().getLatitude(),
                                friends.get(i).getCurrent_location().getLongitude()))
                        .infoWindowAnchor(0.5f, 0.5f));
                if (friends.get(i).getGender().equals("male"))
                    marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.manmap));
                else if (user.getGender().equals("female"))
                    marker.setIcon(BitmapDescriptorFactory.fromResource(R.drawable.womanmap));
                friendsMarkers.add(marker);
            } else
                friendsMarkers.get(i).setPosition(new LatLng(friends.get(i).getCurrent_location().getLatitude(),
                        friends.get(i).getCurrent_location().getLongitude()));
        }
    }

    /**
     * Prompts the user for permission to use the device location.
     */
    private void getLocationPermission() {
        /*
         * Request location permission, so that we can get the location of the
         * device. The result of the permission request is handled by a callback,
         * onRequestPermissionsResult.
         */
        if (ContextCompat.checkSelfPermission(this.getContext(),
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            mLocationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(getActivity(),
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[],
                                           int[] grantResults) {

        mLocationPermissionGranted = false;
        switch (requestCode) {
            case PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    mLocationPermissionGranted = true;
                }
            }
        }
        updateLocationUI();
    }

    @Override
    public boolean onMyLocationButtonClick() {
        getDeviceLocation();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    /**
     * Updates the map's UI settings based on whether the user has granted location permission.
     */
    private void updateLocationUI() {
        if (mMap == null) {
            return;
        }
        try {
            if (mLocationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                mMap.getUiSettings().setMyLocationButtonEnabled(true);
            } else {
                mMap.setMyLocationEnabled(false);
                mMap.getUiSettings().setMyLocationButtonEnabled(false);
                mLastKnownLocation = null;
                getLocationPermission();
            }
        } catch (SecurityException e) {
            Log.e("Exception: %s", e.getMessage());
        }
    }

    public static String getCurrentTimeStamp() {
        SimpleDateFormat sdfDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");//dd/MM/yyyy
        Date now = new Date();
        String strDate = sdfDate.format(now);
        StringBuilder newStrDate = new StringBuilder(strDate);
        newStrDate.setCharAt(10, 'T');
        strDate = newStrDate.toString();
        strDate += "Z";
        return strDate;
    }

    private void createTracker() {

        if(tracker == null ) {
            tracker = new Tracker();
            tracker.setSource(sourceSelected.getLocation());
            tracker.setDestination(destinationSelected.getLocation());
            tracker.setUser_id(user.getId());
            tracker.setCreated_at(getCurrentTimeStamp());

            String data = gson.toJson(tracker, Tracker.class);
            Log.e("tracker data", data);

            AsynchTaskPost asynchTaskPost = new AsynchTaskPost(data, getContext(), new EventListener<String>() {
                @Override
                public void onSuccess(String object) {

                    JSONObject jsonObject = null;
                    String message = "";
                    Boolean ok = false;

                    try {
                        jsonObject = new JSONObject(object);
                        ok = jsonObject.getBoolean("ok");
                        message = jsonObject.getString("message");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                    if (ok && message.contains("Tracker created")) {
                        tracker = null;
                        try {
                            tracker = gson.fromJson(jsonObject.getString("data"), Tracker.class);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        addPolyLine();

                    }
                }

                @Override
                public void onFailure(Exception e) {
                    tracker=null;
                    Toast.makeText(getContext(), "Internal server error.", Toast.LENGTH_SHORT).show();
                }
            });
            asynchTaskPost.execute(Constants.CREATE_TRACKER);
        } else
            Toast.makeText(getContext(), "Tracker already active.", Toast.LENGTH_SHORT).show();

    }

    @Override
    public void onPolylineClick(Polyline polyline) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setMessage("Are you sure you want to delete this tracker?");
        alertDialogBuilder.setPositiveButton("yes",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                                deleteTracker();
                    }
                });

        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    private void deleteTracker() {

        AsynchTaskDelete asynchTaskDelete = new AsynchTaskDelete("", getContext(), new EventListener<String>() {
            @Override
            public void onSuccess(String object) {
                JSONObject jsonObject = null;
                String message = "";
                Boolean ok = false;

                try {
                    jsonObject = new JSONObject(object);
                    ok = jsonObject.getBoolean("ok");
                    message = jsonObject.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (ok && message.contains("Tracker deleted successfully")) {
                    Toast.makeText(getContext(), "Tracker deleted.", Toast.LENGTH_SHORT).show();
                    tracker=null;
                    polyline1.remove();

                } else if (ok && message.contains("Tracker is not")) {
                    Toast.makeText(getContext(), "Tracker not deleted.Retry", Toast.LENGTH_SHORT).show();

                }
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getContext(), "Internal server error.", Toast.LENGTH_SHORT).show();

            }
        });
        asynchTaskDelete.execute(Constants.DELETE_TRACKER + tracker.getId());
    }

}
