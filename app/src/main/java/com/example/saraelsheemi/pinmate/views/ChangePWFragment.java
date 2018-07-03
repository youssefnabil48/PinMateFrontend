package com.example.saraelsheemi.pinmate.views;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.saraelsheemi.pinmate.R;
import com.example.saraelsheemi.pinmate.controllers.AsynchTasks.AsynchTaskPut;
import com.example.saraelsheemi.pinmate.controllers.Constants;
import com.example.saraelsheemi.pinmate.controllers.EventListener;
import com.example.saraelsheemi.pinmate.models.User;
import com.google.gson.Gson;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sara ElSheemi on 5/20/2018.
 */

public class ChangePWFragment extends Fragment implements View.OnClickListener {
    EditText old, newpw, cpw;
    Button save;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_change_password, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("Change Password");
        init(view);
    }

    private void init(View view) {
        old = view.findViewById(R.id.edt_old_pw);
        newpw = view.findViewById(R.id.edt_new_pw);
        cpw = view.findViewById(R.id.edt_confnew_pw);
        save = view.findViewById(R.id.btn_submit_new_pw);
        save.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_submit_new_pw: {
                if(checkIfMatch())
                    updatePw();
            }
        }
    }

    private boolean checkIfMatch() {
        boolean flag = true;
        if (newpw.getText().toString().length() > 0 && newpw.getText().toString().length() > 0 &&
                !cpw.getText().toString().equals(cpw.getText().toString())) {
            cpw.setError("Password doesn't match.");
            flag = false;
        }
        if(old.getText().toString().length()==0) {
            old.setError("Old password is required.");
            flag= false;
        }
        return flag;
    }

    private void updatePw() {
        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.apply();
        Gson gson = new Gson();
        String json = sharedPreferences.getString("user_info", "");
        User user = gson.fromJson(json, User.class);

        String token = "{\"password\":\"" + newpw.getText().toString() + "\"}";

        AsynchTaskPut asynchTaskPut = new AsynchTaskPut(token, getContext(), new EventListener<String>() {
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
                    Toast.makeText(getContext(),"Password updated",Toast.LENGTH_SHORT).show();
                    Log.e("Update", "password updated successfully");
                } else {
                    Toast.makeText(getContext(),"Password not updated. retry",Toast.LENGTH_SHORT).show();
                    Log.e("Update failed", "password not updated");
                }
            }

            @Override
            public void onFailure(Exception e) {
                Log.e("Update failed", "server error");
            }
        });
        asynchTaskPut.execute(Constants.UPDATE_USER + user.getId());
    }
}
