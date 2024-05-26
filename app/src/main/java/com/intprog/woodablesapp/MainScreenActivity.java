package com.intprog.woodablesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class MainScreenActivity extends AppCompatActivity {

    ImageView homeclick, communityclick, postingclick, chatclick, docclick;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main_screen);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.wholeLayout), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

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