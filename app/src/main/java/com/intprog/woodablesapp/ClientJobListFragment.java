package com.intprog.woodablesapp;

import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class ClientJobListFragment extends Fragment {

    private LinearLayout joblistingsLinearLayout;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private String userId;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewRoot = inflater.inflate(R.layout.fragment_client_job_list, container, false);

        joblistingsLinearLayout = viewRoot.findViewById(R.id.joblistingsLinearLayout);
        ImageView bkbutton = viewRoot.findViewById(R.id.backbutton);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        userId = mAuth.getCurrentUser().getUid();

        loadUserJobList();

        bkbutton.setOnClickListener(v -> {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            CreateJobCardFragment createjobfrag = new CreateJobCardFragment();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.setCustomAnimations(R.anim.slide_out_right, R.anim.slide_in_left);
            fragmentTransaction.replace(R.id.contentView, createjobfrag);
            fragmentTransaction.commit();
        });

        return viewRoot;
    }

    private void loadUserJobList() {
        db.collection("job_listings")
                .document(userId)
                .collection("user_jobs")
                .whereEqualTo("status", "approved")
                .orderBy("jobTitle", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        joblistingsLinearLayout.removeAllViews(); // Clear existing views
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Listing listing = document.toObject(Listing.class);
                            addListingToLayout(document.getId(), listing);
                        }
                    } else {
                        Toast.makeText(getContext(), "Error loading job listings: " + task.getException(), Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addListingToLayout(String documentId, Listing listing) {
        View listingView = getLayoutInflater().inflate(R.layout.listing_item, joblistingsLinearLayout, false);

        TextView companyNameTextView = listingView.findViewById(R.id.companyNameTextView);
        TextView jobTitleTextView = listingView.findViewById(R.id.jobTitleTextView);
        TextView payRangeTextView = listingView.findViewById(R.id.payRangeTextView);
        TextView detailsTextView = listingView.findViewById(R.id.detailsTextView);
        TextView requirements1TextView = listingView.findViewById(R.id.requirements1TextView);
        TextView requirements2TextView = listingView.findViewById(R.id.requirements2TextView);
        TextView requirements3TextView = listingView.findViewById(R.id.requirements3TextView);
        TextView hasBenefitsTextView = listingView.findViewById(R.id.hasBenefitsTextView);
        Button deleteButton = listingView.findViewById(R.id.deleteButton);

        companyNameTextView.setText("Company Name: " + listing.getCompanyName());
        jobTitleTextView.setText("Job Title: " + listing.getJobTitle());
        payRangeTextView.setText("Pay Range: " + listing.getPayRange());
        detailsTextView.setText("Details: " + listing.getDetails());
        requirements1TextView.setText("Requirements 1: " + listing.getRequirements1());
        requirements2TextView.setText("Requirements 2: " + listing.getRequirements2());
        requirements3TextView.setText("Requirements 3: " + listing.getRequirements3());
        hasBenefitsTextView.setText("Has Benefits: " + listing.getHasBenefits());

        deleteButton.setOnClickListener(v -> {
            deleteJobListing(documentId);
        });

        joblistingsLinearLayout.addView(listingView);
    }

    private void deleteJobListing(String documentId) {
        db.collection("job_listings")
                .document(userId)
                .collection("user_jobs")
                .document(documentId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(getContext(), "Job listing deleted", Toast.LENGTH_SHORT).show();
                    loadUserJobList(); // Refresh the job list
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getContext(), "Error deleting job listing: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}