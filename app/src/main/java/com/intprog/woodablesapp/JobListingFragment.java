package com.intprog.woodablesapp;

import android.os.Bundle;
import androidx.fragment.app.Fragment;
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
        TextView titleTextView = listingView.findViewById(R.id.title_post);
        TextView descriptionTextView = listingView.findViewById(R.id.desc_post);

        titleTextView.setText(listing.getTitle());
        descriptionTextView.setText(listing.getDescription());

        // Add the listing view to your LinearLayout with appropriate margins
        LinearLayout listingContainer = getView().findViewById(R.id.listingContainer);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(40, 40, 40, 40); // Add bottom margin for gap
        listingView.setLayoutParams(layoutParams);
        listingContainer.addView(listingView);
    }

    public void retrieveListings() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("listings").get().addOnSuccessListener(queryDocumentSnapshots -> {
            for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                Listing listing = documentSnapshot.toObject(Listing.class);
                renderListing(listing);
            }
        }).addOnFailureListener(e -> {
            // Handle failure
        });
    }
}