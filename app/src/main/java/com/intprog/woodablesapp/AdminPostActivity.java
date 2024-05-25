package com.intprog.woodablesapp;

import androidx.appcompat.app.AppCompatActivity;
import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class AdminPostActivity extends AppCompatActivity {
    private LinearLayout postsLinearLayout;
    private FirebaseFirestore db;

    private Button listingsButton, assessmentButton, postsButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_post);

        postsLinearLayout = findViewById(R.id.postsLinearLayout);
        listingsButton = findViewById(R.id.toListings);
        assessmentButton = findViewById(R.id.toAssessment);
        postsButton = findViewById(R.id.toPosts);
        db = FirebaseFirestore.getInstance();



        loadPosts();

        listingsButton.setOnClickListener(v -> startActivity(new Intent(AdminPostActivity.this, AdminActivity.class)));
        assessmentButton.setOnClickListener(v -> startActivity(new Intent(AdminPostActivity.this, AdminAssesmentActivity.class)));
        postsButton.setOnClickListener(v -> startActivity(new Intent(AdminPostActivity.this, AdminPostActivity.class)));
    }

    private void loadPosts() {
        db.collection("posts")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String documentId = document.getId();
                            String title = document.getString("title");
                            String message = document.getString("message");
                            String userName = document.getString("userName");

                            Log.d("AdminPostActivity", "Adding document to layout: " + documentId);
                            addPostToLayout(documentId, title, message, userName);
                        }
                    } else {
                        Log.e("AdminPostActivity", "Error loading documents: ", task.getException());
                        Toast.makeText(AdminPostActivity.this, "Error loading documents.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deletePost(String documentId, View postView) {
        db.collection("posts").document(documentId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    postsLinearLayout.removeView(postView);
                    Toast.makeText(AdminPostActivity.this, "Post deleted.", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AdminPostActivity.this, "Error deleting post.", Toast.LENGTH_SHORT).show();
                });
    }

    private void approvePost(String documentId) {
        db.collection("posts").document(documentId)
                .update("status", "approved")
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(AdminPostActivity.this, "Post approved.", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AdminPostActivity.this, "Error approving post.", Toast.LENGTH_SHORT).show();
                });
    }

    private void addPostToLayout(String documentId, String title, String message, String userName) {
        View postView = getLayoutInflater().inflate(R.layout.admin_post_item, postsLinearLayout, false);

        TextView titleTextView = postView.findViewById(R.id.titleTextView);
        TextView messageTextView = postView.findViewById(R.id.messageTextView);
        TextView userNameTextView = postView.findViewById(R.id.userNameTextView);
        Button deleteButton = postView.findViewById(R.id.deleteButton);
        Button approveButton = postView.findViewById(R.id.approveButton);

        titleTextView.setText(title);
        messageTextView.setText(message);
        userNameTextView.setText(userName);

        deleteButton.setOnClickListener(v -> showConfirmationDialog(documentId, postView, "delete"));
        approveButton.setOnClickListener(v -> showConfirmationDialog(documentId, postView, "approve"));

        postsLinearLayout.addView(postView);
    }

    private void showConfirmationDialog(String documentId, View postView, String action) {
        new AlertDialog.Builder(this)
                .setMessage("Are you sure you want to " + action + " this post?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    if (action.equals("delete")) {
                        deletePost(documentId, postView);
                    } else if (action.equals("approve")) {
                        approvePost(documentId);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}