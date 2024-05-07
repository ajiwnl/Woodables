package com.intprog.woodablesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.google.android.material.snackbar.Snackbar;

public class CreatePostActivity extends AppCompatActivity {

    private static final int CAMERA_REQUEST_CODE = 1;
    private static final int GALLERY_REQUEST_CODE = 1;
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
                Intent toPost = new Intent(CreatePostActivity.this, DummyCreatePostActivity.class);
                toPost.putExtra("Title here",title.getText().toString());
                toPost.putExtra("Content here",content.getText().toString());
                startActivity(toPost);

                // Clear EditText fields
                title.getText().clear();
                content.getText().clear();

            }
        });

        closeview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent toCommunity = new Intent(CreatePostActivity.this, CommunityActivity.class);
                startActivity(toCommunity);

                // Clear EditText fields
                title.getText().clear();
                content.getText().clear();
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
}
