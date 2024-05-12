package com.intprog.woodablesapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;


public class CourseCatalogFragment extends Fragment {

    Button toSkillAssess;
    ImageView backBtn;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewRoot = inflater.inflate(R.layout.fragment_course_catalog, container, false);

        toSkillAssess = viewRoot.findViewById(R.id.skillassess);
        backBtn = viewRoot.findViewById(R.id.backbutton);

        toSkillAssess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toAssessment = new Intent(viewRoot.getContext(), AssessmentActivity.class);
                startActivity(toAssessment);
            }

        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                LearnCourseFragment learnFragment = new LearnCourseFragment();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.contentView, learnFragment);
                fragmentTransaction.commit();
            }
        });

        return viewRoot;

    }
}