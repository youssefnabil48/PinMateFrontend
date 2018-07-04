package com.example.saraelsheemi.pinmate.views.place;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saraelsheemi.pinmate.R;
import com.example.saraelsheemi.pinmate.controllers.AsynchTasks.AsynchTaskDelete;
import com.example.saraelsheemi.pinmate.controllers.AsynchTasks.AsynchTaskGet;
import com.example.saraelsheemi.pinmate.controllers.AsynchTasks.AsynchTaskPost;
import com.example.saraelsheemi.pinmate.controllers.Constants;
import com.example.saraelsheemi.pinmate.controllers.EventListener;
import com.example.saraelsheemi.pinmate.controllers.adapters.EventsAdapter;
import com.example.saraelsheemi.pinmate.controllers.adapters.PostsAdapter;
import com.example.saraelsheemi.pinmate.models.Event;
import com.example.saraelsheemi.pinmate.models.Place;
import com.example.saraelsheemi.pinmate.models.Post;
import com.example.saraelsheemi.pinmate.models.User;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class EventFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemLongClickListener{

    TextView name;
    TextView description;
    TextView startDate;
    TextView endDate;
    ProgressBar progressBar;
    SwipeRefreshLayout swipeRefreshLayout;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Gson gson;
    ArrayList<Event> events;
    Place place;
    EventsAdapter eventsAdapter;
    ListView eventsView;
    String deletedEventId;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_event_list_view, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        getEvents();
    }

    private void init(View view){
        name = view.findViewById(R.id.event_name);
        description = view.findViewById(R.id.description);
        startDate = view.findViewById(R.id.start_date);
        endDate = view.findViewById(R.id.end_date);
        gson = new Gson();
        events = new ArrayList<>();
        sharedPreferences = getActivity().getSharedPreferences("placeInfo", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh_events);
        swipeRefreshLayout.setOnRefreshListener(this);
        progressBar = view.findViewById(R.id.progressbar_events_loading);


        eventsAdapter = new EventsAdapter(getActivity(),events);
        eventsView = view.findViewById(R.id.listView_events);
        eventsView.setAdapter(eventsAdapter);


        eventsView.setLongClickable(true);
        eventsView.setOnItemLongClickListener(this);
        registerForContextMenu(eventsView);
    }

    //called when first time opening list view
    private void getEvents() {
        String json = sharedPreferences.getString("place_details", "");
        gson = new Gson();
        place = gson.fromJson(json, Place.class);
        events = place.getEvents();
        populatePostsArrayListAdapter(events);
    }

    //delete old list and get new one
    public void populatePostsArrayListAdapter(ArrayList<Event> eventsArrayList) {
        eventsAdapter.clear();
        eventsAdapter.addAll(eventsArrayList);
        eventsAdapter.notifyDataSetChanged();
    }

    private void getUpdatedEvents() {

        AsynchTaskGet asynchTaskGet = new AsynchTaskGet(getContext(), new EventListener<String>() {
            @Override
            public void onSuccess(String object) {
                swipeRefreshLayout.setRefreshing(false);
                JSONObject jsonObject = null;
                String message = "";
                Boolean ok = false;
                String placeinfo = "";

                try {
                    jsonObject = new JSONObject(object);
                    ok = jsonObject.getBoolean("ok");
                    message = jsonObject.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (ok && message.contains("events loaded")) {
                    showMessage("Updated");
                    try {
                        placeinfo = jsonObject.getString("data");
                        Place p = gson.fromJson(placeinfo, Place.class);
                        events = p.getEvents();
                        populatePostsArrayListAdapter(events);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Exception e) {
                swipeRefreshLayout.setRefreshing(false);
            }
        });
        asynchTaskGet.execute(Constants.GET_PLACE + place.getId());
    }

    @Override
    public void onRefresh() {
        getUpdatedEvents();
    }


    private void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        Event e = (Event) adapterView.getItemAtPosition(i);
        deletedEventId = e.getId();
        eventsView.showContextMenu();
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.event_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        showMessage(String.valueOf(item.getItemId()));
        Log.e("event", String.valueOf(item.getItemId()));
        switch (item.getItemId()) {
            case R.id.delete_item: {
                Log.e("delete id", deletedEventId);
                deleteEvent(deletedEventId);
            }
            break;
        }
        return true;
    }

    public void deleteEvent(String eventId) {

        Log.e("event id", eventId);
        String data = "{\"place_id\":\"" + place.getId() + "\"}";

        AsynchTaskDelete asynchTaskDelete = new AsynchTaskDelete(data, getContext(), new EventListener<String>() {
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
                if (ok && message.contains("Event deleted")) {
                    showMessage("event deleted.");
                    getUpdatedEvents();
                } else if (ok && message.contains("Event was not")) {
                    showMessage("Event not deleted. Retry");
                }

            }

            @Override
            public void onFailure(Exception e) {
                showMessage("Internal server error. Retry.");
            }
        });
        asynchTaskDelete.execute(Constants.DELETE_EVENT + eventId);
    }

}
