package com.intprog.woodablesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.FirebaseFirestore;

public class CreatePostActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int GALLERY_REQUEST_CODE = 2;
    EditText title, content;
    Button clickPost;
    ImageView closeview, toCam, toGal;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);


        title = findViewById(R.id.titletxt);
        content = findViewById(R.id.contenttxt);
        clickPost = findViewById(R.id.postbtn);
        closeview = findViewById(R.id.close);
        toCam = findViewById(R.id.toCamera);
        toGal = findViewById(R.id.toGallery);

        clickPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String postTitle = title.getText().toString();
                String postContent = content.getText().toString();
                SharedPreferences preferences = getSharedPreferences("user_info", MODE_PRIVATE);
                String name = "w/" + preferences.getString("name", "");
                String role = preferences.getString("role", "");


                // Check if title and content are not empty
                if (!postTitle.isEmpty() && !postContent.isEmpty()) {
                    // Call the uploadPost method to store the post in Firestore
                    uploadPost(postTitle, postContent, name);

                    // Clear EditText fields
                    title.getText().clear();
                    content.getText().clear();
                } else {
                    // Show a Snackbar or toast message indicating that fields are empty
                    Snackbar.make(v, "Title and content cannot be empty", Snackbar.LENGTH_SHORT).show();
                }
            }
        });


        closeview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
                overridePendingTransition(R.anim.slide_out_right, R.anim.slide_in_left);
            }
        });

        toCam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (cameraIntent.resolveActivity(getPackageManager()) != null) {

                    startActivityForResult(cameraIntent, CAMERA_REQUEST_CODE);
                } else {
                    Log.d("CreatePostActivity", "No Camera found");
                }
            }
        });

        toGal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galleryIntent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                if (galleryIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(galleryIntent, GALLERY_REQUEST_CODE);
                } else {
                    Log.d("CreatePostActivity", "No app found to handle gallery intent");
                }
            }
        });
    }

    public void uploadPost(String title, String message, String userName) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        Post post = new Post(title, message, userName);

        db.collection("posts")
                .add(post)
                .addOnSuccessListener(documentReference -> {
                    // Post added successfully
                    Log.d("Firestore", "Post added successfully with ID: " + documentReference.getId());
                })
                .addOnFailureListener(e -> {
                    // Handle failure
                    Log.e("Firestore", "Error adding post: " + e.getMessage(), e);
                });
    }
}
