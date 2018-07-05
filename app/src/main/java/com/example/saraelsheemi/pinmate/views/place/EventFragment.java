package com.example.saraelsheemi.pinmate.views.place;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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
import com.example.saraelsheemi.pinmate.models.HangoutRequest;
import com.example.saraelsheemi.pinmate.models.Place;
import com.example.saraelsheemi.pinmate.models.Post;
import com.example.saraelsheemi.pinmate.models.User;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class EventFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemLongClickListener, View.OnClickListener{

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
    Button addEventBtn;
    User loggedInUser;

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
        place = new Place();
        name = view.findViewById(R.id.event_name);
        description = view.findViewById(R.id.description);
        startDate = view.findViewById(R.id.start_date);
        endDate = view.findViewById(R.id.end_date);
        gson = new Gson();
        events = new ArrayList<>();

        swipeRefreshLayout = view.findViewById(R.id.swiperefresh_events);
        swipeRefreshLayout.setOnRefreshListener(this);
        progressBar = view.findViewById(R.id.progressbar_events_loading);


        eventsAdapter = new EventsAdapter(getActivity(),events);
        eventsView = view.findViewById(R.id.listView_events);
        eventsView.setAdapter(eventsAdapter);


        eventsView.setLongClickable(true);
        eventsView.setOnItemLongClickListener(this);
        registerForContextMenu(eventsView);

        addEventBtn = view.findViewById(R.id.btn_new_event);
        addEventBtn.setOnClickListener(this);

        //get current logged in user
        sharedPreferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
        String json = sharedPreferences.getString("user_info","");
        loggedInUser = gson.fromJson(json,User.class);

        sharedPreferences = getActivity().getSharedPreferences("placeInfo", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
        String placeJson = sharedPreferences.getString("place_details", "");
        place = gson.fromJson(placeJson,Place.class);
        actAsManager();

    }

    private void actAsManager(){
        try {
            if (!(place.getManager().equals(loggedInUser.getId()))) {
                addEventBtn.setVisibility(View.INVISIBLE);
                RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) swipeRefreshLayout.getLayoutParams();
                lp.removeRule(RelativeLayout.BELOW);
            }
        }catch (Exception e ){
            addEventBtn.setVisibility(View.INVISIBLE);
            RelativeLayout.LayoutParams lp = (RelativeLayout.LayoutParams) swipeRefreshLayout.getLayoutParams();
            lp.removeRule(RelativeLayout.BELOW);
            Log.e("error", e.getMessage());
        }
    }


    //called when first time opening list view
    private void getEvents() {
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
                    Log.e("response message", message);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (ok) {
                    showMessage("Updated");
                    try {
                        placeinfo = jsonObject.getString("data");
                        Log.e("place after update", placeinfo);
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


    private int startmYear, startmMonth, startmDay, startmHour, startmMinute;
    private int endmYear, endmMonth, endmDay, endmHour, endmMinute;
    ImageButton startDatebtn, startTimebtn;
    ImageButton endDatebtn, endTimebtn;
    EditText edtStartDate, edtStartTime, edtEndDate, edtEndTime, newtitle, newdesc;
    Button addEventButton;
    PopupWindow pw;

    private void createEventPopup() {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //Inflate the view from a predefined XML layout (no need for root id, using entire layout)
        View layout = inflater.inflate(R.layout.activity_create_event, null);

        //buttons
        startDatebtn = layout.findViewById(R.id.imgbtn_event_start_date);
        endDatebtn = layout.findViewById(R.id.imgbtn_event_end_date);

        startTimebtn = layout.findViewById(R.id.imgbtn_event_start_time);
        endTimebtn = layout.findViewById(R.id.imgbtn_event_end_time);

        addEventButton = layout.findViewById(R.id.btn_Add_event);
        addEventButton.setOnClickListener(this);

        //title and dexcription
        newtitle = layout.findViewById(R.id.edt_event_title);
        newdesc = layout.findViewById(R.id.edt_desc);

        //edit text views
        edtStartDate = layout.findViewById(R.id.edt_event_start_date);
        edtEndDate = layout.findViewById(R.id.edt_end_date);
        edtStartTime = layout.findViewById(R.id.edt_event_start_time);
        edtEndTime = layout.findViewById(R.id.edt_end_time);

        setUpPopup(layout);
    }

    private void setUpPopup(View view) {
        //Get the devices screen density to calculate correct pixel sizes
        float density = getActivity().getResources().getDisplayMetrics().density;
        // create a focusable PopupWindow with the given layout and correct size
        pw = new PopupWindow(view, (int) density * 330, (int) density * 385, true);
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
        switch (item.getItemId()) {
            case R.id.delete_item: {
                deleteEvent(deletedEventId);
                getUpdatedEvents();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_new_event: {
                createEventPopup();
            }
            break;
            case R.id.btn_Add_event: {
                Event event = new Event();
                event.setName(newtitle.getText().toString());
                event.setDescription(newdesc.getText().toString());
                event.setStart_date(edtStartDate.getText()+" "+edtStartTime.getText());
                event.setEnd_date(edtEndDate.getText()+ " " + edtEndTime.getText());
                String jsonEvent = gson.toJson(event, Event.class);
                createAddEventRequest(event);
            }
            break;
            case R.id.imgbtn_event_start_date: {
                final Calendar c = Calendar.getInstance();
                startmYear = c.get(Calendar.YEAR);
                startmMonth = c.get(Calendar.MONTH);
                startmDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                edtStartDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, startmYear, startmMonth, startmDay);
                datePickerDialog.show();
            }
            break;
            case R.id.imgbtn_event_start_time: {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                startmHour = c.get(Calendar.HOUR_OF_DAY);
                startmMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                edtStartTime.setText(hourOfDay + ":" + minute);
                            }
                        }, startmHour, startmMinute, false);
                timePickerDialog.show();
            }
            break;
            case R.id.imgbtn_event_end_date: {
                final Calendar c = Calendar.getInstance();
                endmYear = c.get(Calendar.YEAR);
                endmMonth = c.get(Calendar.MONTH);
                endmDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                edtEndDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, endmYear, endmMonth, endmDay);
                datePickerDialog.show();
            }
            break;
            case R.id.imgbtn_event_end_time: {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                endmHour = c.get(Calendar.HOUR_OF_DAY);
                endmMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                edtEndTime.setText(hourOfDay + ":" + minute);
                            }
                        }, endmHour, endmMinute, false);
                timePickerDialog.show();
            }
            break;
        }
    }

    private void createAddEventRequest(Event event){

        String data = gson.toJson(event, Event.class);
        JsonObject o = new JsonParser().parse(data).getAsJsonObject();
        o.addProperty("place_id", String.valueOf(place.getId()));
        Log.e("hangout request data", data);

        AsynchTaskPost asynchTaskPost = new AsynchTaskPost(o.toString(), getContext(), new EventListener<String>() {
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

                if (ok) {
                    pw.dismiss();
                    getUpdatedEvents();
                    showMessage("Event added");
                }
            }
            @Override
            public void onFailure(Exception e) {
                showMessage("Internal server error. Retry");
            }
        });
        asynchTaskPost.execute(Constants.CREATE_EVENT);
    }
}
