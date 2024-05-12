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

    Button toAssess, toCatalog;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View viewRoot = inflater.inflate(R.layout.fragment_learn_course, container, false);

        toAssess = viewRoot.findViewById(R.id.skillassess);
        toCatalog = viewRoot.findViewById(R.id.browsecoursecatalog);


        toCatalog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
                CourseCatalogFragment catalogFragment = new CourseCatalogFragment();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                fragmentTransaction.replace(R.id.contentView, catalogFragment);
                fragmentTransaction.commit();
            }
        });

        toAssess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toAssess = new Intent(viewRoot.getContext(), AssessmentActivity.class);
                startActivity(toAssess);
            }
        });

        return viewRoot;
    }


}