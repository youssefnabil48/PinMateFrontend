package com.example.saraelsheemi.pinmate.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.saraelsheemi.pinmate.R;
import com.example.saraelsheemi.pinmate.controllers.AsynchTaskPost;
import com.example.saraelsheemi.pinmate.controllers.Constants;
import com.example.saraelsheemi.pinmate.controllers.EventListener;
import com.example.saraelsheemi.pinmate.models.MLocation;
import com.example.saraelsheemi.pinmate.models.MResponse;
import com.example.saraelsheemi.pinmate.models.User;
import com.google.gson.Gson;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Pattern;

public class Register extends AppCompatActivity implements  View.OnClickListener {
    ImageView logo;
    Button register,login;
    EditText edtName, edtEmail, edtPw, edtCPw;
    RadioGroup rgGender;
    String  gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    private void init() {
        logo =  findViewById(R.id.img_logo);
        Picasso.get().load(R.drawable.image).resize(100,100).into(logo);
        register =  findViewById(R.id.btn_reg_reg);
        register.setOnClickListener(this);
        login =  findViewById(R.id.btn_already_member);
        edtName = findViewById(R.id.edt_username_reg);
        edtEmail = findViewById(R.id.edt_email_login);
        edtPw = findViewById(R.id.edt_password_login);
        edtCPw = findViewById(R.id.edt_password_confirm);
        rgGender = findViewById(R.id.radiogp_gender);
        rgGender.setOnCheckedChangeListener(onCheckedChangeListener);


    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_already_member : {
                Intent intent = new Intent(this,Login.class);
                startActivity(intent);
            }
                break;
            case R.id.btn_reg_reg : {
                    if(checkIfValid()) {
                        User user = new User();
                        user.setName(edtName.getText().toString());
                        user.setEmail(edtEmail.getText().toString());
                        user.setPassword(edtPw.getText().toString());
                        user.setGender(gender);
                        user.setCurrent_location(new MLocation());
                        user.getCurrent_location().setLongitude(Constants.DEFAULT_LONTIDUE);
                        user.getCurrent_location().setLatitude(Constants.DEFAULT_LATITUDE);
                        user.setNotification_token("");
                        sendData(user);
                    }
            }
                break;

        }
    }

    private void sendData(User user) {
        final Gson gson = new Gson();
        String json = gson.toJson(user,User.class);
        AsynchTaskPost asynchTaskPost = new AsynchTaskPost(json,getApplicationContext(), new EventListener<String>() {

            @Override
            public void onSuccess(String object) {
                JSONObject jsonObject = null;
                String message="";
                Boolean ok=false;

                if(object != null ) {
                    try {
                        jsonObject = new JSONObject(object);
                         ok= jsonObject.getBoolean("ok");
//                        Gson gson1 = new Gson();
//                        User user = gson1.fromJson(jsonObject.getString("data"),User.class);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
                if(ok) {
                    showMessage("Account created successfully.");
                    Intent intent = new Intent(getApplicationContext(),Login.class);
                    startActivity(intent);
                } else {
                    showMessage("Failed to create account. Please retry.");
                }

            }
            @Override
            public void onFailure(Exception e) {
                        showMessage("Internal error. Please retry.");
            }
        });
        asynchTaskPost.execute(Constants.REGISTER_URL);
        Log.e("json",json);
    }
    private void showMessage(String message) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
    }
    private boolean validEmail(String email) {

        return Pattern.compile("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$").matcher(email).matches();
    }
    private boolean checkIfValid(){
        boolean flag = true;
        if(edtName.getText().toString().length()==0) {
            edtName.setError("Name is required.");
            flag= false;
        }
        if(edtEmail.getText().toString().length()==0) {
            edtEmail.setError("Email is required.");
            flag= false;
        }
        if(edtEmail.getText().toString().length()>0 && !validEmail(edtEmail.getText().toString())) {
            edtEmail.setError("Valid email is required.");
            flag= false;
        }
        if(edtPw.getText().toString().length()==0) {
            edtPw.setError("Password is required.");
            flag= false;
        }
        if(edtPw.getText().toString().length()<8) {
            edtPw.setError("Min 8 characters.");
            flag= false;
        }
        if(edtCPw.getText().toString().length()==0) {
            edtCPw.setError("Confirm password is required.");
            flag= false;
        }
        if(edtCPw.getText().toString().length()>0 && edtPw.getText().toString().length()>0 &&
        !edtCPw.getText().toString().equals(edtPw.getText().toString())) {
            edtCPw.setError("Password doesn't match.");
            flag= false;
        }
        RadioButton radioButton = findViewById(R.id.radio_female);
        if(rgGender.getCheckedRadioButtonId() == -1) {
            radioButton.setError("Gender is required");
            flag= false;
        }
        radioButton.setError(null);
        return  flag;
    }
    RadioGroup.OnCheckedChangeListener onCheckedChangeListener = new RadioGroup.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(RadioGroup radioGroup,int checkedId) {
            RadioButton rb =  rgGender.findViewById(checkedId);
            switch (rb.getId()) {
                case R.id.radio_female: {
                    gender = "female";

                }
                    break;
                case R.id.radio_male: {
                    gender = "male";
                }
                    break;

            }
        }
    };


}
