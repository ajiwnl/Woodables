package com.intprog.woodablesapp;


import android.os.Bundle;
import android.view.View;
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

        replaceFragment(new WoodworkerProfileFragment());

        homeclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new WoodworkerProfileFragment());
                homeclick.setImageResource(R.drawable.cinbtn); // Change icon
                communityclick.setImageResource(R.drawable.socialicon); // Reset community icon
                postingclick.setImageResource(R.drawable.dghammer); // Reset posting icon
                chatclick.setImageResource(R.drawable.dgmessage); // Reset chat icon
                docclick.setImageResource(R.drawable.dgdoc); // Reset doc icon
            }
        });

        communityclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new CommunityFragment());
                communityclick.setImageResource(R.drawable.cinchat); // Change icon
                homeclick.setImageResource(R.drawable.dghomebtn); // Reset home icon
                postingclick.setImageResource(R.drawable.dghammer); // Reset posting icon
                chatclick.setImageResource(R.drawable.dgmessage); // Reset chat icon
                docclick.setImageResource(R.drawable.dgdoc); // Reset doc icon
            }
        });

        postingclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new JobListingFragment());
                postingclick.setImageResource(R.drawable.cinhammer); // Change icon
                homeclick.setImageResource(R.drawable.dghomebtn); // Reset home icon
                communityclick.setImageResource(R.drawable.socialicon); // Reset community icon
                chatclick.setImageResource(R.drawable.dgmessage); // Reset chat icon
                docclick.setImageResource(R.drawable.dgdoc); // Reset doc icon
            }
        });

        chatclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new MessageChatViewFragment());
                chatclick.setImageResource(R.drawable.cinsocial); // Change icon
                homeclick.setImageResource(R.drawable.dghomebtn); // Reset home icon
                communityclick.setImageResource(R.drawable.socialicon); // Reset community icon
                postingclick.setImageResource(R.drawable.dghammer); // Reset posting icon
                docclick.setImageResource(R.drawable.dgdoc); // Reset doc icon
            }
        });

        docclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new LearnCourseFragment());
                docclick.setImageResource(R.drawable.cindoc); // Change icon
                homeclick.setImageResource(R.drawable.dghomebtn); // Reset home icon
                communityclick.setImageResource(R.drawable.socialicon); // Reset community icon
                postingclick.setImageResource(R.drawable.dghammer); // Reset posting icon
                chatclick.setImageResource(R.drawable.dgmessage); // Reset chat icon
            }
        });
    }

    private void replaceFragment(Fragment frag){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.contentView, frag);
        fragmentTransaction.commit();
    }
}