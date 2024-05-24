package com.intprog.woodablesapp;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
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

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ClientProfileFragment extends Fragment {

    TextView clientName, clientLoc, clientFB;
    RelativeLayout layout;
    //Used
    private TextView profileName;
    private TextView woodworkerRole;
    private TextView profileDesc2;
    private TextView profileDesc3;
    private TextView profileDesc4;
    private TextView profileDesc5;
    private TextView profileDesc6;
    private TextView profileDesc7;
    private ImageView profilePicture;
    ImageView logoutBtn;
    private StorageReference storageReference;
    private String userId;

    private FirebaseAuth mAuth; //FirebaseAuth instance

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewRoot = inflater.inflate(R.layout.fragment_client_profile, container, false);

        profileName = viewRoot.findViewById(R.id.profileName);
        woodworkerRole = viewRoot.findViewById(R.id.profileCategory);
        profileDesc2 = viewRoot.findViewById(R.id.profileDesc2);
        profileDesc3 = viewRoot.findViewById(R.id.profileDesc3);
        profileDesc4 = viewRoot.findViewById(R.id.profileDesc4);
        profileDesc5 = viewRoot.findViewById(R.id.profileDesc5);
        profileDesc6 = viewRoot.findViewById(R.id.profileDesc6);
        profileDesc7 = viewRoot.findViewById(R.id.profileDesc7);
        profilePicture = viewRoot.findViewById(R.id.profilepicture);
        Button editProfile = viewRoot.findViewById(R.id.editProfile);
        Button openTo = viewRoot.findViewById(R.id.openToButton);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        userId = mAuth.getCurrentUser().getUid();

        // Logout Button
        logoutBtn = viewRoot.findViewById(R.id.logout);

        // Set click listener for the logout button
        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sign out from Firebase
                mAuth.signOut();
                // Navigate back to the LoginActivity and clear the back stack
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                // finish the current activity
                getActivity().finish();
            }
        });

        return viewRoot;
    }

//Reuse for something
    private void createPopUpWindow(String clientName) {
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View popup = inflater.inflate(R.layout.activity_client_profile_followed_dummy, null); // Consider fragment-specific layout name

        TextView clientFollowed = (TextView) popup.findViewById(R.id.followingName);
        clientFollowed.setText("You've Followed " + clientName);
        Button confirm = (Button) popup.findViewById(R.id.confirmbtn);

        boolean focusable = true;
        PopupWindow followedPopUp = new PopupWindow(popup, 800, 500, focusable);
        layout.post(new Runnable() {
            @Override
            public void run() {
                followedPopUp.showAtLocation(layout, Gravity.CENTER, 0, 0);
            }
        });

        confirm.setOnClickListener(v -> {
            followedPopUp.dismiss();
        });
    }
}
