package com.intprog.woodablesapp;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.google.firebase.firestore.FirebaseFirestore;

public class ClientJobListFragment extends Fragment {

    private LinearLayout joblistingsLinearLayout;
    private FirebaseFirestore db;

    ImageView bkbutton;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewRoot = inflater.inflate(R.layout.fragment_client_job_list, container, false);
        //for add item/card
        joblistingsLinearLayout = viewRoot.findViewById(R.id.joblistingsLinearLayout);

        bkbutton = viewRoot.findViewById(R.id.backbutton);


        db = FirebaseFirestore.getInstance();

        //implement fetch with addlayout LoadUserJobList();


        bkbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getActivity().getSupportFragmentManager().getBackStackEntryCount() > 0) {
                    getActivity().getSupportFragmentManager().popBackStack();
                }
            }
        });

        return viewRoot;
    }


}