package com.intprog.woodablesapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.cardview.widget.CardView;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import android.util.Log;
import androidx.appcompat.app.AlertDialog;

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

                            Log.d("AdminAssesmentActivity", "Adding document to layout: " + documentId);
                            addDocumentToLayout(documentId, fullName, expertise, desc7, educ, course, exp_1, exp_2, location);
                        }
                    } else {
                        Log.e("AdminAssesmentActivity", "Error loading documents: ", task.getException());
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
        // Inflate the item layout
        LayoutInflater inflater = LayoutInflater.from(this);
        View itemView = inflater.inflate(R.layout.admin_item_assessment, assessmentLinearLayout, false);

        // Get references to the views in the inflated layout
        TextView fullNameTextView = itemView.findViewById(R.id.fullNameTextView);
        TextView educTextView = itemView.findViewById(R.id.educTextView);
        TextView courseTextView = itemView.findViewById(R.id.courseTextView);
        TextView exp1TextView = itemView.findViewById(R.id.exp1TextView);
        TextView exp2TextView = itemView.findViewById(R.id.exp2TextView);
        TextView locationTextView = itemView.findViewById(R.id.locationTextView);
        Button deleteButton = itemView.findViewById(R.id.deleteButton);
        Button approveButton = itemView.findViewById(R.id.approveButton);

        // Set the text for the views
        fullNameTextView.setText(String.format("%s %s\n%s", fullName, expertise, desc7));
        educTextView.setText(String.format("Education: %s", educ));
        courseTextView.setText(String.format("Course: %s", course));
        exp1TextView.setText(String.format("Experience 1: %s", exp_1));
        exp2TextView.setText(String.format("Experience 2: %s", exp_2));
        locationTextView.setText(String.format("Location: %s", location));

        // Set the click listeners for the buttons
        deleteButton.setOnClickListener(v -> showDeleteConfirmationDialog(documentId, itemView));
        approveButton.setOnClickListener(v -> showApproveConfirmationDialog(documentId, itemView));

        // Add the item view to the layout
        assessmentLinearLayout.addView(itemView);
    }

    private void showDeleteConfirmationDialog(String documentId, View documentView) {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Delete")
                .setMessage("Are you sure you want to delete this document?")
                .setPositiveButton("Yes", (dialog, which) -> deleteDocument(documentId, documentView))
                .setNegativeButton("No", null)
                .show();
    }

    private void showApproveConfirmationDialog(String documentId, View documentView) {
        new AlertDialog.Builder(this)
                .setTitle("Confirm Approve")
                .setMessage("Are you sure you want to approve this document?")
                .setPositiveButton("Yes", (dialog, which) -> approveDocument(documentId, documentView))
                .setNegativeButton("No", null)
                .show();
    }
}
