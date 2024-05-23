package com.intprog.woodablesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class ChatPersonActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private String selectedUserID;
    private String selectedUsername;
    private EditText messageInput;
    private ImageView sendButton;
    private LinearLayout chatLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_person);

        // Get the selected user's ID from the intent extras
        selectedUserID = getIntent().getStringExtra("userID");
        selectedUsername = getIntent().getStringExtra("name");

        TextView receiverUser = findViewById(R.id.receiverUser);
        receiverUser.setText(selectedUsername);

        // Initialize Firestore
        db = FirebaseFirestore.getInstance();

        // Initialize views
        messageInput = findViewById(R.id.message_input);
        sendButton = findViewById(R.id.send_button);
        chatLayout = findViewById(R.id.chat_layout);

        // Set OnClickListener to the send button
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMessage();
            }
        });

        // Load messages for the selected user
        loadMessages(selectedUserID);
    }

    private void sendMessage() {
        // Get the message text from the EditText
        String messageText = messageInput.getText().toString().trim();

        // Check if the message is not empty
        if (!messageText.isEmpty()) {
            // Get the sender's ID (current user's ID)
            String senderID = FirebaseAuth.getInstance().getCurrentUser().getUid();

            // Get the current timestamp
            long timestamp = System.currentTimeMillis();

            // Create a new message document in Firestore
            Map<String, Object> message = new HashMap<>();
            message.put("sender", senderID);
            message.put("receiver", selectedUserID); // Use the selected user's ID as the receiver
            message.put("message", messageText);
            message.put("timestamp", timestamp); // Include the timestamp
            // Add any other fields as needed

            // Save the message to Firestore
            FirebaseFirestore.getInstance().collection("messages")
                    .add(message)
                    .addOnSuccessListener(documentReference -> {
                        // Message saved successfully
                        // You can add any UI update or notification here
                    })
                    .addOnFailureListener(e -> {
                        // Failed to save message
                        // Handle the error appropriately
                    });

            // Clear the EditText after sending the message
            messageInput.setText("");
        }
    }

    private void loadMessages(String selectedUserID) {
        if (selectedUserID != null) {
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if (currentUser != null) {
                String currentUserId = currentUser.getUid();

                // Query Firestore for messages between the current user and the selected user
                db.collection("messages")
                        .whereEqualTo("sender", currentUserId)
                        .whereEqualTo("receiver", selectedUserID)
                        .orderBy("timestamp", Query.Direction.ASCENDING) // Order by timestamp in descending order
                        .addSnapshotListener((querySnapshot, error) -> {
                            if (error != null) {
                                Log.e("LoadMessages", "Error getting messages: ", error);
                                return;
                            }

                            if (querySnapshot != null) {
                                // Get the LinearLayout container for messages
                                LinearLayout messageContainer = findViewById(R.id.messageContainer);
                                messageContainer.removeAllViews(); // Clear existing messages

                                // Iterate through the messages and display them
                                for (QueryDocumentSnapshot document : querySnapshot) {
                                    // Process each message
                                    processMessage(document, messageContainer, currentUserId);
                                }
                            }
                        });
            }
        }
    }


    private void processMessage(QueryDocumentSnapshot document, LinearLayout messageContainer, String currentUserId) {
        String messageText = document.getString("message");
        String senderID = document.getString("sender");
        Log.d("Message", "Sender: " + senderID + ", Message: " + messageText);

        // Inflate the chat message layout
        View messageView = LayoutInflater.from(ChatPersonActivity.this).inflate(R.layout.chat_message_layout, null);

        // Find views within the inflated layout
        TextView messageTextView = messageView.findViewById(R.id.message_text);
        ImageView senderImage = messageView.findViewById(R.id.message_sender_image);

        // Set message text
        messageTextView.setText(messageText);

        // Set background colors and gravity based on sender and receiver
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.WRAP_CONTENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
        );
        if (senderID.equals(currentUserId)) {
            // Set layout parameters for sender messages (right-aligned, green background)
            layoutParams.gravity = Gravity.END | Gravity.CENTER_VERTICAL; // Align with end and center vertically
            messageView.setBackgroundResource(R.drawable.bluemsg_bg); // Add your sender background drawable

            // Set sender's image and text color
            messageTextView.setTextColor(Color.WHITE); // Set text color to white
        } else {
            // Set layout parameters for receiver messages (left-aligned, grey background)
            layoutParams.gravity = Gravity.START | Gravity.CENTER_VERTICAL; // Align with start and center vertically
            messageView.setBackgroundResource(R.drawable.whitemsg_bg); // Add your sender background drawable

            // Set receiver's image and text color
            messageTextView.setTextColor(Color.BLACK); // Set text color to black
        }
        messageView.setLayoutParams(layoutParams);

        // Add the inflated layout to the message container
        messageContainer.addView(messageView);
    }
}


