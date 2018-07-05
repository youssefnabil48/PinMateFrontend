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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.saraelsheemi.pinmate.R;
import com.example.saraelsheemi.pinmate.controllers.AsynchTasks.AsynchTaskDelete;
import com.example.saraelsheemi.pinmate.controllers.AsynchTasks.AsynchTaskGet;
import com.example.saraelsheemi.pinmate.controllers.AsynchTasks.AsynchTaskPost;
import com.example.saraelsheemi.pinmate.controllers.Constants;
import com.example.saraelsheemi.pinmate.controllers.EventListener;
import com.example.saraelsheemi.pinmate.controllers.adapters.PostsAdapter;
import com.example.saraelsheemi.pinmate.controllers.adapters.ReviewsAdapter;
import com.example.saraelsheemi.pinmate.models.Place;
import com.example.saraelsheemi.pinmate.models.Post;
import com.example.saraelsheemi.pinmate.models.Review;
import com.example.saraelsheemi.pinmate.models.User;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class ReviewFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemLongClickListener{
    private ListView listViewReviews;
    private ArrayList<Review> reviews;
    private ArrayAdapter<Review> reviewArrayAdapter;
    ProgressBar progressBar;
    SwipeRefreshLayout swipeRefreshLayout;
    Gson gson;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Place place;
    Button submitReviewButton;
    EditText newReviewContent;
    RatingBar reviewRatingBar;
    String newReviewRating;
    String deletedReviewId;
    User loggedInUser;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_review_list_view, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        init(view);
        getReviews();
        super.onViewCreated(view, savedInstanceState);
    }

    public void init(View view) {
        gson = new Gson();
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh_reviews);
        swipeRefreshLayout.setOnRefreshListener(this);
        progressBar = view.findViewById(R.id.progressbar_review_loading);
        listViewReviews = view.findViewById(R.id.listView_reviews);

        reviewArrayAdapter = new ReviewsAdapter(getActivity(), R.layout.activity_review_list_item, new ArrayList<Review>(), this);
        listViewReviews.setAdapter(reviewArrayAdapter);

        listViewReviews.setLongClickable(true);
        listViewReviews.setOnItemLongClickListener(this);
        registerForContextMenu(listViewReviews);

        sharedPreferences = getActivity().getSharedPreferences("placeInfo", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();

        newReviewContent = view.findViewById(R.id.edt_new_review_content);
        submitReviewButton = view.findViewById(R.id.btn_new_review);
        submitReviewButton.setOnClickListener(AddReviewListner);

        reviewRatingBar = view.findViewById(R.id.ratingBar);


        //get current logged in user
        sharedPreferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
        String json = sharedPreferences.getString("user_info","");
        loggedInUser = gson.fromJson(json,User.class);
        //if rating value is changed,
        //display the current rating value in the result (textview) automatically
        reviewRatingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            public void onRatingChanged(RatingBar ratingBar, float rating,
                                        boolean fromUser) {
                showMessage(String.valueOf(rating));
                newReviewRating = String.valueOf(rating);
            }
        });

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        Review p = (Review) adapterView.getItemAtPosition(i);
        deletedReviewId = p.getId();
        listViewReviews.showContextMenu();
        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.review_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.remove_review_item: {
                deleteReview(deletedReviewId);
                return true;
            }
        }
        return false;
    }

    //called when first time opening list view
    private void getReviews() {
        String json = sharedPreferences.getString("place_details", "");
        gson = new Gson();
        place = gson.fromJson(json, Place.class);
        reviews = place.getReviews();
        // sortData(posts);
        populateReviewsArrayListAdapter(reviews);

    }

    //delete old list and get new one
    public void populateReviewsArrayListAdapter(ArrayList<Review> reviewArrayList) {
        reviewArrayAdapter.clear();
        reviewArrayAdapter.addAll(reviewArrayList);
        reviewArrayAdapter.notifyDataSetChanged();
    }

    private void getUpdatedReviews() {

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

                if (ok && message.contains("Place loaded")) {
                    showMessage("Updated");
                    try {
                        placeinfo = jsonObject.getString("data");
                        Place p = gson.fromJson(placeinfo, Place.class);
                        reviews = p.getReviews();
                        //   sortData(posts);
                        populateReviewsArrayListAdapter(reviews);

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
        getUpdatedReviews();
    }

    public void deleteReview(String reviewId) {

        Log.e("review id", reviewId);
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
                if (ok) {
                    showMessage("Post deleted.");
                    getUpdatedReviews();
                } else if (ok && message.contains("Review was not")) {
                    showMessage("Review not deleted. Retry");
                }

            }

            @Override
            public void onFailure(Exception e) {
                showMessage("Internal server error. Retry.");
            }
        });
        asynchTaskDelete.execute(Constants.DELETE_Review + reviewId);
    }

    View.OnClickListener AddReviewListner = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            //get place id
            String place_id = place.getId();

            //get the user
            sharedPreferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
            editor = sharedPreferences.edit();
            editor.apply();
            String json = sharedPreferences.getString("user_info", "");
            gson = new Gson();
            User user = gson.fromJson(json, User.class);
            String user_id = user.getId();

            //get current time
            SimpleDateFormat mdformat = new SimpleDateFormat("dd MM yyyy, HH:mm");
            String strDate = mdformat.format(Calendar.getInstance().getTime());

            //post json to send to backend
            String postData = "{ \"place_id\":" + "\"" + place_id + "\","
                    + "\"user_id\":" + "\"" + user_id + "\","
                    + "\"user_name\":\"" + user.getName() + "\","
                    + "\"user_pic\":\"" + user.getPicture() + "\","
                    + "\"created_at\":" + "\"" + strDate + "\","
                    + "\"rating\":" + "\"" + newReviewRating + "\","
                    + "\"content\":" + "\"" + newReviewContent.getText().toString() + "\""
                    + "}";

            Log.e("post data", postData);
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
                    if (ok && message.contains("Review was not added")) {
                        showMessage("Post not added. Retry.");
                    } else if (ok) {
                        getUpdatedReviews();
                        showMessage("Post added");
                    }
                }

                @Override
                public void onFailure(Exception e) {
                    showMessage("Review not added. Internal server error");
                }
            });
            asynchTaskPost.execute(Constants.CREATE_REVIEW);
        }
    };


    private void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
