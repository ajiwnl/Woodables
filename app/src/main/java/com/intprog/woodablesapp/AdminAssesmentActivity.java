package com.intprog.woodablesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.work.Data;
import androidx.work.OneTimeWorkRequest;
import androidx.work.WorkManager;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import android.util.Log;
import java.util.concurrent.TimeUnit;

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
                            String email = document.getString("email");  // Assuming you have an email field

                            Log.d("AdminAssesmentActivity", "Adding document to layout: " + documentId);
                            addDocumentToLayout(documentId, fullName, expertise, desc7, educ, course, exp_1, exp_2, location, email);
                        }
                    } else {
                        Log.e("AdminAssesmentActivity", "Error loading documents: ", task.getException());
                        Toast.makeText(AdminAssesmentActivity.this, "Error loading documents.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteDocument(String documentId, View documentView, String email, String desc7, String fullName) {
        db.collection("assessment").document(documentId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    assessmentLinearLayout.removeView(documentView);
                    sendDisapprovalEmail(email, desc7, fullName);  // Send email upon successful deletion
                    Toast.makeText(AdminAssesmentActivity.this, "Document deleted.", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AdminAssesmentActivity.this, "Error deleting document.", Toast.LENGTH_SHORT).show();
                });
    }

    private void approveDocument(String documentId, View documentView, String email, String desc7, String fullName) {
        db.collection("assessment").document(documentId)
                .update("status", "approved")
                .addOnSuccessListener(aVoid -> {
                    sendApprovalEmail(email, desc7, fullName);  // Send email upon successful approval
                    disableButtons(documentView);
                    scheduleDocumentDeletion(documentId);
                    Toast.makeText(AdminAssesmentActivity.this, "Document approved.", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AdminAssesmentActivity.this, "Error approving document.", Toast.LENGTH_SHORT).show();
                });
    }

    private void disableButtons(View documentView) {
        Button deleteButton = documentView.findViewById(R.id.deleteButton);
        Button approveButton = documentView.findViewById(R.id.approveButton);

        deleteButton.setEnabled(false);
        approveButton.setEnabled(false);
    }

    private void scheduleDocumentDeletion(String documentId) {
        Data data = new Data.Builder()
                .putString("documentId", documentId)
                .build();

        OneTimeWorkRequest deleteRequest = new OneTimeWorkRequest.Builder(DeleteDocumentWorker.class)
                .setInitialDelay(7, TimeUnit.DAYS)
                .setInputData(data)
                .build();

        WorkManager.getInstance(this).enqueue(deleteRequest);
    }

    private void addDocumentToLayout(String documentId, String fullName, String expertise, String desc7, String educ, String course, String exp_1, String exp_2, String location, String email) {
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
        deleteButton.setOnClickListener(v -> showDeleteConfirmationDialog(documentId, itemView, email, desc7, fullName));
        approveButton.setOnClickListener(v -> showApproveConfirmationDialog(documentId, itemView, email, desc7, fullName));

        // Add the item view to the layout
        assessmentLinearLayout.addView(itemView);
    }

    private void showDeleteConfirmationDialog(String documentId, View documentView, String email, String desc7, String fullName) {
        new AlertDialog.Builder(this)
                .setTitle("Disapproved Assessment")
                .setMessage("Are you sure you want to disapprove this assessment?")
                .setPositiveButton("Yes", (dialog, which) -> deleteDocument(documentId, documentView, email, desc7, fullName))
                .setNegativeButton("No", null)
                .show();
    }

    private void showApproveConfirmationDialog(String documentId, View documentView, String email, String desc7, String fullName) {
        new AlertDialog.Builder(this)
                .setTitle("Approved Assessment")
                .setMessage("Are you sure you want to approve this assessment?")
                .setPositiveButton("Yes", (dialog, which) -> approveDocument(documentId, documentView, email, desc7, fullName))
                .setNegativeButton("No", null)
                .show();
    }

    private void sendApprovalEmail(String email, String desc7, String fullName) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Woodables [Skill Assessment Approved]");
        emailIntent.putExtra(Intent.EXTRA_TEXT, String.format("We are happy to inform you, %s, that your skill assessment has been approved. Congratulations!\n\nDate of Skill Assessment: %s\n\nPlease communicate with us to comply with the requirements needed.", fullName, desc7));

        try {
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(AdminAssesmentActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }

    private void sendDisapprovalEmail(String email, String desc7, String fullName) {
        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{email});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Woodables [Skill Assessment Disapproved]");
        emailIntent.putExtra(Intent.EXTRA_TEXT, String.format("We are very sorry to inform you, %s, that your skill assessment has been disapproved.\n\nDate of Skill Assessment: %s\n\nPlease contact us for more information.", fullName, desc7));

        try {
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(AdminAssesmentActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }
    }
}