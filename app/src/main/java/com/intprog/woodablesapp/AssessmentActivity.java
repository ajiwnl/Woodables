package com.intprog.woodablesapp;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class AssessmentActivity extends AppCompatActivity {

    EditText lNameIn, fNameIn, mNameIn, doaIn, expertiseIn;
    Button sendBtn, getSendBtn;
    ImageView backBtn;

    // Firestore instance
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;

    // SharedPreferences instance
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assessment);

        // Initialize Firestore and FirebaseAuth
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        sharedPreferences = getSharedPreferences("UserData", MODE_PRIVATE);
        lNameIn = findViewById(R.id.assesslName);
        fNameIn = findViewById(R.id.assessfName);
        mNameIn = findViewById(R.id.assessmName);
        doaIn = findViewById(R.id.assesDOA);
        expertiseIn = findViewById(R.id.assessExpertise);
        sendBtn = findViewById(R.id.bookBtn);
        backBtn = findViewById(R.id.backbutton);

        // Fetch and set user data
        fetchAndSetUserData();

        sendBtn.setOnClickListener(v -> saveUserData());
        backBtn.setOnClickListener(v -> finish());
    }

    // Method to fetch user data from Firestore and set in EditText fields
    private void fetchAndSetUserData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            db.collection("users").document(uid).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String firstName = documentSnapshot.getString("First Name");
                            String middleName = documentSnapshot.getString("Middle Name");
                            String lastName = documentSnapshot.getString("Last Name");

                            fNameIn.setText(firstName);
                            mNameIn.setText(middleName);
                            lNameIn.setText(lastName);

                            // Make the fields non-editable
                            fNameIn.setEnabled(false);
                            mNameIn.setEnabled(false);
                            lNameIn.setEnabled(false);
                        } else {
                            Toast.makeText(AssessmentActivity.this, "User data not found", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(AssessmentActivity.this, "Error fetching user data", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(AssessmentActivity.this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }

    // Method to save user data in Firestore
    public void saveUserData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();

            Map<String, Object> assessmentData = new HashMap<>();
            assessmentData.put("lastName", lNameIn.getText().toString());
            assessmentData.put("firstName", fNameIn.getText().toString());
            assessmentData.put("middleName", mNameIn.getText().toString());
            assessmentData.put("dateOfAssessment", doaIn.getText().toString());
            assessmentData.put("expertise", expertiseIn.getText().toString());

            // Use set with the document ID as the user's UID
            db.collection("assessment").document(uid).set(assessmentData)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(AssessmentActivity.this, "Data saved successfully", Toast.LENGTH_SHORT).show();
                        setResult(RESULT_OK);
                        finish();
                    })
                    .addOnFailureListener(e -> {
                        Toast.makeText(AssessmentActivity.this, "Error saving data", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(AssessmentActivity.this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }
}