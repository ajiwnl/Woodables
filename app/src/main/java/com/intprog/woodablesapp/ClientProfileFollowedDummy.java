package com.intprog.woodablesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;



public class ClientProfileFollowedDummy extends AppCompatActivity {

    TextView following;
    Button confirm;
    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_profile_followed_dummy);

        following = findViewById(R.id.followingName);
        confirm = findViewById(R.id.confirmbtn);

        Intent followingClient = getIntent();

        following.setText("You've Followed" +followingClient.getStringExtra("client_name"));

        confirm.setOnClickListener(v ->{
            Intent backToHome = new Intent(ClientProfileFollowedDummy.this, ClientProfileActivity.class);
            startActivity(backToHome);
        });
    }
}