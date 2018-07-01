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
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.saraelsheemi.pinmate.R;
import com.example.saraelsheemi.pinmate.controllers.AsynchTasks.AsynchTaskPut;
import com.example.saraelsheemi.pinmate.controllers.Constants;
import com.example.saraelsheemi.pinmate.controllers.EventListener;
import com.example.saraelsheemi.pinmate.models.User;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

public class UserInfoFragment extends Fragment implements View.OnClickListener{

    EditText name, birthDate, gender, email, phone, home;
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
        disableEditText();
    }

    private void init(View view) {
        name = view.findViewById(R.id.txt_placep_name);

        email = view.findViewById(R.id.txt_place_address);
        phone = view.findViewById(R.id.txt_place_phone);
        birthDate = view.findViewById(R.id.txt_user_birthday);
        gender = view.findViewById(R.id.txt_user_gender);
        home= view.findViewById(R.id.txt_user_home);
        editInfo= view.findViewById(R.id.btn_edit_userinfo);
        editInfo.setOnClickListener(this);
        editInfo.setTag("disabled");
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
                if(view.getTag().equals("disabled")) {
                    enableEditText();
                    view.setTag("enabled");
                }
                else {
                    disableEditText();
                    view.setTag("disabled");
                    updateUserInfo();
                }
            }
            break;

        }
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

        phone.setFocusable(false);
        phone.setEnabled(false);
        phone.setCursorVisible(false);
        phone.setBackgroundColor(Color.TRANSPARENT);

        home.setFocusable(false);
        home.setEnabled(false);
        home.setCursorVisible(false);
        home.setBackgroundColor(Color.TRANSPARENT);

    }
    private void enableEditText(){

        name.setFocusable(true);
        name.setFocusableInTouchMode(true);
        name.setEnabled(true);
        name.setCursorVisible(true);

        birthDate.setFocusable(true);
        birthDate.setFocusableInTouchMode(true);
        birthDate.setEnabled(true);
        birthDate.setCursorVisible(true);

        gender.setFocusable(true);
        gender.setEnabled(true);
        gender.setFocusableInTouchMode(true);
        gender.setCursorVisible(true);

        email.setFocusable(true);
        email.setEnabled(true);
        email.setFocusableInTouchMode(true);
        email.setCursorVisible(true);

        phone.setFocusable(true);
        phone.setEnabled(true);
        phone.setFocusableInTouchMode(true);
        phone.setCursorVisible(true);


        home.setFocusable(true);
        home.setEnabled(true);
        home.setCursorVisible(true);
        home.setFocusableInTouchMode(true);

    }

    private void updateUserInfo() {

        user.setName(name.getText().toString());
        user.setEmail(email.getText().toString());
        user.setBirth_date(birthDate.getText().toString());
        user.setGender(gender.getText().toString());
        user.setHome_address(home.getText().toString());
        user.setMobile_number(phone.getText().toString());

        final String newData = gson.toJson(user,User.class);

        AsynchTaskPut asynchTaskPut = new AsynchTaskPut(newData, getContext(), new EventListener<String>() {
            @Override
            public void onSuccess(String object) {
                JSONObject jsonObject;
                Boolean ok = false;
                try {
                    jsonObject = new JSONObject(object);
                    ok = jsonObject.getBoolean("ok");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (ok) {
                    showMessage("Info updated successfully.");
                    editor.clear();
                    sharedPreferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                    editor.putString("user_info",newData);
                    editor.apply();
                    getUserInfo();
                } else {
                    showMessage("Info not updated. Retry");
                }
            }

            @Override
            public void onFailure(Exception e) {
                showMessage("Internal server error.");
                Log.e("Update failed", "server error");
            }
        });

        asynchTaskPut.execute(Constants.UPDATE_USER + user.getId());
    }
    private void showMessage(String message) {
        Toast.makeText(getActivity(), message, Toast.LENGTH_SHORT).show();
    }
}
