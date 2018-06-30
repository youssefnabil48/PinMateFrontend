package com.example.saraelsheemi.pinmate.views;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saraelsheemi.pinmate.R;
import com.example.saraelsheemi.pinmate.controllers.AsynchTasks.AsynchTaskGet;
import com.example.saraelsheemi.pinmate.controllers.Constants;
import com.example.saraelsheemi.pinmate.controllers.EventListener;
import com.example.saraelsheemi.pinmate.controllers.SearchAdapter;
import com.example.saraelsheemi.pinmate.models.SearchItem;
import com.example.saraelsheemi.pinmate.views.user.AnyUser;
import com.google.gson.Gson;

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
    JSONObject jsonObject = null;
    JSONArray jsonArray;

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
//                JSONObject jsonObject = null;
//                JSONArray jsonArray;
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
        String itemType = (String)((TextView) view.findViewById(R.id.search_item_type)).getText();
        if (itemType.equals("place")){
            //redirect to place view
            try {
                JSONObject placeJson = (JSONObject)jsonArray.get(position);
                showMessage(placeJson.getString("name"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }else {
            try {
                Intent i = new Intent(this, AnyUser.class);
                i.putExtra("user", jsonArray.get(position).toString());
                startActivity(i);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }
}
