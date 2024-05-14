package com.intprog.woodablesapp;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

public class WoodworkerProfileFragment extends Fragment{
    private TextView profileName;
    private TextView profileDesc2;
    private TextView profileDesc3;
    private TextView profileDesc4;
    private TextView profileDesc5;
    private TextView profileDesc6;
    private TextView profileDesc7;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewRoot = inflater.inflate(R.layout.fragment_woodworker_profile, container, false);

        profileName = viewRoot.findViewById(R.id.profileName);
        profileDesc2 = viewRoot.findViewById(R.id.profileDesc2);
        profileDesc3 = viewRoot.findViewById(R.id.profileDesc3);
        profileDesc4 = viewRoot.findViewById(R.id.profileDesc4);
        profileDesc5 = viewRoot.findViewById(R.id.profileDesc5);
        profileDesc6 = viewRoot.findViewById(R.id.profileDesc6);
        profileDesc7 = viewRoot.findViewById(R.id.profileDesc7);

        Button editProfile = viewRoot.findViewById(R.id.editProfile);
        Button openTo = viewRoot.findViewById(R.id.openToButton);

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

        Intent intent = getActivity().getIntent();
        String fname = intent.getStringExtra("FName");
        String lname = intent.getStringExtra("LName");

        String fullName = fname + " " + lname;

        profileName.setText(fullName);

        return viewRoot;
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String name = data.getStringExtra("NAME");
            String desc2 = data.getStringExtra("DESC2");
            String desc3 = data.getStringExtra("DESC3");
            String desc4 = data.getStringExtra("DESC4");
            String desc5 = data.getStringExtra("DESC5");
            String desc6 = data.getStringExtra("DESC6");
            String desc7 = data.getStringExtra("DESC7");

            profileName.setText(name);
            profileDesc2.setText(desc2);
            profileDesc3.setText(desc3);
            profileDesc4.setText(desc4);
            profileDesc5.setText(desc5);
            profileDesc6.setText(desc6);
            profileDesc7.setText(desc7);
        }
    }


    private void replaceFragment(Fragment frag){
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.contentView, frag);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }
}