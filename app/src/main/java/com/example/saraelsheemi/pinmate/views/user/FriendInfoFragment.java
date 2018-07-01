package com.example.saraelsheemi.pinmate.views.user;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.example.saraelsheemi.pinmate.R;
import com.example.saraelsheemi.pinmate.models.User;
import com.google.gson.Gson;

public class FriendInfoFragment  extends Fragment{

    EditText name, birthDate, gender, email, phone, home;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    Gson gson;
    User user;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_user_info, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        disableEditText();
    }

    private void init(View view) {
        name = view.findViewById(R.id.txt_placep_name);
        email = view.findViewById(R.id.txt_place_address);
        birthDate = view.findViewById(R.id.txt_user_birthday);
        gender = view.findViewById(R.id.txt_user_gender);
        view.findViewById(R.id.img_userp_home_icon).setVisibility(View.GONE);
        view.findViewById(R.id.txt_user_home).setVisibility(View.GONE);
        view.findViewById(R.id.txt_place_phone).setVisibility(View.GONE);
        view.findViewById(R.id.img_place_phone_icon).setVisibility(View.GONE);

        sharedPreferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
        getUserInfo("friend_details");

    }
    public void getUserInfo(String shared) {
        String json = sharedPreferences.getString(shared,"");
        Log.e("json",json);
        gson = new Gson();
        user = gson.fromJson(json,User.class);
        name.setText(user.getName());
        email.setText(user.getEmail());
        birthDate.setText(user.getBirth_date());
        gender.setText(user.getGender());


    }

    private void disableEditText() {
        name.setFocusable(false);
        name.setEnabled(false);
        name.setCursorVisible(false);
        name.setBackgroundColor(Color.TRANSPARENT);

        birthDate.setFocusable(false);
        birthDate.setEnabled(false);
        birthDate.setCursorVisible(false);
        birthDate.setBackgroundColor(Color.TRANSPARENT);

        gender.setFocusable(false);
        gender.setEnabled(false);
        gender.setCursorVisible(false);
        gender.setBackgroundColor(Color.TRANSPARENT);

        email.setFocusable(false);
        email.setEnabled(false);
        email.setCursorVisible(false);
        email.setBackgroundColor(Color.TRANSPARENT);



    }

    private void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }


}
