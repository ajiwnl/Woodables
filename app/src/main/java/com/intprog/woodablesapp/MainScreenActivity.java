package com.intprog.woodablesapp;

import android.content.Intent;
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

        // Get the role from the intent
        Intent intent = getIntent();
        String role = intent.getStringExtra("ROLE");

        if ("admin".equals(role)) {
            // Add admin-specific options here
            Intent adminAssessmentIntent = new Intent(MainScreenActivity.this, AdminAssesmentActivity.class);
            startActivity(adminAssessmentIntent);

            Intent adminActivityIntent = new Intent(MainScreenActivity.this, AdminActivity.class);
            startActivity(adminActivityIntent);

            communityclick.setOnClickListener(v -> {
                replaceFragment(new CommunityFragment());
                updateIcons(R.drawable.dghomebtn, R.drawable.cinchat, R.drawable.dghammer, R.drawable.dgmessage, R.drawable.dgdoc);
            });
        } else if ("client".equals(role)) {
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
            updateIcons(R.drawable.cinbtn, R.drawable.socialicon, R.drawable.dghammer, R.drawable.dgmessage, R.drawable.dgdoc);
            enableAllBtn();
            homeclick.setEnabled(false);

        });

        communityclick.setOnClickListener(v -> {
            replaceFragment(new CommunityFragment());
            updateIcons(R.drawable.dghomebtn, R.drawable.cinchat, R.drawable.dghammer, R.drawable.dgmessage, R.drawable.dgdoc);
            enableAllBtn();
            communityclick.setEnabled(false);
        });

        postingclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("client".equals(role)) {
                    replaceFragment(new CreateJobCardFragment());
                } else {
                    replaceFragment(new JobListingFragment());
                }
                updateIcons(R.drawable.dghomebtn, R.drawable.socialicon, R.drawable.cinhammer, R.drawable.dgmessage, R.drawable.dgdoc);
                enableAllBtn();
                postingclick.setEnabled(false);
            }     
        });

        chatclick.setOnClickListener(v -> {
            replaceFragment(new MessageChatViewFragment());
            updateIcons(R.drawable.dghomebtn, R.drawable.socialicon, R.drawable.dghammer, R.drawable.cinsocial, R.drawable.dgdoc);
            enableAllBtn();
            chatclick.setEnabled(false);
        });

        docclick.setOnClickListener(v -> {
            replaceFragment(new LearnCourseFragment());
            updateIcons(R.drawable.dghomebtn, R.drawable.socialicon, R.drawable.dghammer, R.drawable.dgmessage, R.drawable.cindoc);
            enableAllBtn();
            docclick.setEnabled(false);
        });
    }

    private void updateIcons(int homeRes, int communityRes, int postingRes, int chatRes, int docRes) {
        homeclick.setImageResource(homeRes);
        communityclick.setImageResource(communityRes);
        postingclick.setImageResource(postingRes);
        chatclick.setImageResource(chatRes);
        docclick.setImageResource(docRes);
    }

    private void replaceFragment(Fragment frag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_out_right, R.anim.slide_in_left);
        fragmentTransaction.replace(R.id.contentView, frag);
        fragmentTransaction.commit();
    }

    private void enableAllBtn(){
        ImageView[] nav = {homeclick, communityclick, postingclick, chatclick, docclick};

        for (ImageView navigate : nav) {
            navigate.setEnabled(true);
        }

    }
}