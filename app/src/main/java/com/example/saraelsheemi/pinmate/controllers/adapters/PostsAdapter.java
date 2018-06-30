package com.example.saraelsheemi.pinmate.controllers.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.saraelsheemi.pinmate.R;
import com.example.saraelsheemi.pinmate.controllers.AsynchTaskGet;
import com.example.saraelsheemi.pinmate.models.Post;
import com.example.saraelsheemi.pinmate.models.User;
import com.example.saraelsheemi.pinmate.views.place.PostsFragment;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class PostsAdapter  extends ArrayAdapter<Post> {

    AsynchTaskGet asynchTaskGet;
    User user;
    Gson gson = new Gson();
    PostsFragment fragment;
    Post post;
    ArrayList<Post> posts;

    public PostsAdapter(@NonNull Context context, int resource, ArrayList<Post> posts, PostsFragment fragment) {
        super(context, resource, posts);
        this.fragment = fragment;
        this.posts = posts;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView userName;
        TextView content;
        TextView date;
        ImageView userPicture;
        ImageButton deletePost;


        //convert view == one row
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.activity_posts_list_item, parent, false);
        }

        userName = listItemView.findViewById(R.id.txt_fav_place_list_name);
        content = listItemView.findViewById(R.id.txt_fav_place_list_description);
        userPicture = listItemView.findViewById(R.id.img_fav_place_list_picture);
        date = listItemView.findViewById(R.id.txt_post_date);
 //       deletePost = listItemView.findViewById(R.id.btn_delete_post);

//        deletePost.setOnClickListener(DeletePost);
        post = getItem(position);

        userName.setText(post.getUser_name());
        content.setText(post.getContent());
        date.setText(post.getCreated_at());
        Log.e("date",post.getCreated_at());

        if (post.getUser_pic() != null)
            Picasso.get().load(post.getUser_pic()).into(userPicture);

        return listItemView;
    }


//    View.OnClickListener DeletePost = new View.OnClickListener() {
//        @Override
//        public void onClick(View view) {
//          Log.e("post 2 id", post.getId());
//            fragment.deletePost(post.getId());
//        }
//    };
}

