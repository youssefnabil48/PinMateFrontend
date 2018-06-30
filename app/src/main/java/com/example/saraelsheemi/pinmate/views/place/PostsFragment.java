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
import android.widget.Toast;

import com.example.saraelsheemi.pinmate.R;
import com.example.saraelsheemi.pinmate.controllers.AsynchTaskDelete;
import com.example.saraelsheemi.pinmate.controllers.AsynchTaskGet;
import com.example.saraelsheemi.pinmate.controllers.AsynchTaskPost;
import com.example.saraelsheemi.pinmate.controllers.Constants;
import com.example.saraelsheemi.pinmate.controllers.EventListener;
import com.example.saraelsheemi.pinmate.controllers.adapters.PostsAdapter;
import com.example.saraelsheemi.pinmate.models.Place;
import com.example.saraelsheemi.pinmate.models.Post;
import com.example.saraelsheemi.pinmate.models.User;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;

public class PostsFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, AdapterView.OnItemLongClickListener {
    private ListView listViewPosts;
    private ArrayList<Post> posts;
    private ArrayAdapter<Post> postArrayAdapter;
    ProgressBar progressBar;
    SwipeRefreshLayout swipeRefreshLayout;
    Gson gson;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Place place;
    Button addPost;
    EditText newContent;
    String deletePostId;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_posts_list_view, container, false);

    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        init(view);
        getPosts();
        super.onViewCreated(view, savedInstanceState);
    }

    public void init(View view) {
        gson = new Gson();
        swipeRefreshLayout = view.findViewById(R.id.swiperefresh_posts);
        swipeRefreshLayout.setOnRefreshListener(this);
        progressBar = view.findViewById(R.id.progressbar_posts_loading);
        listViewPosts = view.findViewById(R.id.listView_posts);

        postArrayAdapter = new PostsAdapter(getActivity(), R.layout.activity_posts_list_item, new ArrayList<Post>(), this);
        listViewPosts.setAdapter(postArrayAdapter);

        listViewPosts.setLongClickable(true);
        listViewPosts.setOnItemLongClickListener(this);
        registerForContextMenu(listViewPosts);

        sharedPreferences = getActivity().getSharedPreferences("placeInfo", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();

        newContent = view.findViewById(R.id.edt_new_post);
        addPost = view.findViewById(R.id.btn_new_post);
        addPost.setOnClickListener(AddPostListener);

    }


    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        Post p = (Post) adapterView.getItemAtPosition(i);
        deletePostId = p.getId();
        listViewPosts.showContextMenu();

        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.post_menu, menu);
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.remove_item: {
                deletePost(deletePostId);
            }
            break;
        }
        return true;
    }

    //called when first time opening list view
    private void getPosts() {
        String json = sharedPreferences.getString("place_details", "");
        gson = new Gson();
        place = gson.fromJson(json, Place.class);
        posts = place.getPosts();
       // sortData(posts);
        populatePostsArrayListAdapter(posts);

    }

    //delete old list and get new one
    public void populatePostsArrayListAdapter(ArrayList<Post> postArrayList) {
        postArrayAdapter.clear();
        postArrayAdapter.addAll(postArrayList);
        postArrayAdapter.notifyDataSetChanged();
    }

    private void getUpdatedPosts() {

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
                        posts = p.getPosts();
                     //   sortData(posts);
                        populatePostsArrayListAdapter(posts);

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

    // sort post according to date
    private void sortData(ArrayList<Post> posts) {
        Collections.sort(posts, new Comparator<Post>() {
            @Override
            public int compare(Post data1, Post data2) {
                if (data1.getCreated_at().compareTo(data2.getCreated_at()) <= 0)
                    return 1;
                else
                    return 0;
            }
        });

    }

    @Override
    public void onRefresh() {
        getUpdatedPosts();
    }


    public void deletePost(String postId) {

        Log.e("post id", postId);
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
                if (ok && message.contains("Post deleted")) {
                    showMessage("Post deleted.");
                    getUpdatedPosts();
                } else if (ok && message.contains("Post was not")) {
                    showMessage("Post not deleted. Retry");
                }

            }

            @Override
            public void onFailure(Exception e) {
                showMessage("Internal server error. Retry.");
            }
        });
        asynchTaskDelete.execute(Constants.DELETE_POST + postId);
    }

    View.OnClickListener AddPostListener = new View.OnClickListener() {
        @Override
        public void onClick(View view) {

            newContent.setHint("Enter your post here...");
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
                    + "\"user\":" + "\"" + user_id + "\","
                    + "\"user_name\":\"" + user.getName() + "\","
                    + "\"user_pic\":\"" + user.getPicture() + "\","
                    + "\"created_at\":" + "\"" + strDate + "\","
                    + "\"content\":" + "\"" + newContent.getText().toString() + "\""
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
                    if (ok && message.contains("Post was not added")) {
                        showMessage("Post not added. Retry.");
                    } else if (ok && message.contains("Post added")) {
                        getUpdatedPosts();
                        showMessage("Post added");
                    }
                }

                @Override
                public void onFailure(Exception e) {
                    showMessage("Post not added. Internal server error");
                }
            });
            asynchTaskPost.execute(Constants.CREATE_POST);
        }
    };


    private void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }

}
