package com.intprog.woodablesapp;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class AdminPostFragment extends Fragment {
    private LinearLayout postsLinearLayout;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewRoot = inflater.inflate(R.layout.fragment_admin_post, container, false);

        postsLinearLayout = viewRoot.findViewById(R.id.postsLinearLayoutAdmin);

        db = FirebaseFirestore.getInstance();

        loadPosts();

        return viewRoot;
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
                        Toast.makeText(getContext(), "Error loading documents.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deletePost(String documentId, View postView) {
        db.collection("posts").document(documentId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    postsLinearLayout.removeView(postView);
                    Toast.makeText(getContext(), "Post deleted.", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error deleting post.", Toast.LENGTH_SHORT).show();
                });
    }

    private void approvePost(String documentId, View postView) {
        db.collection("posts").document(documentId)
                .update("status", "approved")
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Post approved.", Toast.LENGTH_SHORT).show();
                    // Hide the approve button after successful approval
                    Button approveButton = postView.findViewById(R.id.approveButton);
                    if (approveButton != null) {
                        approveButton.setVisibility(View.GONE);
                    }
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error approving post.", Toast.LENGTH_SHORT).show();
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
        new AlertDialog.Builder(getContext())
                .setMessage("Are you sure you want to " + action + " this post?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    if (action.equals("delete")) {
                        deletePost(documentId, postView);
                    } else if (action.equals("approve")) {
                        approvePost(documentId, postView);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}