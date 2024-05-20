package com.intprog.woodablesapp;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class CommunityFragment extends Fragment {

    ImageView addPost;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View viewRoot = inflater.inflate(R.layout.fragment_community, container, false);

        addPost = viewRoot.findViewById(R.id.addpost);

        addPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toPost = new Intent(viewRoot.getContext(), CreatePostActivity.class);
                startActivity(toPost);
            }
        });

        // Call retrievePosts to fetch and render posts from Firestore
        retrievePosts();

        return viewRoot;
    }

    public void retrievePosts() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("posts")
                .get()
                .addOnSuccessListener(queryDocumentSnapshots -> {
                    for (DocumentSnapshot documentSnapshot : queryDocumentSnapshots) {
                        Post post = documentSnapshot.toObject(Post.class);
                        renderPost(post);
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                });
    }

    public void renderPost(Post post) {
        // Inflate your post layout dynamically
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View postView = inflater.inflate(R.layout.post_item, null);

        // Populate views with post data
        TextView titleTextView = postView.findViewById(R.id.title);
        TextView messageTextView = postView.findViewById(R.id.text);
        TextView userNameTextView = postView.findViewById(R.id.postusername);

        titleTextView.setText(post.getTitle());
        messageTextView.setText(post.getMessage());
        userNameTextView.setText(post.getUserName());

        // Add the post view to your ScrollView
        LinearLayout postContainer = getView().findViewById(R.id.postContainer);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(40, 40, 40, 40);
        postView.setLayoutParams(layoutParams);
        postContainer.addView(postView);
    }
}
