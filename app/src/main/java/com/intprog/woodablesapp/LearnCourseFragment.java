package com.intprog.woodablesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LearnCourseFragment extends Fragment {

    private static final int ASSESSMENT_REQUEST_CODE = 1;
    private Button toAssess, toCatalog, refreshButton;
    private LinearLayout enrolledCoursesContainer;
    private FirebaseFirestore db;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_learn_course, container, false);

        toAssess = viewRoot.findViewById(R.id.skillassess);
        toCatalog = viewRoot.findViewById(R.id.browsecoursecatalog);
        refreshButton = viewRoot.findViewById(R.id.refreshbutton);
        enrolledCoursesContainer = viewRoot.findViewById(R.id.enrolled_courses_container);
        db = FirebaseFirestore.getInstance();

        toCatalog.setOnClickListener(v -> {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            CourseCatalogFragment catalogFragment = new CourseCatalogFragment();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.contentView, catalogFragment);
            fragmentTransaction.commit();
        });

        toAssess.setOnClickListener(v -> {
            Intent toAssess = new Intent(viewRoot.getContext(), AssessmentActivity.class);
            startActivityForResult(toAssess, ASSESSMENT_REQUEST_CODE);
        });

        refreshButton.setOnClickListener(v -> refreshFragment());

        loadEnrolledCourses();

        return viewRoot;
    }

    private void refreshFragment() {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.contentView, new LearnCourseFragment());
        fragmentTransaction.commit();
    }


    private void loadEnrolledCourses() {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            db.collection("enrolled_courses").document(userId).collection("courses")
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (querySnapshot != null) {
                                enrolledCoursesContainer.removeAllViews(); // Clear existing views
                                for (QueryDocumentSnapshot document : querySnapshot) {
                                    String title = document.getString("title");
                                    String description = document.getString("description");
                                    String details = document.getString("details");
                                    addEnrolledCourseToUI(title, description, details);
                                }
                            }
                        } else {
                            Toast.makeText(getContext(), "Failed to load courses.", Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            Toast.makeText(getContext(), "User not authenticated. Please log in.", Toast.LENGTH_SHORT).show();
        }
    }

    private void addEnrolledCourseToUI(String title, String description, String details) {
        View courseItem = LayoutInflater.from(getContext()).inflate(R.layout.item_enrolled_course, enrolledCoursesContainer, false);

        TextView courseTitle = courseItem.findViewById(R.id.course_title);
        TextView courseDescription = courseItem.findViewById(R.id.course_description);

        courseTitle.setText(title);
        courseDescription.setText(description);

        courseItem.setOnClickListener(v -> showCourseDetailsDialog(title, description, details));

        enrolledCoursesContainer.addView(courseItem);
    }

    private void showCourseDetailsDialog(String title, String description, String details) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_learncourse_details, null);
        builder.setView(dialogView);

        TextView dialogTitle = dialogView.findViewById(R.id.dialog_course_title);
        TextView dialogDetails = dialogView.findViewById(R.id.dialog_course_details);
        ImageView buttonCancel = dialogView.findViewById(R.id.button_cancel);
        Button buttonDropCourse = dialogView.findViewById(R.id.button_drop);
        Button buttonLaunchCourse = dialogView.findViewById(R.id.button_launch);

        dialogTitle.setText(title);
        dialogDetails.setText(details);

        AlertDialog dialog = builder.create();

        buttonCancel.setOnClickListener(v -> dialog.dismiss());

        buttonDropCourse.setOnClickListener(v -> {
            // Logic to drop the course
            dropCourse(title);
            dialog.dismiss();
        });

        buttonLaunchCourse.setOnClickListener(v -> {
            // Logic to launch the course
            launchCourse(title);
            dialog.dismiss();
        });

        dialog.show();
    }

    private void dropCourse(String title) {
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if (currentUser != null) {
            String userId = currentUser.getUid();
            db.collection("enrolled_courses").document(userId).collection("courses")
                    .whereEqualTo("title", title)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful() && !task.getResult().isEmpty()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                document.getReference().delete()
                                        .addOnSuccessListener(aVoid -> {
                                            Toast.makeText(getContext(), "Course dropped successfully.", Toast.LENGTH_SHORT).show();
                                            loadEnrolledCourses(); // Reload the courses to update UI
                                        })
                                        .addOnFailureListener(e -> Toast.makeText(getContext(), "Failed to drop course.", Toast.LENGTH_SHORT).show());
                            }
                        } else {
                            Toast.makeText(getContext(), "Course not found.", Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void launchCourse(String title) {
        // Logic to launch the course
        Toast.makeText(getContext(), "Launching course: " + title, Toast.LENGTH_SHORT).show();
        // Implement your launch course logic here
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == ASSESSMENT_REQUEST_CODE && resultCode == getActivity().RESULT_OK) {
            // Handle the result from the assessment
        }
    }
}