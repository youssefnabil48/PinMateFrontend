package com.example.saraelsheemi.pinmate.views;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saraelsheemi.pinmate.R;
import com.example.saraelsheemi.pinmate.controllers.AsynchTaskGet;
import com.example.saraelsheemi.pinmate.controllers.Constants;
import com.example.saraelsheemi.pinmate.controllers.EventListener;
import com.example.saraelsheemi.pinmate.controllers.PlacesAdapter;
import com.example.saraelsheemi.pinmate.controllers.SearchAdapter;
import com.example.saraelsheemi.pinmate.models.Place;
import com.example.saraelsheemi.pinmate.models.SearchItem;
import com.example.saraelsheemi.pinmate.views.place.PlaceProfile;
import com.google.gson.Gson;
import android.app.Fragment;
import android.app.FragmentTransaction;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class SearchableActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {

    private String query;
    private ArrayList<SearchItem> searchItems;
    private ArrayAdapter<SearchItem> searchAdapter;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Gson gson;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_searchable);
        init();
        getPlaces();
    }

    public void init() {
        searchItems = new ArrayList<>();
        searchAdapter = new SearchAdapter(this, searchItems);
        ListView searchListView = findViewById(R.id.search_list_view);
        searchListView.setAdapter(searchAdapter);
        searchListView.setOnItemClickListener(this);
        query = getIntent().getStringExtra("query");
        TextView searchTitle = findViewById(R.id.title);
        searchTitle.setText(String.format("Search Result for \"%s\"", query));
        gson = new Gson();
    }

    private void getPlaces() {
        AsynchTaskGet asynchTaskGet = new AsynchTaskGet(this, new EventListener<String>() {
            @Override
            public void onSuccess(String object) {
                JSONObject jsonObject = null;
                JSONArray jsonArray;
                String message;
                Boolean ok = false;

                try {
                    jsonObject = new JSONObject(object);
                    ok = jsonObject.getBoolean("ok");
                    message = jsonObject.getString("message");
                    showMessage(message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (ok) {
                    try {
                        jsonArray = jsonObject.getJSONArray("data");
                        Log.e("response", jsonArray.toString());
                        JSONObject itemJson;
                        searchItems = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            itemJson =(JSONObject) jsonArray.get(i);
                            SearchItem item = new SearchItem();
                            //adding data to items
                            item.setName(itemJson.getString("name"));
                            item.setId(itemJson.getString("_id"));
                            if (itemJson.has("picture")){
                                item.setPicURL(itemJson.getString("picture"));
                            }else {
                                item.setPicURL(null);
                            }

                            if(itemJson.has("gender")){
                                //item is a user
                                item.setHint(itemJson.getString("email"));
                                item.setItemType("user");

                            }else {
                                //item is a place
                                item.setHint(itemJson.getString("description"));
                                item.setItemType("place");
                            }
                            searchItems.add(item);
                        }
                        populateSearchItemsArrayListAdapter(searchItems);
                        Log.e("items", searchItems.toString());

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Exception e) {
                showMessage("internal error please try again"+e.getMessage());
            }
        });
        asynchTaskGet.execute(Constants.SEARCH+query);
    }

    public void populateSearchItemsArrayListAdapter(ArrayList<SearchItem> searchItems) {
        searchAdapter.addAll(searchItems);
        searchAdapter.notifyDataSetChanged();
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        //set listener on item click to redirect to single user or place view
        String itemId = (String)((TextView) view.findViewById(R.id.search_item_id)).getText();
        String itemType = (String)((TextView) view.findViewById(R.id.search_item_type)).getText();
        if (itemType.equals("place")){
            showMessage("place");
        }else {
            showMessage("user");
        }

//        TextView idTextView = view.findViewById(R.id.id);
//        TextView userNameTextView = view.findViewById(R.id.user_name);
//        Intent intent = new Intent(this, SingleChat.class);
//        intent.putExtra("userId", idTextView.getText());
//        intent.putExtra("userName", userNameTextView.getText());
//        startActivity(intent);
    }
}
