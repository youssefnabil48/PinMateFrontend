package com.example.saraelsheemi.pinmate.views;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.saraelsheemi.pinmate.R;
import com.example.saraelsheemi.pinmate.controllers.MLRoundedImageView;
import com.example.saraelsheemi.pinmate.controllers.PagerAdapter;


public class UserProfile extends Fragment{
    MLRoundedImageView userPicture;
    ImageView userCoverPicture;
    private TabLayout tabLayout;
    private ViewPager viewPager;


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

        viewPager =   view.findViewById(R.id.user_viewpager);
        setUpViewPager(viewPager);
        tabLayout =  view.findViewById(R.id.user_tabs);
        tabLayout.setupWithViewPager(viewPager);
        setUpTabIcons();

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
        adapter.addFragment(new Chats(),"one");
        adapter.addFragment(new Notifications(),"one");
        viewPager.setAdapter(adapter);
    }
}

