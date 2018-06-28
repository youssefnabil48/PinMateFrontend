package com.example.saraelsheemi.pinmate.views.user;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.saraelsheemi.pinmate.R;
import com.example.saraelsheemi.pinmate.controllers.AsynchTaskGet;
import com.example.saraelsheemi.pinmate.controllers.AsynchTaskPost;
import com.example.saraelsheemi.pinmate.controllers.Constants;
import com.example.saraelsheemi.pinmate.controllers.EventListener;
import com.example.saraelsheemi.pinmate.controllers.HangoutRequestsAdapter;
import com.example.saraelsheemi.pinmate.models.HangoutRequest;
import com.example.saraelsheemi.pinmate.models.Place;
import com.example.saraelsheemi.pinmate.models.User;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

public class HangoutRequestsFragment extends Fragment implements View.OnClickListener {

    private ListView listViewHangouts;
    private ArrayList<HangoutRequest> hangoutRequests;
    private String user_id;
    private ArrayAdapter<HangoutRequest> hangoutRequestArrayAdapter;
    private
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    ProgressBar progressBar;
    Gson gson;
    User user;
    ImageButton newHangoutReq;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_hangouts_list_view, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        init(view);
        getHangouts();
        super.onViewCreated(view, savedInstanceState);
    }

    public void init(View view) {
        sharedPreferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
        hangoutRequests = new ArrayList<>();
        progressBar = view.findViewById(R.id.progressbar_loading_hangouts);
        listViewHangouts = view.findViewById(R.id.listView_hangouts);
        listViewHangouts.setOnItemClickListener(onItemClickListener);
        hangoutRequestArrayAdapter = new HangoutRequestsAdapter(getActivity(), R.layout.activity_hangouts_list_item, new ArrayList<HangoutRequest>());
        listViewHangouts.setAdapter(hangoutRequestArrayAdapter);
        newHangoutReq = view.findViewById(R.id.btn_new_hangout);
        newHangoutReq.setOnClickListener(this);

    }

    private void getHangouts() {
        String json = sharedPreferences.getString("user_info", "");
        gson = new Gson();
        user = gson.fromJson(json, User.class);
        user_id = user.getId();
        Log.e("user id ", user_id);

        AsynchTaskGet asynchTaskGet = new AsynchTaskGet(getContext(), new EventListener<String>() {
            @Override
            public void onSuccess(String object) {
                progressBar.setVisibility(View.INVISIBLE);
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

                if (ok && message.contains("No requests")) {
                    showMessage("No Requests yet.");

                } else if (ok && message.contains("Requests loaded")) {
                    try {
                        jsonArray = jsonObject.getJSONArray("data");

                        for (int i = 0; i < jsonArray.length(); i++) {
                            HangoutRequest hangoutRequest = gson.fromJson(jsonArray.get(i).toString(), HangoutRequest.class);
                            hangoutRequests.add(hangoutRequest);
                        }

                        populateFriendsArrayListAdapter(hangoutRequests);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            }

            @Override
            public void onFailure(Exception e) {
                showMessage("Internal server error.");
            }
        });
        asynchTaskGet.execute(Constants.GET_USER_HANGOUTS + user_id);

    }

    public void populateFriendsArrayListAdapter(ArrayList<HangoutRequest> allHangoutsArrayList) {
        hangoutRequestArrayAdapter.addAll(allHangoutsArrayList);
        hangoutRequestArrayAdapter.notifyDataSetChanged();
    }


    AdapterView.OnItemClickListener onItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
            HangoutRequest hangoutRequest = (HangoutRequest) adapterView.getItemAtPosition(position);
            // showMessage(user.getName());

        }
    };

    private void showMessage(String message) {
        if(getActivity() != null)
          Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_new_hangout: {
                Log.e("btn ", "neeew");
                createHangoutPopup();
            }
            break;

            case R.id.imgbtn_date: {
                final Calendar c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog datePickerDialog = new DatePickerDialog(getContext(),
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year,
                                                  int monthOfYear, int dayOfMonth) {
                                edtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year);

                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();
            }
            break;
            case R.id.imgbtn_time: {
                // Get Current Time
                final Calendar c = Calendar.getInstance();
                mHour = c.get(Calendar.HOUR_OF_DAY);
                mMinute = c.get(Calendar.MINUTE);

                // Launch Time Picker Dialog
                TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(),
                        new TimePickerDialog.OnTimeSetListener() {

                            @Override
                            public void onTimeSet(TimePicker view, int hourOfDay,
                                                  int minute) {

                                edtTime.setText(hourOfDay + ":" + minute);
                            }
                        }, mHour, mMinute, false);
                timePickerDialog.show();

            }
            break;

            case R.id.btn_send_req: {
                HangoutRequest hangoutRequest = new HangoutRequest();
                hangoutRequest.setCreated_by_id(user_id);
                hangoutRequest.setDate(edtDate.getText().toString());
                hangoutRequest.setStart_time(edtTime.getText().toString());
                hangoutRequest.setTitle(title.getText().toString());
                hangoutRequest.setInvited(new ArrayList<String>(friendsSelected.subList(1, friendsSelected.size())));
                hangoutRequest.setPlace_id(placeSelected);
                //na2es el invites and place
                createRequest(hangoutRequest);
            }
            break;
        }
    }


    private void createRequest(HangoutRequest hangoutRequest) {

        String data = gson.toJson(hangoutRequest, HangoutRequest.class);
        Log.e("hangout request data", data);

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

                if (ok && message.contains("Hangout request created")) {
                    showMessage("Request sent.");
                }
            }
            @Override
            public void onFailure(Exception e) {
                    showMessage("Internal server error. Retry");
            }
        });
        asynchTaskPost.execute(Constants.CREATE_HANGOUT);
    }

    private int mYear, mMonth, mDay, mHour, mMinute;
    ImageButton date, time;
    Spinner location, friends;
    EditText edtDate, edtTime, title;
    Button sendReq;
    ArrayList<String> friendsNames = new ArrayList<>();
    ArrayList<String> places = new ArrayList<>();
    ArrayList<User> friendsObjects = new ArrayList<>();
    ArrayList<Place> placesObjects = new ArrayList<>();
    ArrayList<String> friendsSelected = new ArrayList<>();
    String placeSelected ="";

    private void createHangoutPopup() {
        LayoutInflater inflater = (LayoutInflater) getActivity().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //Inflate the view from a predefined XML layout (no need for root id, using entire layout)
        View layout = inflater.inflate(R.layout.activity_create_hangout_req, null);

        date = layout.findViewById(R.id.imgbtn_date);
        date.setOnClickListener(this);
        time = layout.findViewById(R.id.imgbtn_time);
        time.setOnClickListener(this);
        location = layout.findViewById(R.id.search_places);
        friends = layout.findViewById(R.id.search_friends);
        edtDate = layout.findViewById(R.id.edt_date);
        edtTime = layout.findViewById(R.id.edt_time);
        sendReq = layout.findViewById(R.id.btn_send_req);
        sendReq.setOnClickListener(this);
        title = layout.findViewById(R.id.edt_title);
        setUpPopup(layout);
        getFriendsNames();
        getPlacesNames();
    }

    AdapterView.OnItemSelectedListener onFriendSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            friendsSelected.add(friendsObjects.get(i).getId());
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };
    AdapterView.OnItemSelectedListener onPlacesSelectedListener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
            placeSelected  = placesObjects.get(i).getId();
        }

        @Override
        public void onNothingSelected(AdapterView<?> adapterView) {

        }
    };

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
                        places = new ArrayList<>();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            Place place = gson.fromJson(jsonArray.get(i).toString(), Place.class);
                            placesObjects.add(place);
                            places.add(place.getName());
                        }
                        ArrayAdapter adapter2 = new ArrayAdapter<String>(getContext(),
                                android.R.layout.simple_spinner_item, places);
                        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        location.setAdapter(adapter2);
                        location.setOnItemSelectedListener(onPlacesSelectedListener);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
            }

            @Override
            public void onFailure(Exception e) {
                showMessage("Internal server error.");
            }
        });
        asynchTaskGet.execute(Constants.GET_PLACES);
    }

    private void getFriendsNames() {
        String json = sharedPreferences.getString("user_info", "");
        gson = new Gson();
        user = gson.fromJson(json, User.class);

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
                        for (int i = 0; i < jsonArray.length(); i++) {
                            User u = gson.fromJson(jsonArray.get(i).toString(), User.class);
                            friendsObjects.add(u);
                            friendsNames.add(u.getName());
                        }
                        ArrayAdapter adapter = new ArrayAdapter<String>(getActivity().getApplicationContext(),
                                android.R.layout.simple_spinner_item, friendsNames);
                        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                        friends.setAdapter(adapter);
                        friends.setOnItemSelectedListener(onFriendSelectedListener);
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


    private void setUpPopup(View view) {
        //Get the devices screen density to calculate correct pixel sizes
        float density = getActivity().getResources().getDisplayMetrics().density;
        // create a focusable PopupWindow with the given layout and correct size
        final PopupWindow pw = new PopupWindow(view, (int) density * 330, (int) density * 340, true);
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

}

