package com.intprog.woodablesapp;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class ClientProfileFragment extends Fragment {

    private static final int PROFILE_PIC_REQ_CODE = 1000;
    private static final int BACKGROUND_PIC_REQ_CODE = 2000;

    ImageView profilechange, profilebkgchange, profilepic, backgroundpic;
    Button followBtn;
    TextView clientName,clientLoc,clientFB;
    RelativeLayout layout;
    private Uri profilePicUri;
    private Uri backgroundPicUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_client_profile, container, false);


        //Follow Button
        followBtn = rootView.findViewById(R.id.openToButton);
        clientName = rootView.findViewById(R.id.profileName);
        //Client Profile Main Layout
        layout = rootView.findViewById(R.id.scrollMainLayout);
        //Image changer location
        profilechange = rootView.findViewById(R.id.cameralogoprofile);
        profilebkgchange = rootView.findViewById(R.id.cameralogobackground);
        //Image Location
        profilepic = rootView.findViewById(R.id.profilepicture);
        backgroundpic = rootView.findViewById(R.id.profilebackground);
        //Find Client Location
        clientLoc = rootView.findViewById(R.id.profileDesc2);
        clientFB = rootView.findViewById(R.id.profileDesc4);

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


        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
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
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View popup = inflater.inflate(R.layout.activity_client_profile_followed_dummy, null); // Consider fragment-specific layout name

        TextView clientFollowed = (TextView) popup.findViewById(R.id.followingName);
        clientFollowed.setText("You've Followed "+clientName);
        Button confirm = (Button) popup.findViewById(R.id.confirmbtn);

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