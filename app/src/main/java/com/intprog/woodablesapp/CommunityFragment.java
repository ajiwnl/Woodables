package com.intprog.woodablesapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

public class CommunityFragment extends Fragment {

    ImageView addPost;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewRoot = inflater.inflate(R.layout.fragment_community, container, false);

        addPost = viewRoot.findViewById(R.id.addpost);

        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toPost = new Intent(viewRoot.getContext(), CreatePostActivity.class);
                startActivity(toPost);
            }
        });


        return viewRoot;
    }
}