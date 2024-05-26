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


public class AdminListingFragment extends Fragment {

    private LinearLayout listingsLinearLayout;
    private FirebaseFirestore db;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewRoot = inflater.inflate(R.layout.fragment_admin_listing, container, false);

        listingsLinearLayout = viewRoot.findViewById(R.id.listingsLinearLayoutAdmin);

        db = FirebaseFirestore.getInstance();

        loadDocumentIds();

        return viewRoot;
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
                        Toast.makeText(getContext(), "Error loading documents.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void deleteDocument(String documentId, View documentView) {
        db.collection("listings").document(documentId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    listingsLinearLayout.removeView(documentView);
                    Toast.makeText(getContext(), "Document deleted.", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error deleting document.", Toast.LENGTH_SHORT).show();
                });
    }

    private void approveDocument(String documentId, View documentView) {
        db.collection("listings").document(documentId)
                .update("status", "approved")
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Document approved.", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error approving document.", Toast.LENGTH_SHORT).show();
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
        new AlertDialog.Builder(getContext())
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