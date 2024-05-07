package com.intprog.woodablesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class LearnCourseActivity extends AppCompatActivity {

    ImageView homeclick, communityclick, postingclick, chatclick, docclick;
    Button toAssess, toCatalog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_learn_course);

        homeclick = findViewById(R.id.home);
        communityclick = findViewById(R.id.community);
        postingclick = findViewById(R.id.hammer);
        chatclick = findViewById(R.id.messenger);
        docclick = findViewById(R.id.documents);

        toAssess = findViewById(R.id.skillassess);
        toCatalog = findViewById(R.id.browsecoursecatalog);

        homeclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toHome = new Intent(LearnCourseActivity.this, ClientProfileActivity.class);
                startActivity(toHome);
            }
        });

        toCatalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toCatalog = new Intent(LearnCourseActivity.this, CourseCatalogActivity.class);
                startActivity(toCatalog);
            }
        });

        toAssess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toAssess = new Intent(LearnCourseActivity.this, AssessmentActivity.class);
                startActivity(toAssess);
            }
        });

        communityclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toClient = new Intent(LearnCourseActivity.this, CommunityActivity.class);
                startActivity(toClient);

            }
        });

        postingclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toPosting = new Intent(LearnCourseActivity.this, JobListingActivity.class);
                startActivity(toPosting);
            }
        });

        chatclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toChats = new Intent(LearnCourseActivity.this, MessageChatViewActivity.class);
                startActivity(toChats);
            }
        });

        docclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toDocs = new Intent(LearnCourseActivity.this, LearnCourseActivity.class);
                startActivity(toDocs);
            }
        });
    }
}