package com.example.saraelsheemi.pinmate.views;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.example.saraelsheemi.pinmate.R;
import com.example.saraelsheemi.pinmate.controllers.AsynchTasks.AsynchTaskDelete;
import com.example.saraelsheemi.pinmate.controllers.Constants;
import com.example.saraelsheemi.pinmate.controllers.EventListener;
import com.example.saraelsheemi.pinmate.models.User;
import com.google.gson.Gson;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Sara ElSheemi on 5/18/2018.
 */

public class AccountSettingsFragment extends Fragment implements View.OnClickListener {
    Button changePW, deactivateAcc, blockedUsers;
    Fragment fragment = null;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_account_settings, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init(view);
        getActivity().setTitle("Account Settings");
    }

    private void init(View view) {
        changePW = view.findViewById(R.id.btn_change_pw);
        changePW.setOnClickListener(this);
        deactivateAcc = view.findViewById(R.id.btn_deactivate);
        deactivateAcc.setOnClickListener(this);
//        blockedUsers = view.findViewById(R.id.btn_blocked_users);
//        blockedUsers.setOnClickListener(this);
        sharedPreferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();

    }

    private void deactivateAcc() {
        String json = sharedPreferences.getString("user_info", "");
        Gson gson = new Gson();
        User user = gson.fromJson(json, User.class);

        AsynchTaskDelete asynchTaskDelete = new AsynchTaskDelete("", getContext(), new EventListener<String>() {
            @Override
            public void onSuccess(String object) {
                JSONObject jsonObject = null;
                String message = "";
                JSONArray jsonArray;
                Boolean ok = false;

                try {
                    jsonObject = new JSONObject(object);
                    ok = jsonObject.getBoolean("ok");
                    message = jsonObject.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (ok && message.contains("User deleted successfully")) {
                    Toast.makeText(getContext(), "Account deactivated", Toast.LENGTH_SHORT);
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("userInfo", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.clear();
                    editor.apply();
                    Intent intent = new Intent(getActivity(), Login.class);
                    startActivity(intent);

                } else if (ok && message.contains("User is not"))
                    Toast.makeText(getContext(), "Account not deactivated. Retry", Toast.LENGTH_SHORT);
            }

            @Override
            public void onFailure(Exception e) {
                Toast.makeText(getContext(), "Internal server error. Retry", Toast.LENGTH_SHORT);
            }
        });
        asynchTaskDelete.execute(Constants.DELETE_USER + user.getId());
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btn_change_pw: {
                fragment = new ChangePWFragment();
                    FragmentTransaction ft = getFragmentManager().beginTransaction();
                    ft.replace(R.id.content_frame, fragment);
                    ft.addToBackStack(null);
                    ft.commit();
            }
            break;
            case R.id.btn_deactivate: {
                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
                alertDialogBuilder.setMessage("Are you sure you want to deactivate your account?");
                alertDialogBuilder.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface arg0, int arg1) {
                                deactivateAcc();
                            }
                        });

                alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });

                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();
            }
            break;
//            case R.id.btn_blocked_users: {
//                fragment = new BlockedFragment();
//            }
//                break;
        }

    }
}
