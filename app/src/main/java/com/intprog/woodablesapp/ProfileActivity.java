package com.intprog.woodablesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

public class ProfileActivity extends AppCompatActivity {
    private TextView profileName;
    private TextView profileDesc2;
    private TextView profileDesc3;
    private TextView profileDesc4;
    private TextView profileDesc5;
    private TextView profileDesc6;
    private TextView profileDesc7;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        profileName = findViewById(R.id.profileName);
        profileDesc2 = findViewById(R.id.profileDesc2);
        profileDesc3 = findViewById(R.id.profileDesc3);
        profileDesc4 = findViewById(R.id.profileDesc4);
        profileDesc5 = findViewById(R.id.profileDesc5);
        profileDesc6 = findViewById(R.id.profileDesc6);
        profileDesc7 = findViewById(R.id.profileDesc7);

        Button editProfile = findViewById(R.id.editProfile);

        editProfile.setOnClickListener(v -> {
            Intent intent = new Intent(this, EditProfileActivity.class);
            startActivityForResult(intent, 1);
        });

        // Retrieve first name and last name from intent extras if available
        Intent intent = getIntent();
        String fname = intent.getStringExtra("FName");
        String lname = intent.getStringExtra("LName");

        // Concatenate first name and last name with a space in between
        String fullName = fname + " " + lname;

        // Set the concatenated name to the profile name TextView
        profileName.setText(fullName);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == Activity.RESULT_OK) {
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

}