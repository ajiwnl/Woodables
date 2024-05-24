package com.intprog.woodablesapp;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

public class AdminPostActivity extends AppCompatActivity {
    private LinearLayout postLinearLayout;
    private FirebaseFirestore db;
    private Button listingsButton, assessmentButton, postsButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_post);

        postLinearLayout = findViewById(R.id.postLinearLayout);
        listingsButton = findViewById(R.id.toListings);
        assessmentButton = findViewById(R.id.toAssessment);
        postsButton = findViewById(R.id.toPosts);
        db = FirebaseFirestore.getInstance();

        loadDocumentIds();

        listingsButton.setOnClickListener(v -> startActivity(new Intent(AdminPostActivity.this, AdminActivity.class)));
        assessmentButton.setOnClickListener(v -> startActivity(new Intent(AdminPostActivity.this, AdminAssesmentActivity.class)));
        postsButton.setOnClickListener(v -> startActivity(new Intent(AdminPostActivity.this, AdminPostActivity.class)));
    }

    private void loadDocumentIds() {
        db.collection("posts")
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            String documentId = document.getId();
                            String title = document.getString("title");
                            addDocumentIdToLayout(title, documentId);
                        }
                    } else {
                        // Handle the error
                        Toast.makeText(AdminPostActivity.this, "Error loading documents.", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void addDocumentIdToLayout(String title, String documentId) {
        LinearLayout documentLayout = new LinearLayout(this);
        documentLayout.setOrientation(LinearLayout.HORIZONTAL);
        documentLayout.setPadding(8, 8, 8, 8);

        TextView textView = new TextView(this);
        textView.setText(String.format("%s (ID: %s)", title, documentId));
        textView.setTextSize(16);
        textView.setLayoutParams(new LinearLayout.LayoutParams(
                0,
                LinearLayout.LayoutParams.WRAP_CONTENT,
                1
        ));

        Button deleteButton = new Button(this);
        deleteButton.setText("Delete");
        deleteButton.setOnClickListener(v -> deleteDocument(documentId, documentLayout));

        documentLayout.addView(textView);
        documentLayout.addView(deleteButton);

        postLinearLayout.addView(documentLayout);
    }

    private void deleteDocument(String documentId, View documentView) {
        db.collection("posts").document(documentId)
                .delete()
                .addOnSuccessListener(aVoid -> {
                    postLinearLayout.removeView(documentView);
                    Toast.makeText(AdminPostActivity.this, "Document deleted.", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    Toast.makeText(AdminPostActivity.this, "Error deleting document.", Toast.LENGTH_SHORT).show();
                });
    }
}
