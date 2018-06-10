package com.example.saraelsheemi.pinmate.views;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.example.saraelsheemi.pinmate.R;
import com.squareup.picasso.Picasso;

public class Login extends AppCompatActivity {
    Button signUp,signIn;
    ImageView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        init();
    }

    private void init() {
        logo = (ImageView) findViewById(R.id.img_logo);
        Picasso.get().load(R.drawable.image).resize(100,100).into(logo);
        signUp = (Button) findViewById(R.id.btn_register);
        signIn = (Button) findViewById(R.id.btn_login);
        final Intent intent1 = new Intent(this, Home.class);
        final Intent intent2 = new Intent(this, Register.class);
        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                startActivity(intent1);
            }
        });
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent2);
            }
        });
    }

}
