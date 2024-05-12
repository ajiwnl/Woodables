package com.intprog.woodablesapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class AssessmentActivity extends AppCompatActivity {

    EditText lNameIn, fNameIn, mNameIn, doaIn, expertiseIn;
    Button sendBtn, getSendBtn;

    ImageView backBtn;

    // SharedPreferences instance
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment);
        sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        lNameIn = findViewById(R.id.assesslName);
        fNameIn = findViewById(R.id.assessfName);
        mNameIn = findViewById(R.id.assessmName);
        doaIn = findViewById(R.id.assesDOA);
        expertiseIn = findViewById(R.id.assessExpertise);
        sendBtn = findViewById(R.id.bookBtn);
        getSendBtn = findViewById(R.id.getDataBtn);
        backBtn = findViewById(R.id.backbutton);

        sendBtn.setOnClickListener(v -> saveUserData());

        getSendBtn.setOnClickListener(v -> displayUserData());

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    // Method to save user data in SharedPreferences
    public void saveUserData() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("lastName", lNameIn.getText().toString());
        editor.putString("firstName", fNameIn.getText().toString());
        editor.putString("middleName", mNameIn.getText().toString());
        editor.putString("dateOfAssessment", doaIn.getText().toString());
        editor.putString("expertise", expertiseIn.getText().toString());
        editor.apply();
    }

    // Method to display user data from SharedPreferences
    // Method to display user data from SharedPreferences
    public void displayUserData() {
        String lastName = sharedPreferences.getString("lastName", "");
        String firstName = sharedPreferences.getString("firstName", "");
        String middleName = sharedPreferences.getString("middleName", "");
        String dateOfAssessment = sharedPreferences.getString("dateOfAssessment", "");
        String expertise = sharedPreferences.getString("expertise", "");

        // Build the message for the dialog
        StringBuilder messageBuilder = new StringBuilder();
        messageBuilder.append("Last Name: ").append(lastName).append("\n")
                .append("First Name: ").append(firstName).append("\n")
                .append("Middle Name: ").append(middleName).append("\n")
                .append("Date of Assessment: ").append(dateOfAssessment).append("\n")
                .append("Expertise: ").append(expertise);

        // Show the dialog
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("User Data");
        builder.setMessage(messageBuilder.toString());
        builder.setPositiveButton("OK", null);
        builder.create().show();
    }

}
