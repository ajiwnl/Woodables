package com.intprog.woodablesapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Layout;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ClientProfileActivity extends AppCompatActivity {
    private static final int PROFILE_PIC_REQ_CODE = 1000;
    private static final int BACKGROUND_PIC_REQ_CODE = 2000;
    ImageView homeclick, communityclick, postingclick, chatclick, docclick, profilechange, profilebkgchange, profilepic, backgroundpic;
    Button followBtn;
    TextView clientName,clientLoc,clientFB;
    RelativeLayout layout;
    private Uri profilePicUri;
    private Uri backgroundPicUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_profile);

        homeclick = findViewById(R.id.home);
        communityclick = findViewById(R.id.community);
        postingclick = findViewById(R.id.hammer);
        chatclick = findViewById(R.id.messenger);
        docclick = findViewById(R.id.documents);
        //Follow Button
        followBtn = findViewById(R.id.openToButton);
        clientName = findViewById(R.id.profileName);
        //Client Profile Main Layout
        layout = findViewById(R.id.wholeLayout);
        //Image changer location
        profilechange = findViewById(R.id.cameralogoprofile);
        profilebkgchange = findViewById(R.id.cameralogobackground);
        //Image Location
        profilepic = findViewById(R.id.profilepicture);
        backgroundpic = findViewById(R.id.profilebackground);
        //Find Client Location
        clientLoc = findViewById(R.id.profileDesc2);
        clientFB = findViewById(R.id.profileDesc4);




        clientLoc.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                String url = "https://maps.app.goo.gl/5U2HDuowZVD4heeW6";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
            }
        });

        clientFB.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/TheKrustyKrabOfficial"));
                startActivity(intent);
            }
        });


        followBtn.setOnClickListener(v ->{
            createPopUpWindow(clientName.getText().toString());
        });

        profilechange.setOnClickListener(v ->{
            Intent imgGallery = new Intent(Intent.ACTION_PICK);
            imgGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(imgGallery, PROFILE_PIC_REQ_CODE);
        });

        profilebkgchange.setOnClickListener(v->{
            Intent imgGallery = new Intent(Intent.ACTION_PICK);
            imgGallery.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(imgGallery, BACKGROUND_PIC_REQ_CODE);
        });

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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            if (requestCode == PROFILE_PIC_REQ_CODE) {
                profilePicUri = data.getData();
                profilepic.setImageURI(profilePicUri);
            } else if (requestCode == BACKGROUND_PIC_REQ_CODE) {
                backgroundPicUri = data.getData();
                backgroundpic.setImageURI(backgroundPicUri);
            }
        }
    }

    private void createPopUpWindow(String clientName) {
        LayoutInflater followedInflated = (LayoutInflater) getSystemService(LAYOUT_INFLATER_SERVICE);
        View popup = followedInflated.inflate(R.layout.activity_client_profile_followed_dummy, null);

        TextView clientFollowed = (TextView) popup.findViewById(R.id.followingName);
        clientFollowed.setText("You've Followed "+clientName);
        Button confirm = popup.findViewById(R.id.confirmbtn);

        boolean focusable = true;
        PopupWindow followedPopUp = new PopupWindow(popup,800,500,focusable);
        layout.post(new Runnable() {
            @Override
            public void run() {
                followedPopUp.showAtLocation(layout, Gravity.CENTER, 0, 0);
            }
        });

        confirm.setOnClickListener(v ->{
            followedPopUp.dismiss();
        });

    }
}