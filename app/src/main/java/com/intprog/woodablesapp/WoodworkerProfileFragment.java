package com.intprog.woodablesapp;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class WoodworkerProfileFragment extends Fragment {
    private TextView profileName;
    private TextView woodworkerRole;
    private TextView profileDesc2;
    private TextView profileDesc3;
    private TextView profileDesc4;
    private TextView profileDesc5;
    private TextView profileDesc6;
    private TextView profileDesc7;
    private ImageView profilePicture;
    private ImageView logoutBtn;
    private FirebaseAuth mAuth;
    private StorageReference storageReference;
    private String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewRoot = inflater.inflate(R.layout.fragment_woodworker_profile, container, false);

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
        logoutBtn = viewRoot.findViewById(R.id.logout);

        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        userId = mAuth.getCurrentUser().getUid();

        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mAuth.signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                getActivity().finish();
            }
        });

        openTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new ClientProfileFragment());
            }
        });

        editProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(viewRoot.getContext(), EditProfileActivity.class);
                startActivityForResult(intent, 1);
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

        // Fetch profile descriptions from Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("profile_descriptions").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        ProfileDescriptions profileDescriptions = documentSnapshot.toObject(ProfileDescriptions.class);
                        profileDesc2.setText(profileDescriptions.getDesc2());
                        profileDesc3.setText(profileDescriptions.getDesc3());
                        profileDesc4.setText(profileDescriptions.getDesc4());
                        profileDesc5.setText(profileDescriptions.getDesc5());
                        profileDesc6.setText(profileDescriptions.getDesc6());
                        profileDesc7.setText(getAccountCreationDate());
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Failed to load descriptions", Toast.LENGTH_SHORT).show();
                });

        // Fetch profile picture from Firebase Storage
        storageReference.child("profile_pictures/" + userId).getDownloadUrl().addOnSuccessListener(uri -> {
            Glide.with(getContext()).load(uri).into(profilePicture);
        }).addOnFailureListener(e -> {
            Toast.makeText(getContext(), "Failed to load profile picture", Toast.LENGTH_SHORT).show();
        });

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

    private void replaceFragment(Fragment frag) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.contentView, frag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    private String getAccountCreationDate() {
        Log.d("ProfileFragment", "getAccountCreationDate() called");
        long creationTimestamp = FirebaseAuth.getInstance().getCurrentUser().getMetadata().getCreationTimestamp();
        Date creationDate = new Date(creationTimestamp);
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        return dateFormat.format(creationDate);
    }

}