package com.intprog.woodablesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
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
import android.util.Log;

public class AdminActivity extends AppCompatActivity {
    private LinearLayout listingsLinearLayout;
    private FirebaseFirestore db;

    private Button listingsButton, assessmentButton, postsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        listingsLinearLayout = findViewById(R.id.listingsLinearLayout);
        listingsButton = findViewById(R.id.toListings);
        assessmentButton = findViewById(R.id.toAssessment);
        postsButton = findViewById(R.id.toPosts);
        db = FirebaseFirestore.getInstance();

        loadDocumentIds();

        listingsButton.setOnClickListener(v -> startActivity(new Intent(AdminActivity.this, AdminActivity.class)));
        assessmentButton.setOnClickListener(v -> startActivity(new Intent(AdminActivity.this, AdminAssesmentActivity.class)));
        postsButton.setOnClickListener(v -> startActivity(new Intent(AdminActivity.this, AdminPostActivity.class)));
    }

    private void loadDocumentIds() {
        db.collection("listings")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String documentId = document.getId();
                            String title = document.getString("title");
                            String description = document.getString("description");

                            Log.d("AdminActivity", "Adding document to layout: " + documentId);
                            addDocumentToLayout(documentId, title, description);
                        }
                    } else {
                        Log.e("AdminActivity", "Error loading documents: ", task.getException());
                        Toast.makeText(AdminActivity.this, "Error loading documents.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteDocument(String documentId, View documentView) {
        db.collection("listings").document(documentId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    listingsLinearLayout.removeView(documentView);
                    Toast.makeText(AdminActivity.this, "Document deleted.", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AdminActivity.this, "Error deleting document.", Toast.LENGTH_SHORT).show();
                });
    }

    private void approveDocument(String documentId, View documentView) {
        db.collection("listings").document(documentId)
                .update("status", "approved")
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(AdminActivity.this, "Document approved.", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AdminActivity.this, "Error approving document.", Toast.LENGTH_SHORT).show();
                });
    }

    private void addDocumentToLayout(String documentId, String title, String description) {
        // Inflate the layout for a single document
        View documentView = getLayoutInflater().inflate(R.layout.admin_listing_activity, listingsLinearLayout, false);

        // Set the document data
        TextView titleTextView = documentView.findViewById(R.id.titleTextView);
        TextView descriptionTextView = documentView.findViewById(R.id.descriptionTextView);
        Button deleteButton = documentView.findViewById(R.id.deleteButton);
        Button approveButton = documentView.findViewById(R.id.approveButton);

        titleTextView.setText(title);
        descriptionTextView.setText(description);

        // Set button click listeners
        deleteButton.setOnClickListener(v -> showConfirmationDialog(documentId, documentView, "delete"));
        approveButton.setOnClickListener(v -> showConfirmationDialog(documentId, documentView, "approve"));

        // Add the document view to the layout
        listingsLinearLayout.addView(documentView);
    }

    private void showConfirmationDialog(String documentId, View documentView, String action) {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to " + action + " this document?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    if (action.equals("delete")) {
                        deleteDocument(documentId, documentView);
                    } else if (action.equals("approve")) {
                        approveDocument(documentId, documentView);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}
