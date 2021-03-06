package com.example.saraelsheemi.pinmate.views.place;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.example.saraelsheemi.pinmate.R;
import com.example.saraelsheemi.pinmate.controllers.AsynchTasks.AsynchTaskGet;
import com.example.saraelsheemi.pinmate.controllers.AsynchTasks.AsynchTaskPost;
import com.example.saraelsheemi.pinmate.controllers.Constants;
import com.example.saraelsheemi.pinmate.controllers.EventListener;
import com.example.saraelsheemi.pinmate.controllers.adapters.PlacesAdapter;
import com.example.saraelsheemi.pinmate.models.Place;
import com.example.saraelsheemi.pinmate.models.User;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class AllPlacesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    private ListView listViewPlace, listViewRec;
    private ArrayList<Place> places, recPlaces;
    private ArrayAdapter<Place> placesAdapter, recAdapter;
    ProgressBar progressBar;
    SwipeRefreshLayout swipeRefreshLayout;
    Gson gson;
    private AsynchTaskGet asynchTaskGet;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_allplaces_list_view, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        init(view);
        getActivity().setTitle("Places");
        getPlaces();
        try {
            getRecommendedPlaces();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        super.onViewCreated(view, savedInstanceState);
    }

    public void init(View view) {
        places = new ArrayList<>();
        recPlaces = new ArrayList<>();
        gson = new Gson();
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh_allPlaces);
        swipeRefreshLayout.setOnRefreshListener(this);
        progressBar = view.findViewById(R.id.progressbar_favorites_loading);
        listViewPlace = view.findViewById(R.id.listView_allPlaces);
        listViewRec = view.findViewById(R.id.listView_recommended);
        recPlaces = new ArrayList<>();

        //get recommended places

        placesAdapter = new PlacesAdapter(getActivity(), R.layout.activity_allplaces_list_item, new ArrayList<Place>(), this);
        recAdapter = new PlacesAdapter(getActivity(), R.layout.activity_recplaces_list_item, recPlaces, this);

        listViewRec.setAdapter(recAdapter);
        listViewRec.setOnItemClickListener(onItemClickListener);
        listViewPlace.setAdapter(placesAdapter);
        listViewPlace.setOnItemClickListener(onItemClickListener);

        sharedPreferences = getActivity().getSharedPreferences("placeInfo", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();


    }

    private void getPlaces() {
        asynchTaskGet = new AsynchTaskGet(getContext(), new EventListener<String>() {
            @Override
            public void onSuccess(String object) {
                swipeRefreshLayout.setRefreshing(false);
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
                        populatePlacesArrayListAdapter(places);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Exception e) {
                swipeRefreshLayout.setRefreshing(false);
                showMessage("Internal server error.");
            }
        });
        asynchTaskGet.execute(Constants.GET_PLACES);
    }


    private void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    public void populatePlacesArrayListAdapter(ArrayList<Place> placeArrayList) {
        placesAdapter.clear();
        placesAdapter.addAll(placeArrayList);
        placesAdapter.notifyDataSetChanged();
    }

    public void populateRecPlacesArrayListAdapter(ArrayList<Place> placeArrayList) {
        recAdapter.clear();
        recAdapter.addAll(placeArrayList);
        recAdapter.notifyDataSetChanged();
    }


    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

            //add place info in shared preferences to pass it to next fragment
            sharedPreferences = getActivity().getSharedPreferences("placeInfo", Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
            editor.apply();
            Place place = (Place) adapterView.getItemAtPosition(position);
            String jsonData = gson.toJson(place, Place.class);
            editor.putString("place_details", jsonData);
            editor.apply();

            //open place profile
            PlaceProfile profile = new PlaceProfile();
            FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
            if(profile != null ) {
                transaction.replace(R.id.content_frame, profile); // give your fragment container id in first parameter
                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();
            }
        }
    };

    @Override
    public void onRefresh() {
        getPlaces();
    }


    public void getRecommendedPlaces() throws JSONException {
        sharedPreferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
        String json = sharedPreferences.getString("user_info", "");
        final Gson gson = new Gson();
        User user = gson.fromJson(json, User.class);

        final JSONObject data = new JSONObject();
        data.put("id", user.getId());
        AsynchTaskPost asynchTaskPost = new AsynchTaskPost(data.toString(), getContext(), new EventListener<String>() {
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
                Log.e("rec response", jsonObject.toString());
                if (ok) {
                    try {
                        JSONArray jsonArray = jsonObject.getJSONArray("data");
                        Log.e("rec response", jsonArray.toString());
                        recPlaces = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Place place = gson.fromJson(jsonArray.get(i).toString(), Place.class);
                            Log.e("recplace", jsonArray.get(i).toString());
                            recPlaces.add(place);
                        }
                        populateRecPlacesArrayListAdapter(recPlaces);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                } else {
                    showMessage("Place not added.");
                }
            }

            @Override
            public void onFailure(Exception e) {
                showMessage("Internal server error");
            }
        });
        asynchTaskPost.execute(Constants.RECOMMEND_PLACE);

    }

    public void favoritePlace(String placeId) {

        sharedPreferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
        String json = sharedPreferences.getString("user_info", "");
        Gson gson = new Gson();
        User user = gson.fromJson(json, User.class);
        if(user.getFavoritePlaces().contains(placeId))
            showMessage("Place already in favorites.");

       else {
            Log.e("unfavorite id", placeId);
            String data = "{\"place_id\":\"" + placeId + "\","
                    + "\"user_id\":\"" + getUserID() + "\"}";
            Log.e("data", data);

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

                    if (ok && message.contains("Favorite added")) {
                        showMessage("Added to favorites.");
                    } else {
                        showMessage("Place not added.");
                    }
                }

                @Override
                public void onFailure(Exception e) {
                    showMessage("Internal server error");
                }
            });
            asynchTaskPost.execute(Constants.FAVORITE_PLACE);
        }
    }

    private String getUserID() {
        sharedPreferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
        String userdata = sharedPreferences.getString("user_info", "");
        User user = gson.fromJson(userdata, User.class);
        return user.getId();
    }

    public void unfavoritePlace(String placeId) {
        Log.e("unfavorite id", placeId);
        String data = "{\"place_id\":\"" + placeId + "\","
                + "\"user_id\":\"" + getUserID() + "\"}";
        Log.e("data", data);

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

                if (ok && message.contains("Favorite removed")) {
                    showMessage("Removed from favorites.");
                } else {
                    showMessage("Place not removed.");
                }
            }
            @Override
            public void onFailure(Exception e) {
                showMessage("Internal server error");
            }
        });
        asynchTaskPost.execute(Constants.UNFAVORITE_PLACE);
    }

}
