package com.intprog.woodablesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RadioButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class UserInfoActivity extends AppCompatActivity {

    RadioButton woodType, clientType;
    String selectedRole = "Woodworker"; // Default role

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_user_info);

        woodType = findViewById(R.id.workerrad);
        clientType = findViewById(R.id.clientrad);

        replaceFragment(new UserInfoWoodworkerFragment());

        woodType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedRole = "woodworker";
                replaceFragment(new UserInfoWoodworkerFragment());
            }
        });

        clientType.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectedRole = "client";
                replaceFragment(new UserInfoClientFragment());
            }
        });
    }

    private void replaceFragment(Fragment frag){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.contentView, frag);
        fragmentTransaction.commit();
    }

    public String getSelectedRole() {
        return selectedRole;
    }
}