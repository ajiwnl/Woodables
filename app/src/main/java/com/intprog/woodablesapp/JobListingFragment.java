package com.intprog.woodablesapp;

import android.os.Bundle;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class JobListingFragment extends Fragment {
    private FirebaseAuth mAuth;
    private StorageReference storageReference;
    private String userId;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_job_listing, container, false);

        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(getContext(), "User not authenticated", Toast.LENGTH_SHORT).show();
            return rootView;
        }

        userId = mAuth.getCurrentUser().getUid();

        ImageView profilePicture = rootView.findViewById(R.id.profilepicture);
        fetchProfilePicture(profilePicture);

        retrieveListings();

        return rootView;
    }

    private void fetchProfilePicture(ImageView profilePicture) {
        storageReference.child("profile_pictures/" + userId).getDownloadUrl().addOnSuccessListener(uri -> {
            Glide.with(getContext()).load(uri).into(profilePicture);
        }).addOnFailureListener(e -> {
            Toast.makeText(getContext(), "Failed to load profile picture", Toast.LENGTH_SHORT).show();
        });
    }

    public void renderListing(Listing listing) {
        // Inflate your listing item layout dynamically
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View listingView = inflater.inflate(R.layout.post_listing, null);

        // Populate views with listing data
        TextView companyNameTextView = listingView.findViewById(R.id.company_name_post);
        TextView jobTitleTextView = listingView.findViewById(R.id.title_post);
        TextView payRangeTextView = listingView.findViewById(R.id.pay_range_post);
        TextView detailsTextView = listingView.findViewById(R.id.details_post);
        TextView requirements1TextView = listingView.findViewById(R.id.requirements1_post);
        TextView requirements2TextView = listingView.findViewById(R.id.requirements2_post);
        TextView requirements3TextView = listingView.findViewById(R.id.requirements3_post);
        TextView hasBenefitsTextView = listingView.findViewById(R.id.benefits_post);

        companyNameTextView.setText("Company Name/Individual Name: " + listing.getCompanyName());
        jobTitleTextView.setText("Job Title: " + listing.getJobTitle());
        payRangeTextView.setText("Pay Range: " + listing.getPayRange());
        detailsTextView.setText("Details: " + listing.getDetails());
        requirements1TextView.setText("Requirements 1: " + listing.getRequirements1());
        requirements2TextView.setText("Requirements 2: " + listing.getRequirements2());
        requirements3TextView.setText("Requirements 3: " + listing.getRequirements3());
        hasBenefitsTextView.setText("Has Benefits: " + listing.getHasBenefits());

        // Add the listing view to your LinearLayout with appropriate margins
        LinearLayout listingContainer = getView().findViewById(R.id.listingContainer);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(40, 40, 40, 40); // Add margins if needed
        listingView.setLayoutParams(layoutParams);
        listingContainer.addView(listingView);
    }

    public void retrieveListings() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collectionGroup("user_jobs")
                .whereEqualTo("status", "approved")
                .orderBy("jobTitle", Query.Direction.ASCENDING)
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    if (queryDocumentSnapshots.isEmpty()) {
                        Toast.makeText(getContext(), "No approved listings found.", Toast.LENGTH_SHORT).show();
                    } else {
                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                            Listing listing = documentSnapshot.toObject(Listing.class);
                            renderListing(listing);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("JobListingFragment", "Failed to retrieve listings", e);
                    Toast.makeText(getContext(), "Failed to retrieve listings: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });

    }
}