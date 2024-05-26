package com.intprog.woodablesapp;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
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
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat_person);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

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

            // Immediately update the UI with the new message
            updateUIWithNewMessage(message);

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

                // Initialize an empty list to store all messages
                List<Map<String, Object>> allMessages = new ArrayList<>();

                // Query Firestore for messages between the current user and the selected user
                db.collection("messages")
                        .whereEqualTo("sender", currentUserId)
                        .whereEqualTo("receiver", selectedUserID)
                        .orderBy("timestamp", Query.Direction.ASCENDING) // Order by timestamp in ascending order
                        .get()
                        .addOnSuccessListener(queryDocumentSnapshots -> {
                            // Add all messages sent by the current user to the list
                            for (DocumentSnapshot document : queryDocumentSnapshots) {
                                Map<String, Object> messageData = document.getData();
                                allMessages.add(messageData);
                            }

                            // Query Firestore for messages sent by the selected user to the current user
                            db.collection("messages")
                                    .whereEqualTo("sender", selectedUserID)
                                    .whereEqualTo("receiver", currentUserId)
                                    .orderBy("timestamp", Query.Direction.ASCENDING) // Order by timestamp in ascending order
                                    .get()
                                    .addOnSuccessListener(queryDocumentSnapshots1 -> {
                                        // Add all messages sent by the selected user to the list
                                        for (DocumentSnapshot document : queryDocumentSnapshots1) {
                                            Map<String, Object> messageData = document.getData();
                                            allMessages.add(messageData);
                                        }

                                        // Sort all messages based on their timestamps
                                        Collections.sort(allMessages, (message1, message2) -> {
                                            long timestamp1 = (long) message1.get("timestamp");
                                            long timestamp2 = (long) message2.get("timestamp");
                                            return Long.compare(timestamp1, timestamp2);
                                        });

                                        // Get the LinearLayout container for messages
                                        LinearLayout messageContainer = findViewById(R.id.messageContainer);
                                        messageContainer.removeAllViews(); // Clear existing messages

                                        // Iterate through the sorted messages and display them
                                        for (Map<String, Object> message : allMessages) {
                                            // Process each message
                                            processMessage(message, messageContainer, currentUserId);
                                        }
                                    })
                                    .addOnFailureListener(e -> Log.e("LoadMessages", "Error getting messages: ", e));
                        })
                        .addOnFailureListener(e -> Log.e("LoadMessages", "Error getting messages: ", e));
            }
        }
    }





    private void processMessage(Map<String, Object> message, LinearLayout messageContainer, String currentUserId) {
        // Extract message details from the map
        String messageText = (String) message.get("message");
        String senderID = (String) message.get("sender");
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
            messageTextView.setTextColor(Color.WHITE); // Set text color to black
        }
        messageView.setLayoutParams(layoutParams);

        // Add the inflated layout to the message container
        messageContainer.addView(messageView);
    }


    private void updateUIWithNewMessage(Map<String, Object> message) {
        // Get the LinearLayout container for messages
        LinearLayout messageContainer = findViewById(R.id.messageContainer);

        // Process the new message and add it to the UI
        processMessage(message, messageContainer, FirebaseAuth.getInstance().getCurrentUser().getUid());
    }
}


