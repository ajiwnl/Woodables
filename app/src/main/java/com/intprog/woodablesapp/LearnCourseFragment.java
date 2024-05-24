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

public class LearnCourseFragment extends Fragment {

    private static final int ASSESSMENT_REQUEST_CODE = 1;
    Button toAssess, toCatalog;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View viewRoot = inflater.inflate(R.layout.fragment_learn_course, container, false);

        toAssess = viewRoot.findViewById(R.id.skillassess);
        toCatalog = viewRoot.findViewById(R.id.browsecoursecatalog);

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

        return viewRoot;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == ASSESSMENT_REQUEST_CODE && resultCode == getActivity().RESULT_OK) {

        }
    }
}