package com.intprog.woodablesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
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
                            String course = document.getString("course");
                            addDocumentIdToLayout(course, documentId);
                        }
                    } else {
                        // Handle the error
                        Toast.makeText(AdminAssesmentActivity.this, "Error loading documents.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addDocumentIdToLayout(String course, String documentId) {
        LinearLayout documentLayout = new LinearLayout(this);
        documentLayout.setOrientation(LinearLayout.HORIZONTAL);
        documentLayout.setPadding(8, 8, 8, 8);

        TextView textView = new TextView(this);
        textView.setText(String.format("%s (ID: %s)", course, documentId));
        textView.setTextSize(16);
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        ));

        Button deleteButton = new Button(this);
        deleteButton.setText("Delete");
        deleteButton.setOnClickListener(v -> deleteDocument(documentId, documentLayout));

        Button approveButton = new Button(this);
        approveButton.setText("Approve");
        approveButton.setOnClickListener(v -> approveDocument(documentId, documentLayout));

        documentLayout.addView(textView);
        documentLayout.addView(deleteButton);
        documentLayout.addView(approveButton);

        assessmentLinearLayout.addView(documentLayout);
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
}