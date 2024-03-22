package com.intprog.woodablesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class ClientProfileActivity extends AppCompatActivity {
        ImageView homeclick, communityclick, postingclick, chatclick, docclick;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_profile);

        homeclick = findViewById(R.id.home);
        communityclick = findViewById(R.id.community);
        postingclick = findViewById(R.id.hammer);
        chatclick = findViewById(R.id.messenger);
        docclick = findViewById(R.id.documents);

        homeclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toHome = new Intent(ClientProfileActivity.this, ClientProfileActivity.class);
                startActivity(toHome);
            }
        });

        communityclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toCommunity = new Intent(ClientProfileActivity.this, CommunityActivity.class);
                startActivity(toCommunity);

            }
        });

        postingclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toPosting = new Intent(ClientProfileActivity.this, JobListingActivity.class);
                startActivity(toPosting);
            }
        });

        chatclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toChats = new Intent(ClientProfileActivity.this, MessageChatViewActivity.class);
                startActivity(toChats);
            }
        });

        docclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toDocs = new Intent(ClientProfileActivity.this, LearnCourseActivity.class);
                startActivity(toDocs);
            }
        });




    }
}