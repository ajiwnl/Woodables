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
                            String email = documentSnapshot.getString("Email");

                            fNameIn.setText(firstName);
                            mNameIn.setText(middleName);
                            lNameIn.setText(lastName);

                            // Make the fields non-editable
                            fNameIn.setEnabled(false);
                            mNameIn.setEnabled(false);
                            lNameIn.setEnabled(false);

                            // Save the email in SharedPreferences for later use
                            sharedPreferences.edit().putString("userEmail", email).apply();
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

    // Method to fetch profile descriptions from Firestore
    private void fetchProfileDescriptions(String uid, ProfileDescriptionsCallback callback) {
        db.collection("profile_descriptions").document(uid).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String desc2 = documentSnapshot.getString("desc2");
                        String desc3 = documentSnapshot.getString("desc3");
                        String desc4 = documentSnapshot.getString("desc4");
                        String desc5 = documentSnapshot.getString("desc5");
                        String desc6 = documentSnapshot.getString("desc6");
                        String desc7 = documentSnapshot.getString("desc7");

                        callback.onProfileDescriptionsFetched(desc2, desc3, desc4, desc5, desc6, desc7);
                    } else {
                        callback.onProfileDescriptionsFetched(null, null, null, null, null, null);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AssessmentActivity.this, "Error fetching profile descriptions", Toast.LENGTH_SHORT).show();
                    callback.onProfileDescriptionsFetched(null, null, null, null, null, null);
                });
    }

    // Interface for profile descriptions callback
    interface ProfileDescriptionsCallback {
        void onProfileDescriptionsFetched(String desc2, String desc3, String desc4, String desc5, String desc6, String desc7);
    }

    // Method to save user data in Firestore
    public void saveUserData() {
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            String uid = currentUser.getUid();
            String email = sharedPreferences.getString("userEmail", "");

            fetchProfileDescriptions(uid, (desc2, desc3, desc4, desc5, desc6, desc7) -> {
                Map<String, Object> assessmentData = new HashMap<>();
                assessmentData.put("lastName", lNameIn.getText().toString());
                assessmentData.put("firstName", fNameIn.getText().toString());
                assessmentData.put("middleName", mNameIn.getText().toString());
                assessmentData.put("dateOfAssessment", doaIn.getText().toString());
                assessmentData.put("expertise", expertiseIn.getText().toString());
                assessmentData.put("email", email); // Add the email to the assessment data

                // Add profile descriptions to assessment data
                assessmentData.put("exp_1", desc2);
                assessmentData.put("exp_2", desc3);
                assessmentData.put("educ", desc4);
                assessmentData.put("course", desc5);
                assessmentData.put("location", desc6);
                assessmentData.put("desc7", desc7);

                // Use set with the document ID as the user's UID
                db.collection("assessment").document(uid).set(assessmentData)
                        .addOnSuccessListener(aVoid -> {
                            Toast.makeText(AssessmentActivity.this, "You've successfully apply for Skills Assessment, We'll contact you soon!", Toast.LENGTH_SHORT).show();
                            setResult(RESULT_OK);
                            finish();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(AssessmentActivity.this, "Cannot apply for SKills Assessment. Try again later", Toast.LENGTH_SHORT).show();
                        });
            });
        } else {
            Toast.makeText(AssessmentActivity.this, "User not logged in", Toast.LENGTH_SHORT).show();
        }
    }
}

