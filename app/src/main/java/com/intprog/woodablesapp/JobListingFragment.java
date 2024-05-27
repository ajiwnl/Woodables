package com.intprog.woodablesapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    private FirebaseFirestore db;
    private String userFullName; // Add a variable to store the user's full name

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_job_listing, container, false);

        mAuth = FirebaseAuth.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();
        db = FirebaseFirestore.getInstance();

        if (mAuth.getCurrentUser() == null) {
            Toast.makeText(getContext(), "User not authenticated", Toast.LENGTH_SHORT).show();
            return rootView;
        }

        userId = mAuth.getCurrentUser().getUid();

        ImageView profilePicture = rootView.findViewById(R.id.profilepicture);
        fetchProfilePicture(profilePicture);

        retrieveUserFullName(); // Fetch the user's full name
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

    private void retrieveUserFullName() {
        db.collection("users").document(userId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String firstName = documentSnapshot.getString("First Name");
                        String middleName = documentSnapshot.getString("Middle Name");
                        String lastName = documentSnapshot.getString("Last Name");

                        // Concatenate the fields to form the full name
                        userFullName = String.format("%s %s %s", firstName, middleName, lastName).trim();

                        // Log or use the full name as needed
                        Log.d("JobListingFragment", "User Full Name: " + userFullName);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("JobListingFragment", "Failed to fetch user full name", e);
                    Toast.makeText(getContext(), "Failed to fetch user full name: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }

    private void renderListing(Listing listing, String ownerEmail) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View listingView = inflater.inflate(R.layout.post_listing, null);

        TextView companyNameTextView = listingView.findViewById(R.id.company_name_post);
        TextView jobTitleTextView = listingView.findViewById(R.id.title_post);
        TextView payRangeTextView = listingView.findViewById(R.id.pay_range_post);
        TextView detailsTextView = listingView.findViewById(R.id.details_post);
        TextView requirements1TextView = listingView.findViewById(R.id.requirements1_post);
        TextView requirements2TextView = listingView.findViewById(R.id.requirements2_post);
        TextView requirements3TextView = listingView.findViewById(R.id.requirements3_post);
        TextView hasBenefitsTextView = listingView.findViewById(R.id.benefits_post);
        Button applyButton = listingView.findViewById(R.id.apply_button);

        companyNameTextView.setText("Company Name/Individual Name: " + listing.getCompanyName());
        jobTitleTextView.setText("Job Title: " + listing.getJobTitle());
        payRangeTextView.setText("Pay Range: " + listing.getPayRange());
        detailsTextView.setText("Details: " + listing.getDetails());
        requirements1TextView.setText("Requirements 1: " + listing.getRequirements1());
        requirements2TextView.setText("Requirements 2: " + listing.getRequirements2());
        requirements3TextView.setText("Requirements 3: " + listing.getRequirements3());
        hasBenefitsTextView.setText("Has Benefits: " + listing.getHasBenefits());

        applyButton.setOnClickListener(v -> sendEmail(listing.getJobTitle(), ownerEmail));

        LinearLayout listingContainer = getView().findViewById(R.id.listingContainer);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(40, 40, 40, 40);
        listingView.setLayoutParams(layoutParams);
        listingContainer.addView(listingView);
    }

    private void sendEmail(String jobTitle, String ownerEmail) {
        String subject = "Application for " + jobTitle;
        String body = "Dear Sir/Madam,\n\nI am interested in applying for the position of " + jobTitle + ". Can you fill me in with the details and requirements.\n\nBest regards,\n" + userFullName;

        Intent emailIntent = new Intent(Intent.ACTION_SENDTO);
        emailIntent.setData(Uri.parse("mailto:")); // Only email apps should handle this
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{ownerEmail});
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
        emailIntent.putExtra(Intent.EXTRA_TEXT, body);

        try {
            startActivity(Intent.createChooser(emailIntent, "Send email using..."));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getContext(), "No email client found", Toast.LENGTH_SHORT).show();
        }
    }

//    public void retrieveListings() {
//        db.collectionGroup("user_jobs")
//                .whereEqualTo("status", "approved")
//                .orderBy("jobTitle", Query.Direction.ASCENDING)
//                .get()
//                .addOnSuccessListener(queryDocumentSnapshots -> {
//                    if (queryDocumentSnapshots.isEmpty()) {
//                        Toast.makeText(getContext(), "No approved listings found.", Toast.LENGTH_SHORT).show();
//                    } else {
//                        for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
//                            Listing listing = documentSnapshot.toObject(Listing.class);
//                            String ownerId = documentSnapshot.getReference().getParent().getParent().getId();
//                            fetchOwnerEmail(ownerId, listing);
//                        }
//                    }
//                })
//                .addOnFailureListener(e -> {
//                    Log.e("JobListingFragment", "Failed to retrieve listings", e);
//                    Toast.makeText(getContext(), "Failed to retrieve listings: " + e.getMessage(), Toast.LENGTH_SHORT).show();
//                });
//    }

    public void retrieveListings() {
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
                            // Fetch the creator's email directly from the listing document
                            String ownerEmail = documentSnapshot.getString("creatorEmail");
                            renderListing(listing, ownerEmail);
                        }
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("JobListingFragment", "Failed to retrieve listings", e);
                    Toast.makeText(getContext(), "Failed to retrieve listings: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }


    private void fetchOwnerEmail(String ownerId, Listing listing) {
        db.collection("users").document(ownerId).get()
                .addOnSuccessListener(documentSnapshot -> {
                    if (documentSnapshot.exists()) {
                        String ownerEmail = documentSnapshot.getString("email");
                        renderListing(listing, ownerEmail);
                    }
                })
                .addOnFailureListener(e -> {
                    Log.e("JobListingFragment", "Failed to fetch owner email", e);
                    Toast.makeText(getContext(), "Failed to fetch owner email: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
    }
}