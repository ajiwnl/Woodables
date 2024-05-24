package com.intprog.woodablesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MessageChatViewFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View viewRoot = inflater.inflate(R.layout.fragment_message_chat_view, container, false);

        LinearLayout linearLayoutUsers = viewRoot.findViewById(R.id.linear_layout_users);

        // Retrieve user data from Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
                .get()
                .addOnSuccessListener(documents -> {
                    for (DocumentSnapshot document : documents) {
                        // Get user data fields from Firestore document
                        String firstName = document.getString("First Name");
                        String lastName = document.getString("Last Name");

                        // Inflate the user layout XML
                        View userView = inflater.inflate(R.layout.message_user, null);

                        // Set user data to the user layout
                        TextView textViewName = userView.findViewById(R.id.username_message);
                        textViewName.setText(firstName + " " + lastName);

                        // Set OnClickListener to redirect to ChatPersonActivity
                        userView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                // Get the user ID from the Firestore document
                                String userID = document.getId();


                                // Start ChatPersonActivity and pass the selected user's ID
                                Intent intent = new Intent(getContext(), ChatPersonActivity.class);
                                intent.putExtra("userID", userID);
                                intent.putExtra("name", firstName);
                                startActivity(intent);
                            }
                        });


                        // Add the user layout to the LinearLayout
                        linearLayoutUsers.addView(userView);
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle any errors
                });

        return viewRoot;
    }
}
