package com.intprog.woodablesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.cardview.widget.CardView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class AdminAssesmentActivity extends AppCompatActivity {
    private LinearLayout assessmentLinearLayout;
    private FirebaseFirestore db;

    private Button listingsButton, assessmentButton, postsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_assesment);

        assessmentLinearLayout = findViewById(R.id.assessmentLinearLayout);
        listingsButton = findViewById(R.id.toListings);
        assessmentButton = findViewById(R.id.toAssessment);
        postsButton = findViewById(R.id.toPosts);
        db = FirebaseFirestore.getInstance();

        loadDocumentIds();

        listingsButton.setOnClickListener(v -> startActivity(new Intent(AdminAssesmentActivity.this, AdminActivity.class)));
        assessmentButton.setOnClickListener(v -> startActivity(new Intent(AdminAssesmentActivity.this, AdminAssesmentActivity.class)));
        postsButton.setOnClickListener(v -> startActivity(new Intent(AdminAssesmentActivity.this, AdminPostActivity.class)));
    }

    private void loadDocumentIds() {
        db.collection("assessment")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String documentId = document.getId();
                            String fullName = document.getString("firstName") + " " + document.getString("middleName") + " " + document.getString("lastName");
                            String expertise = document.getString("expertise");
                            String desc7 = document.getString("desc7");
                            String educ = document.getString("educ");
                            String course = document.getString("course");
                            String exp_1 = document.getString("exp_1");
                            String exp_2 = document.getString("exp_2");
                            String location = document.getString("location");

                            addDocumentToLayout(documentId, fullName, expertise, desc7, educ, course, exp_1, exp_2, location);
                        }
                    } else {
                        // Handle the error
                        Toast.makeText(AdminAssesmentActivity.this, "Error loading documents.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteDocument(String documentId, View documentView) {
        db.collection("assessment").document(documentId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    assessmentLinearLayout.removeView(documentView);
                    Toast.makeText(AdminAssesmentActivity.this, "Document deleted.", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AdminAssesmentActivity.this, "Error deleting document.", Toast.LENGTH_SHORT).show();
                });
    }

    private void approveDocument(String documentId, View documentView) {
        db.collection("assessment").document(documentId)
                .update("status", "approved")
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(AdminAssesmentActivity.this, "Document approved.", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AdminAssesmentActivity.this, "Error approving document.", Toast.LENGTH_SHORT).show();
                });
    }

    private void addDocumentToLayout(String documentId, String fullName, String expertise, String desc7, String educ, String course, String exp_1, String exp_2, String location) {
        // Create a CardView
        CardView cardView = new CardView(this);
        LinearLayout.LayoutParams cardViewParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        cardViewParams.height = 500;
        cardViewParams.setMargins(8, 8, 8, 8);
        cardView.setLayoutParams(cardViewParams);
        cardView.setRadius(15); // Set corner radius
        cardView.setCardBackgroundColor(Color.WHITE); // Set card background color
        cardView.setCardElevation(8); // Set card elevation

        // Create a LinearLayout inside the CardView
        LinearLayout cardContentLayout = new LinearLayout(this);
        cardContentLayout.setOrientation(LinearLayout.VERTICAL);
        cardContentLayout.setPadding(16, 16, 16, 16);


        TextView fullNameTextView = new TextView(this);
        fullNameTextView.setText(String.format("%s %s\n%s", fullName, expertise, desc7));
        fullNameTextView.setTextSize(18);
        fullNameTextView.setTextColor(Color.BLACK);

        TextView educTextView = new TextView(this);
        educTextView.setText(String.format("Education: %s", educ));
        educTextView.setTextColor(Color.BLACK);

        TextView courseTextView = new TextView(this);
        courseTextView.setText(String.format("Course: %s", course));
        courseTextView.setTextColor(Color.BLACK);

        TextView exp1TextView = new TextView(this);
        exp1TextView.setText(String.format("Experience 1: %s", exp_1));
        exp1TextView.setTextColor(Color.BLACK);

        TextView exp2TextView = new TextView(this);
        exp2TextView.setText(String.format("Experience 2: %s", exp_2));
        exp2TextView.setTextColor(Color.BLACK);

        TextView locationTextView = new TextView(this);
        locationTextView.setText(String.format("Location: %s", location));
        locationTextView.setTextColor(Color.BLACK);

        // Create a LinearLayout for buttons
        LinearLayout buttonLayout = new LinearLayout(this);
        buttonLayout.setOrientation(LinearLayout.HORIZONTAL);
        buttonLayout.setLayoutParams(new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        ));
        buttonLayout.setGravity(Gravity.CENTER);

        Button deleteButton = new Button(this);
        deleteButton.setText("Delete");
        deleteButton.setTextColor(Color.WHITE);
        deleteButton.setBackgroundColor(Color.RED);
        deleteButton.setOnClickListener(v -> deleteDocument(documentId, cardView));

        Button approveButton = new Button(this);
        approveButton.setText("Approve");
        approveButton.setTextColor(Color.WHITE);
        approveButton.setBackgroundColor(Color.BLUE);
        approveButton.setOnClickListener(v -> approveDocument(documentId, cardView));

        // Add buttons to buttonLayout
        LinearLayout.LayoutParams buttonParams = new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        );
        deleteButton.setLayoutParams(buttonParams);
        approveButton.setLayoutParams(buttonParams);

        // Add buttons to buttonLayout
        buttonLayout.addView(deleteButton);
        buttonLayout.addView(approveButton);

        // Add views to cardContentLayout
        cardContentLayout.addView(fullNameTextView);
        cardContentLayout.addView(educTextView);
        cardContentLayout.addView(courseTextView);
        cardContentLayout.addView(exp1TextView);
        cardContentLayout.addView(exp2TextView);
        cardContentLayout.addView(locationTextView);

        // Add cardContentLayout to CardView
        cardView.addView(cardContentLayout);

        // Add CardView to assessmentLinearLayout
        assessmentLinearLayout.addView(cardView);
    }

}