package com.intprog.woodablesapp;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CreateJobCardFragment extends Fragment {

    private EditText compName, jobTitle, payRange, details, requirements1, requirements2, requirements3;
    private Button createButton;
    ImageView burgmenu;
    private FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_create_job_card, container, false);

        burgmenu = viewRoot.findViewById(R.id.burgermenucreate);



        // Initialize Firestore and FirebaseAuth
        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();

        // Reference UI elements
        compName = viewRoot.findViewById(R.id.compName);
        jobTitle = viewRoot.findViewById(R.id.jobTitle);
        payRange = viewRoot.findViewById(R.id.payRange);
        details = viewRoot.findViewById(R.id.details);
        requirements1 = viewRoot.findViewById(R.id.requirements1);
        requirements2 = viewRoot.findViewById(R.id.requirements2);
        requirements3 = viewRoot.findViewById(R.id.requirements3);
        createButton = viewRoot.findViewById(R.id.createButton);

        burgmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new ClientJobListFragment());
            }
        });

        // Set onClickListener for the button
        createButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveJobListing();
            }
        });

        return viewRoot;
    }

    private void saveJobListing() {
        String compNameStr = compName.getText().toString().trim();
        String jobTitleStr = jobTitle.getText().toString().trim();
        String payRangeStr = payRange.getText().toString().trim();
        String detailsStr = details.getText().toString().trim();
        String requirementsStr1 = requirements1.getText().toString().trim();
        String requirementsStr2 = requirements2.getText().toString().trim();
        String requirementsStr3 = requirements3.getText().toString().trim();

        if (compNameStr.isEmpty() || jobTitleStr.isEmpty() || payRangeStr.isEmpty() || detailsStr.isEmpty() || requirementsStr1.isEmpty()) {
            Toast.makeText(getActivity(), "Please fill in all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser == null) {
            Toast.makeText(getActivity(), "User not authenticated", Toast.LENGTH_SHORT).show();
            return;
        }

        String uid = currentUser.getUid();

        // Create a new job listing map
        Map<String, Object> jobListing = new HashMap<>();
        jobListing.put("companyName", compNameStr);
        jobListing.put("jobTitle", jobTitleStr);
        jobListing.put("payRange", payRangeStr);
        jobListing.put("details", detailsStr);
        jobListing.put("requirements1", requirementsStr1);
        jobListing.put("requirements2", requirementsStr2);
        jobListing.put("requirements3", requirementsStr3);

        // Save the job listing in the main "job_listings" collection with the user's UID as a sub-collection
        db.collection("job_listings").document(uid).collection("user_jobs")
                .add(jobListing)
                .addOnSuccessListener(documentReference -> {
                    Toast.makeText(getActivity(), "Job listing created, Wait for Admin Approval", Toast.LENGTH_SHORT).show();
                    // Clear input fields or navigate to another fragment/activity
                    clearFields();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(getActivity(), "Error creating job listing", Toast.LENGTH_SHORT).show();
                });
    }

    private void clearFields() {
        compName.setText("");
        jobTitle.setText("");
        payRange.setText("");
        details.setText("");
        requirements1.setText("");
        requirements2.setText("");
        requirements3.setText("");
    }

    private void replaceFragment(Fragment frag) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_out_right, R.anim.slide_in_left);
        fragmentTransaction.replace(R.id.contentView, frag);
        fragmentTransaction.commit();
    }
}
