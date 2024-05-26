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

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.Query;

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
        db.collectionGroup("user_jobs")
                .whereEqualTo("status", "pending")
                .orderBy("jobTitle", Query.Direction.ASCENDING) // Ensure this matches the composite index
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        listingsLinearLayout.removeAllViews(); // Clear existing views
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String documentId = document.getId();
                            Log.d("AdminActivity", "Adding document to layout: " + documentId);
                            addDocumentToLayout(documentId, document);
                        }
                    } else {
                        Log.e("AdminActivity", "Error loading documents: ", task.getException());
                        Toast.makeText(getContext(), "Error loading documents.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void approveDocument(String documentId, DocumentSnapshot document) {
        document.getReference()
                .update("status", "approved")
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Document approved.", Toast.LENGTH_SHORT).show();
                    loadDocumentIds();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error approving document.", Toast.LENGTH_SHORT).show();
                });
    }

    private void deleteDocument(String documentId, DocumentSnapshot document) {
        document.getReference()
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Document deleted.", Toast.LENGTH_SHORT).show();
                    loadDocumentIds();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error deleting document.", Toast.LENGTH_SHORT).show();
                });
    }

    private void addDocumentToLayout(String documentId, QueryDocumentSnapshot document) {
        // Inflate the layout for a single document
        View documentView = getLayoutInflater().inflate(R.layout.admin_listing_activity, listingsLinearLayout, false);

        // Set the document data
        TextView companyNameTextView = documentView.findViewById(R.id.companyNameTextView);
        TextView jobTitleTextView = documentView.findViewById(R.id.jobTitleTextView);
        TextView payRangeTextView = documentView.findViewById(R.id.payRangeTextView);
        TextView detailsTextView = documentView.findViewById(R.id.detailsTextView);
        TextView requirements1TextView = documentView.findViewById(R.id.requirements1TextView);
        TextView requirements2TextView = documentView.findViewById(R.id.requirements2TextView);
        TextView requirements3TextView = documentView.findViewById(R.id.requirements3TextView);
        TextView hasBenefitsTextView = documentView.findViewById(R.id.hasBenefitsTextView);
        Button deleteButton = documentView.findViewById(R.id.deleteButton);
        Button approveButton = documentView.findViewById(R.id.approveButton);

        companyNameTextView.setText("Company Name: " + document.getString("companyName"));
        jobTitleTextView.setText("Job Title: " + document.getString("jobTitle"));
        payRangeTextView.setText("Pay Range: " + document.getString("payRange"));
        detailsTextView.setText("Details: " + document.getString("details"));
        requirements1TextView.setText("Requirements 1: " + document.getString("requirements1"));
        requirements2TextView.setText("Requirements 2: " + document.getString("requirements2"));
        requirements3TextView.setText("Requirements 3: " + document.getString("requirements3"));
        hasBenefitsTextView.setText("Has Benefits: " + document.getString("hasBenefits"));

        // Set button click listeners
        deleteButton.setOnClickListener(v -> showConfirmationDialog(documentId, documentView, document, "delete"));
        approveButton.setOnClickListener(v -> showConfirmationDialog(documentId, documentView, document, "approve"));

        // Add the document view to the layout
        listingsLinearLayout.addView(documentView);
    }

    private void showConfirmationDialog(String documentId, View documentView, DocumentSnapshot document, String action) {
        new AlertDialog.Builder(getContext())
                .setMessage("Are you sure you want to " + action + " this document?")
                .setPositiveButton("Yes", (dialog, which) -> {
                    if (action.equals("delete")) {
                        deleteDocument(documentId, document);
                    } else if (action.equals("approve")) {
                        approveDocument(documentId, document);
                    }
                })
                .setNegativeButton("No", null)
                .show();
    }
}