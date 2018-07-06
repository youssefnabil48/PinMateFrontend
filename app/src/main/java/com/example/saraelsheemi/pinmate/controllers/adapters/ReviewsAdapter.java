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
import android.widget.RatingBar;
import android.widget.TextView;

import com.example.saraelsheemi.pinmate.R;
import com.example.saraelsheemi.pinmate.controllers.AsynchTasks.AsynchTaskGet;
import com.example.saraelsheemi.pinmate.models.Post;
import com.example.saraelsheemi.pinmate.models.Review;
import com.example.saraelsheemi.pinmate.models.User;
import com.example.saraelsheemi.pinmate.views.place.PostsFragment;
import com.example.saraelsheemi.pinmate.views.place.ReviewFragment;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ReviewsAdapter extends ArrayAdapter<Review> {
    AsynchTaskGet asynchTaskGet;
    User user;
    Gson gson = new Gson();
    ReviewFragment fragment;
    Review review;
    ArrayList<Review> reviews;

    public ReviewsAdapter(@NonNull Context context, int resource, ArrayList<Review> reviews, ReviewFragment fragment) {
        super(context, resource, reviews);
        this.fragment = fragment;
        this.reviews = reviews;
    }

    @Override
    public long getItemId(int position) {
        return super.getItemId(position);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        TextView userName;
        ImageView userPicture;
        TextView content;
        TextView date;
        RatingBar ratingBar;
        TextView ratingCount;


        //convert view == one row
        // Check if the existing view is being reused, otherwise inflate the view
        View listItemView = convertView;
        if (listItemView == null) {
            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.activity_review_list_item, parent, false);
        }

        userName = listItemView.findViewById(R.id.txt_fav_place_list_name);
        content = listItemView.findViewById(R.id.txt_fav_place_list_description);
        userPicture = listItemView.findViewById(R.id.img_fav_place_list_picture);
        date = listItemView.findViewById(R.id.txt_post_date);
        ratingCount = listItemView.findViewById(R.id.ratingCount);

        //       deletePost = listItemView.findViewById(R.id.btn_delete_post);

//        deletePost.setOnClickListener(DeletePost);
        review = getItem(position);

        assert review != null;
        userName.setText(review.getUsername());
        content.setText(review.getContent());
        date.setText(review.getCreated_at());
        ratingCount.setText(String.valueOf(review.getRating()));
        if (review.getUserPic() != null)
            Picasso.get().load(review.getUserPic()).into(userPicture);

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
