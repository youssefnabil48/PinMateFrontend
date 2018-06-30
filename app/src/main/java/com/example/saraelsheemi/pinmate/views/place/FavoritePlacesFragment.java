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
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.saraelsheemi.pinmate.R;
import com.example.saraelsheemi.pinmate.controllers.AsynchTasks.AsynchTaskGet;
import com.example.saraelsheemi.pinmate.controllers.AsynchTasks.AsynchTaskPost;
import com.example.saraelsheemi.pinmate.controllers.Constants;
import com.example.saraelsheemi.pinmate.controllers.EventListener;
import com.example.saraelsheemi.pinmate.controllers.adapters.FavoritePlacesAdapter;
import com.example.saraelsheemi.pinmate.models.Place;
import com.example.saraelsheemi.pinmate.models.User;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class FavoritePlacesFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

        private ListView listViewFavPlaces;
        private ArrayList<Place> places;
        private ArrayAdapter<Place> placesAdapter;
        ProgressBar progressBar;
        SwipeRefreshLayout swipeRefreshLayout;
        Gson gson;
        SharedPreferences sharedPreferences;
        SharedPreferences.Editor editor;


        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
            return inflater.inflate(R.layout.activity_favorites_list_view, container, false);

        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            init(view);
            getActivity().setTitle("Favorites");
            getPlaces();
            super.onViewCreated(view, savedInstanceState);
        }

        public void init(View view) {
            places = new ArrayList<>();
            gson = new Gson();
            swipeRefreshLayout = view.findViewById(R.id.swiperefresh_favorites);
            swipeRefreshLayout.setOnRefreshListener(this);
            progressBar = view.findViewById(R.id.progressbar_favorites_loading);
            listViewFavPlaces = view.findViewById(R.id.listView_favorites);

            placesAdapter = new FavoritePlacesAdapter(getActivity(), R.layout.activity_favorites_list_item, new ArrayList<Place>(), this);
            listViewFavPlaces.setAdapter(placesAdapter);
            listViewFavPlaces.setOnItemClickListener(onItemClickListener);

            sharedPreferences = getActivity().getSharedPreferences("placeInfo", Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
            editor.apply();

        }

        private void getPlaces() {
          AsynchTaskGet asynchTaskGet = new AsynchTaskGet(getContext(), new EventListener<String>() {
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

                    if (ok && message.contains("Favorites loaded")) {
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
                    }else if(ok && message.contains("No favorites")) {
                        swipeRefreshLayout.setRefreshing(false);
                        showMessage("No favorites found");
                    }
                }
                @Override
                public void onFailure(Exception e) {
                    swipeRefreshLayout.setRefreshing(false);
                    showMessage("Internal server error.");
                }
            });
            asynchTaskGet.execute(Constants.GET_USER_FAVS_PLACES + getUserID());
        }


        private void showMessage(String message) {
            Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
        }

        public void populatePlacesArrayListAdapter(ArrayList<Place> placeArrayList) {
            placesAdapter.clear();
            placesAdapter.addAll(placeArrayList);
            placesAdapter.notifyDataSetChanged();
        }

        AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //add place info in shared preferences to pass it to next fragment
                Place place = (Place) adapterView.getItemAtPosition(position);
                String jsonData = gson.toJson(place, Place.class);
                editor.putString("place_details", jsonData);
                editor.apply();

                //open place profile
                Fragment profile = new PlaceProfile();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.replace(R.id.content_frame, profile); // give your fragment container id in first parameter
                //   transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
                transaction.commit();
            }
        };

        @Override
        public void onRefresh() {
            getPlaces();
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
                        getPlaces();
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


