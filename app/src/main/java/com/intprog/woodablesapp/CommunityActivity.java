package com.intprog.woodablesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class CommunityActivity extends AppCompatActivity {

    ImageView homeclick, communityclick, postingclick, chatclick, docclick, addPost;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        homeclick = findViewById(R.id.home);
        communityclick = findViewById(R.id.community);
        postingclick = findViewById(R.id.hammer);
        chatclick = findViewById(R.id.messenger);
        docclick = findViewById(R.id.documents);
        addPost = findViewById(R.id.addpost);

        homeclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toHome = new Intent(CommunityActivity.this, ClientProfileActivity.class);
                startActivity(toHome);
            }
        });

        communityclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toClient = new Intent(CommunityActivity.this, CommunityActivity.class);
                startActivity(toClient);

            }
        });

        postingclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toPosting = new Intent(CommunityActivity.this, JobListingActivity.class);
                startActivity(toPosting);
            }
        });

        chatclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toChats = new Intent(CommunityActivity.this, MessageChatViewActivity.class);
                startActivity(toChats);
            }
        });

        docclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toDocs = new Intent(CommunityActivity.this, LearnCourseActivity.class);
                startActivity(toDocs);
            }
        });

        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toPost = new Intent(CommunityActivity.this, CreatePostActivity.class);
                startActivity(toPost);
            }
        });
    }
}