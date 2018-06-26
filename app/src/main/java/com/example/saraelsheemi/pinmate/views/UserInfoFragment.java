package com.example.saraelsheemi.pinmate.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.saraelsheemi.pinmate.R;
import com.example.saraelsheemi.pinmate.models.User;
import com.google.gson.Gson;

public class UserInfoFragment extends Fragment implements View.OnClickListener{

    TextView name, birthDate, gender, email, password, home;
    ImageButton editInfo;
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
    }

    private void init(View view) {
        name = view.findViewById(R.id.txt_user_name);

        email = view.findViewById(R.id.txt_user_email);
        password = view.findViewById(R.id.txt_user_password);
        birthDate = view.findViewById(R.id.txt_user_birthday);
        gender = view.findViewById(R.id.txt_user_gender);
        home= view.findViewById(R.id.txt_user_home);
        editInfo= view.findViewById(R.id.btn_edit_userinfo);
        sharedPreferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
        getUserInfo();


    }
    private void getUserInfo() {
        String json = sharedPreferences.getString("user_info","");
        gson = new Gson();
        user = gson.fromJson(json,User.class);
        name.setText(user.getName());
        email.setText(user.getEmail());
        birthDate.setText(user.getBirth_date());
        gender.setText(user.getGender());
        home.setText(user.getHome_address());

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_edit_userinfo : {

            }
            break;

        }
    }
}
