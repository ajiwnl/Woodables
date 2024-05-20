package com.intprog.woodablesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View; // Import the View class
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainScreenActivity extends AppCompatActivity {

    ImageView homeclick, communityclick, postingclick, chatclick, docclick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_screen);

        homeclick = findViewById(R.id.home);
        communityclick = findViewById(R.id.community);
        postingclick = findViewById(R.id.hammer);
        chatclick = findViewById(R.id.messenger);
        docclick = findViewById(R.id.documents);

        // Get the role from the intent
        Intent intent = getIntent();
        String role = intent.getStringExtra("ROLE");


        if ("client".equals(role)) {
            replaceFragment(new ClientProfileFragment());
            docclick.setVisibility(View.GONE);
        } else {
            replaceFragment(new WoodworkerProfileFragment());
        }

        homeclick.setOnClickListener(v -> {
            if ("client".equals(role)) {
                replaceFragment(new ClientProfileFragment());
            } else {
                replaceFragment(new WoodworkerProfileFragment());
            }
            homeclick.setImageResource(R.drawable.cinbtn); // Change icon
            communityclick.setImageResource(R.drawable.socialicon); // Reset community icon
            postingclick.setImageResource(R.drawable.dghammer); // Reset posting icon
            chatclick.setImageResource(R.drawable.dgmessage); // Reset chat icon
            docclick.setImageResource(R.drawable.dgdoc); // Reset doc icon
        });

        communityclick.setOnClickListener(v -> {
            replaceFragment(new CommunityFragment());
            communityclick.setImageResource(R.drawable.cinchat); // Change icon
            homeclick.setImageResource(R.drawable.dghomebtn); // Reset home icon
            postingclick.setImageResource(R.drawable.dghammer); // Reset posting icon
            chatclick.setImageResource(R.drawable.dgmessage); // Reset chat icon
            docclick.setImageResource(R.drawable.dgdoc); // Reset doc icon
        });

        postingclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("client".equals(role)) {
                    Intent intent = new Intent(MainScreenActivity.this, CreateJobCardActivity.class);
                    startActivity(intent);
                } else {
                    replaceFragment(new JobListingFragment());
                }
                postingclick.setImageResource(R.drawable.cinhammer); // Change icon
                homeclick.setImageResource(R.drawable.dghomebtn); // Reset home icon
                communityclick.setImageResource(R.drawable.socialicon); // Reset community icon
                chatclick.setImageResource(R.drawable.dgmessage); // Reset chat icon
                docclick.setImageResource(R.drawable.dgdoc); // Reset doc icon
            }
        });

        chatclick.setOnClickListener(v -> {
            replaceFragment(new MessageChatViewFragment());
            chatclick.setImageResource(R.drawable.cinsocial); // Change icon
            homeclick.setImageResource(R.drawable.dghomebtn); // Reset home icon
            communityclick.setImageResource(R.drawable.socialicon); // Reset community icon
            postingclick.setImageResource(R.drawable.dghammer); // Reset posting icon
            docclick.setImageResource(R.drawable.dgdoc); // Reset doc icon
        });

        docclick.setOnClickListener(v -> {
            replaceFragment(new LearnCourseFragment());
            docclick.setImageResource(R.drawable.cindoc); // Change icon
            homeclick.setImageResource(R.drawable.dghomebtn); // Reset home icon
            communityclick.setImageResource(R.drawable.socialicon); // Reset community icon
            postingclick.setImageResource(R.drawable.dghammer); // Reset posting icon
            chatclick.setImageResource(R.drawable.dgmessage); // Reset chat icon
        });
    }

    private void replaceFragment(Fragment frag){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.contentView, frag);
        fragmentTransaction.commit();
    }
}