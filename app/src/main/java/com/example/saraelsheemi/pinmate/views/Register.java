package com.example.saraelsheemi.pinmate.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.saraelsheemi.pinmate.R;
import com.squareup.picasso.Picasso;

public class Register extends AppCompatActivity implements View.OnClickListener {
    ImageView logo;
    Button register,login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    private void init() {
        logo = (ImageView) findViewById(R.id.img_logo);
        Picasso.with(getApplicationContext()).load(R.drawable.image).resize(100,100).into(logo);
        register = (Button) findViewById(R.id.btn_reg_reg);
        login = (Button) findViewById(R.id.btn_already_member);

    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.btn_already_member : {
                Intent intent = new Intent(this,Login.class);
                startActivity(intent);
            }
                break;
            case R.id.btn_reg_reg :
                break;

        }
    }
}
