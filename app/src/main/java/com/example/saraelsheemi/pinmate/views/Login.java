package com.example.saraelsheemi.pinmate.views;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.saraelsheemi.pinmate.R;
import com.example.saraelsheemi.pinmate.controllers.AsynchTaskGet;
import com.example.saraelsheemi.pinmate.controllers.AsynchTaskPost;
import com.example.saraelsheemi.pinmate.controllers.Constants;
import com.example.saraelsheemi.pinmate.controllers.EventListener;
import com.example.saraelsheemi.pinmate.models.MResponse;
import com.example.saraelsheemi.pinmate.models.User;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class Login extends AppCompatActivity implements View.OnClickListener {
    Button signUp, signIn;
    ImageView logo;
    EditText edtEmail, edtPassword;
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    CheckBox rememberMe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
        checkIfRemembered();
    }

    private void init() {
        logo = findViewById(R.id.img_logo);
        Picasso.get().load(R.drawable.image).resize(100, 100).into(logo);
        signUp = findViewById(R.id.btn_register);
        signIn = findViewById(R.id.btn_login);
        signIn.setOnClickListener(this);
        signUp.setOnClickListener(this);
        edtEmail = findViewById(R.id.edt_email_login);
        edtPassword = findViewById(R.id.edt_password_login);
        rememberMe = findViewById(R.id.chk_remember);
        rememberMe.setOnClickListener(this);
        sharedPreferences = getSharedPreferences("userInfo", Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
        editor.apply();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_register: {
                Intent intent = new Intent(this, Register.class);
                startActivity(intent);
            }
            break;

            case R.id.btn_login: {
                if (checkIfValid()) {
                    User user = new User();
                    user.setEmail(edtEmail.getText().toString());
                    user.setPassword(edtPassword.getText().toString());
                    sendLoginData(user);
                }
            }
            break;
            case R.id.chk_remember: {
                keepLogged();
            }
            break;

        }
    }

    private boolean checkIfRemembered() {
        //    if (sharedPreferences.getString("keeploggedin", "") == "true") {
        Log.e("logged in", sharedPreferences.getString("keeploggedin", ""));
        //    return true;
        //  }
        return false;
    }

    private void keepLogged() {
        if (rememberMe.isChecked()) {
            editor.putString("keeploggedin", "true");
            editor.apply();
            showMessage(sharedPreferences.getString("keeploggedin", ""));
        }
    }

    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }

    private void getLoginData() {

    }

    private void sendLoginData(final User user) {
        Gson gson = new Gson();
        String json = gson.toJson(user, User.class);
        AsynchTaskPost asynchTaskPost = new AsynchTaskPost(json, getApplicationContext(), new EventListener<String>() {

            @Override
            public void onSuccess(String object) {
                JSONObject jsonObject = null;
                String message = "";
                String data="";
                String user_token = "";
                Boolean ok = false;

                try {
                    jsonObject = new JSONObject(object);
                    ok = jsonObject.getBoolean("ok");
                    message = jsonObject.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                if (ok && message.contains("user loggedin")) {
                    try {
                        user_token = jsonObject.getJSONObject("data").getString("token");
                        editor.putString("user_token", user_token);
                        editor.apply();
                        showMessage(user_token);
                        Log.e("user_token", sharedPreferences.getString("user_token", ""));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Intent intent = new Intent(getApplicationContext(), Home.class);
                    intent.putExtra("user_token",user_token);
                    startActivity(intent);

                } else if (ok && message.contains("no user")) {
                    showMessage("No user found.");
                } else if (ok && message.contains("wrong email")) {
                    showMessage("Wrong email or password.");
                }
            }

            @Override
            public void onFailure(Exception e) {
                showMessage("Internal error. Please retry.");
            }
        });
        asynchTaskPost.execute(Constants.LOGIN_URL);

    }

    private boolean checkIfValid() {
        boolean flag = true;
        if (edtEmail.getText().toString().length() == 0) {
            edtEmail.setError("Enter your email.");
            flag = false;
        }
        if (edtEmail.getText().toString().length() > 0 && !validEmail(edtEmail.getText().toString())) {
            edtEmail.setError("Invalid email.");
            flag = false;
        }
        if (edtPassword.getText().toString().length() == 0) {
            edtPassword.setError("Enter your password.");
            flag = false;
        }
        return flag;
    }

    private boolean validEmail(String email) {

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }
}