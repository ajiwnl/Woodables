package com.intprog.woodablesapp;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;

public class WoodworkerProfileFragment extends Fragment {
    private TextView profileName;
    private TextView woodworkerRole;
    private TextView profileDesc2;
    private TextView profileDesc3;
    private TextView profileDesc4;
    private TextView profileDesc5;
    private TextView profileDesc6;
    private TextView profileDesc7;
    private ImageView logoutBtn;
    private FirebaseAuth mAuth; //FirebaseAuth instance

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
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
        Button editProfile = viewRoot.findViewById(R.id.editProfile);
        Button openTo = viewRoot.findViewById(R.id.openToButton);
        logoutBtn = viewRoot.findViewById(R.id.logout);

        mAuth = FirebaseAuth.getInstance();

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

        return viewRoot;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
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
        }
    }

    private void replaceFragment(Fragment frag) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.contentView, frag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }
}