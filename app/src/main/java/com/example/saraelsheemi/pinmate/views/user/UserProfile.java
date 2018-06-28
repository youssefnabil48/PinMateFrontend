package com.example.saraelsheemi.pinmate.views.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.saraelsheemi.pinmate.R;
import com.example.saraelsheemi.pinmate.controllers.MLRoundedImageView;
import com.example.saraelsheemi.pinmate.controllers.PagerAdapter;
import com.example.saraelsheemi.pinmate.models.User;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;


public class UserProfile extends Fragment{
    MLRoundedImageView userPicture;
    ImageView userCoverPicture;
    TextView userName;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Gson gson;
    User user;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().setTitle("Profile");
        return inflater.inflate(R.layout.activity_user_profile,container,false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        init(view);
    }
    private void init(View view) {

        userCoverPicture = view.findViewById(R.id.img_userp_cover);
        userPicture = view.findViewById(R.id.imground_user_profile);
        userName = view.findViewById(R.id.txt_placep_name);
        viewPager =   view.findViewById(R.id.user_viewpager);
        setUpViewPager(viewPager);
        tabLayout =  view.findViewById(R.id.user_tabs);
        tabLayout.setupWithViewPager(viewPager);
        setUpTabIcons();

        sharedPreferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
        getUserInfo();

    }

    private void getUserInfo() {
        String json = sharedPreferences.getString("user_info","");
        gson = new Gson();
        user = gson.fromJson(json,User.class);
        userName.setText(user.getName());
        if(user.getPicture() != null)
            Picasso.get().load(user.getPicture()).into(userPicture);


    }
    private void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
    private void setUpTabIcons(){
        tabLayout.getTabAt(0).setText("BIO");
        tabLayout.getTabAt(1).setText("FRIENDS");
        tabLayout.getTabAt(2).setText("HANGOUTS");
    }
    private void setUpViewPager(ViewPager viewPager){
        PagerAdapter adapter = new PagerAdapter(getFragmentManager(),getContext());
        adapter.addFragment(new UserInfoFragment(),"one");
        adapter.addFragment(new FriendListFragment(),"one");
        adapter.addFragment(new HangoutRequestsFragment(),"one");
        viewPager.setAdapter(adapter);
    }
}

