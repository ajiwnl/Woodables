package com.intprog.woodablesapp;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.bumptech.glide.Glide;



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
        Button openTo = viewRoot.findViewById(R.id.openToButton);

        Button editProfile = viewRoot.findViewById(R.id.editProfile);

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(viewRoot.getContext(), EditProfileClientActivity.class);
                startActivityForResult(intent, 1);
            }
        });

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

        // Retrieve full name and role from Intent or SharedPreferences
        Intent intent = getActivity().getIntent();
        String fullName = intent.getStringExtra("FullName");
        String role = intent.getStringExtra("ROLE");
        if (fullName == null || role == null) {
            SharedPreferences preferences = getActivity().getSharedPreferences("user_info", getActivity().MODE_PRIVATE);
            fullName = preferences.getString("fullname", "Default Name");
            role = preferences.getString("role", "Default Role");
        }
        profileName.setText(fullName);
        woodworkerRole.setText(role);

        storageReference.child("profile_pictures/" + userId).getDownloadUrl().addOnSuccessListener(uri -> {
            Glide.with(getContext()).load(uri).into(profilePicture);
        }).addOnFailureListener(e -> {
            Toast.makeText(getContext(), "Failed to load profile picture", Toast.LENGTH_SHORT).show();
        });

        // Fetch profile picture from Firebase Storage using ProfilePictureManager
        ProfilePictureManager.fetchProfilePicture(getContext(), profilePicture);

        return viewRoot;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            String fullName = data.getStringExtra("FULL_NAME");
            String desc2 = data.getStringExtra("DESC2");
            String desc3 = data.getStringExtra("DESC3");
            String desc4 = data.getStringExtra("DESC4");
            String desc5 = data.getStringExtra("DESC5");
            String desc6 = data.getStringExtra("DESC6");
            String desc7 = data.getStringExtra("DESC7");

            if (fullName != null) {
                profileName.setText(fullName);
                SharedPreferences preferences = getActivity().getSharedPreferences("user_info", getActivity().MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("fullname", fullName);
                editor.apply();
            }

            profileDesc2.setText(desc2);
            profileDesc3.setText(desc3);
            profileDesc4.setText(desc4);
            profileDesc5.setText(desc5);
            profileDesc6.setText(desc6);
            profileDesc7.setText(desc7);

            // Fetch and display updated profile picture
            storageReference.child("profile_pictures/" + userId).getDownloadUrl().addOnSuccessListener(uri -> {
                Glide.with(getContext()).load(uri).into(profilePicture);
            }).addOnFailureListener(e -> {
                Toast.makeText(getContext(), "Failed to load updated profile picture", Toast.LENGTH_SHORT).show();
            });
        }
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
