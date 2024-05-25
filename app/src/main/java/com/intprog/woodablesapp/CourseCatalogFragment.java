package com.intprog.woodablesapp;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class CourseCatalogFragment extends Fragment {

    private Button toSkillAssess;
    private ImageView backBtn;
    private FirebaseFirestore db;
    private LinearLayout courseListContainer;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_course_catalog, container, false);

        courseListContainer = viewRoot.findViewById(R.id.course_list_container);
        db = FirebaseFirestore.getInstance();
        toSkillAssess = viewRoot.findViewById(R.id.skillassess);
        backBtn = viewRoot.findViewById(R.id.backbutton);

        toSkillAssess.setOnClickListener(v -> {
            Intent toAssessment = new Intent(viewRoot.getContext(), AssessmentActivity.class);
            startActivity(toAssessment);
        });

        backBtn.setOnClickListener(v -> {
            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            LearnCourseFragment learnFragment = new LearnCourseFragment();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.contentView, learnFragment);
            fragmentTransaction.commit();
        });

        loadCourses();
        return viewRoot;
    }

    private void loadCourses() {
        db.collection("course")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot != null) {
                            for (QueryDocumentSnapshot document : querySnapshot) {
                                String title = document.getString("title");
                                String description = document.getString("description");
                                String details = document.getString("details");
                                addCourseToUI(title, description, details);
                            }
                        }
                    } else {
                        // Handle the error
                    }
                });
    }

    private void addCourseToUI(String title, String description, String details) {
        View courseItem = LayoutInflater.from(getContext()).inflate(R.layout.item_course, courseListContainer, false);

        TextView courseTitle = courseItem.findViewById(R.id.course_title);
        TextView courseDescription = courseItem.findViewById(R.id.course_description);

        courseTitle.setText(title);
        courseDescription.setText(description);

        courseItem.setOnClickListener(v -> showCourseDetailsDialog(title, description, details));

        courseListContainer.addView(courseItem);
    }

    private void showCourseDetailsDialog(String title, String description, String details) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.dialog_course_details, null);
        builder.setView(dialogView);

        TextView dialogTitle = dialogView.findViewById(R.id.dialog_course_title);
        TextView dialogDetails = dialogView.findViewById(R.id.dialog_course_details);
        Button buttonCancel = dialogView.findViewById(R.id.button_cancel);
        Button buttonEnroll = dialogView.findViewById(R.id.button_enroll);

        dialogTitle.setText(title);
        dialogDetails.setText(details);

        AlertDialog dialog = builder.create();

        buttonCancel.setOnClickListener(v -> dialog.dismiss());

        buttonEnroll.setOnClickListener(v -> {
            Bundle bundle = new Bundle();
            bundle.putString("courseTitle", title);
            bundle.putString("courseDescription", details);

            LearnCourseFragment learnCourseFragment = new LearnCourseFragment();
            learnCourseFragment.setArguments(bundle);

            FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.contentView, learnCourseFragment);
            fragmentTransaction.commit();

            dialog.dismiss();
        });

        dialog.show();
    }
}
