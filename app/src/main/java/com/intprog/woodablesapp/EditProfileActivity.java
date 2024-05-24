package com.intprog.woodablesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.Nullable;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class EditProfileActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;

    ImageView backBtn;
    ImageView profilePicture;
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private String userId;
    private boolean profileUpdated = false;
    private boolean descriptionsUpdated = false;
    private boolean pictureUpdated = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        userId = mAuth.getCurrentUser().getUid();

        Button saveButton = findViewById(R.id.saveEdit);
        EditText firstNameEditText = findViewById(R.id.editFirstName);
        EditText middleNameEditText = findViewById(R.id.editMiddleName);
        EditText lastNameEditText = findViewById(R.id.editLastName);
        EditText companyNameEditText = findViewById(R.id.editCompanyName);

        LinearLayout companyNameField = findViewById(R.id.companyname);

        EditText desc2EditText = findViewById(R.id.profileDesc2);
        EditText desc3EditText = findViewById(R.id.profileDesc3);
        EditText desc4EditText = findViewById(R.id.profileDesc4);
        EditText desc5EditText = findViewById(R.id.profileDesc5);
        EditText desc6EditText = findViewById(R.id.profileDesc6);
        EditText desc7EditText = findViewById(R.id.profileDesc7);
        profilePicture = findViewById(R.id.profilepicture);
        ImageView pictureEdit = findViewById(R.id.camerlogo);

        backBtn = findViewById(R.id.backbutton);

        backBtn.setOnClickListener(v -> finish());


        // Fetch and populate user data
        db.collection("users").document(userId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                String firstName = documentSnapshot.getString("First Name");
                String middleName = documentSnapshot.getString("Middle Name");
                String lastName = documentSnapshot.getString("Last Name");
                String role = documentSnapshot.getString("Role");


                if(role.equals("client")){
                    String companyName = documentSnapshot.getString("Company Name");
                    companyNameEditText.setText(companyName);
                    companyNameField.setVisibility(View.VISIBLE);
                }else{
                    companyNameField.setVisibility(View.GONE);
                }
                firstNameEditText.setText(firstName);
                middleNameEditText.setText(middleName);
                lastNameEditText.setText(lastName);


            }
        }).addOnFailureListener(e -> {
            Toast.makeText(EditProfileActivity.this, "Error loading profile", Toast.LENGTH_SHORT).show();
        });

        // Fetch and populate profile descriptions
        db.collection("profile_descriptions").document(userId).get().addOnSuccessListener(documentSnapshot -> {
            if (documentSnapshot.exists()) {
                desc2EditText.setText(documentSnapshot.getString("desc2"));
                desc3EditText.setText(documentSnapshot.getString("desc3"));
                desc4EditText.setText(documentSnapshot.getString("desc4"));
                desc5EditText.setText(documentSnapshot.getString("desc5"));
                desc6EditText.setText(documentSnapshot.getString("desc6"));
                String creationDate = String.valueOf(FirebaseAuth.getInstance().getCurrentUser().getMetadata().getCreationTimestamp());
                desc7EditText.setText(getFormattedDate(Long.parseLong(creationDate)));
                desc7EditText.setEnabled(false); // Disable editing
            }
        }).addOnFailureListener(e -> {
            Toast.makeText(EditProfileActivity.this, "Error loading descriptions", Toast.LENGTH_SHORT).show();
        });

        // Fetch and populate profile picture
        storageReference.child("profile_pictures/" + userId).getDownloadUrl().addOnSuccessListener(uri -> {
            Glide.with(EditProfileActivity.this).load(uri).into(profilePicture);
        }).addOnFailureListener(e -> {
            Toast.makeText(EditProfileActivity.this, "Error loading profile picture", Toast.LENGTH_SHORT).show();
        });

        pictureEdit.setOnClickListener(v -> openFileChooser());

        saveButton.setOnClickListener(v -> {
            String firstName = firstNameEditText.getText().toString();
            String middleName = middleNameEditText.getText().toString();
            String lastName = lastNameEditText.getText().toString();
            String desc2 = desc2EditText.getText().toString();
            String desc3 = desc3EditText.getText().toString();
            String desc4 = desc4EditText.getText().toString();
            String desc5 = desc5EditText.getText().toString();
            String desc6 = desc6EditText.getText().toString();
            String desc7 = desc7EditText.getText().toString();

            // Check if any of the description fields are not empty
            boolean hasNonEmptyDescriptions = !desc2.isEmpty() || !desc3.isEmpty() || !desc4.isEmpty()
                    || !desc5.isEmpty() || !desc6.isEmpty() || !desc7.isEmpty();

            // Update user profile
            if (!firstName.isEmpty() || !middleName.isEmpty() || !lastName.isEmpty()) {
                Map<String, Object> userData = new HashMap<>();
                userData.put("First Name", firstName);
                userData.put("Middle Name", middleName);
                userData.put("Last Name", lastName);

                db.collection("users").document(userId)
                        .update(userData)
                        .addOnSuccessListener(aVoid -> {
                            profileUpdated = true;
                            showToastUpdate();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(EditProfileActivity.this, "Error updating profile", Toast.LENGTH_SHORT).show();
                        });
            } else {
                profileUpdated = true;
                showToastUpdate();
            }

            // Update descriptions if there are non-empty values
            if (hasNonEmptyDescriptions) {
                ProfileDescriptions profileDescriptions = new ProfileDescriptions(desc2, desc3, desc4, desc5, desc6, desc7);
                db.collection("profile_descriptions").document(userId)
                        .set(profileDescriptions)
                        .addOnSuccessListener(aVoid -> {
                            descriptionsUpdated = true;
                            showToastUpdate();
                        })
                        .addOnFailureListener(e -> {
                            Toast.makeText(EditProfileActivity.this, "Error updating descriptions", Toast.LENGTH_SHORT).show();
                        });
            } else {
                descriptionsUpdated = true;
                showToastUpdate();
            }

            String fullName = firstName + " " + middleName + " " + lastName;

            Intent intent = new Intent();
            intent.putExtra("FULL_NAME", fullName);
            intent.putExtra("DESC2", desc2);
            intent.putExtra("DESC3", desc3);
            intent.putExtra("DESC4", desc4);
            intent.putExtra("DESC5", desc5);
            intent.putExtra("DESC6", desc6);
            intent.putExtra("DESC7", desc7);

            setResult(Activity.RESULT_OK, intent);
        });
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri imageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
                profilePicture.setImageBitmap(bitmap);
                uploadImageToFirebase(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadImageToFirebase(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] data = baos.toByteArray();

        StorageReference profilePicRef = storageReference.child("profile_pictures/" + userId);
        profilePicRef.putBytes(data).addOnSuccessListener(taskSnapshot -> {
            profilePicRef.getDownloadUrl().addOnSuccessListener(uri -> {
                ProfilePictureManager.updateProfilePicture(EditProfileActivity.this, uri);
                Toast.makeText(EditProfileActivity.this, "Profile picture updated", Toast.LENGTH_SHORT).show();
                Glide.with(EditProfileActivity.this).load(uri).into(profilePicture);
            });
        }).addOnFailureListener(e -> {
            Toast.makeText(EditProfileActivity.this, "Error uploading profile picture", Toast.LENGTH_SHORT).show();
        });
    }
    private void showToastUpdate() {
        if (profileUpdated && descriptionsUpdated && pictureUpdated) {
            Toast.makeText(EditProfileActivity.this, "Profile, descriptions, and picture updated", Toast.LENGTH_SHORT).show();
            finish();
        } else if (profileUpdated && descriptionsUpdated) {
            Toast.makeText(EditProfileActivity.this, "Profile and descriptions updated", Toast.LENGTH_SHORT).show();
            finish();
        }
    }

    private String getFormattedDate(long timestamp) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.getDefault());
        return dateFormat.format(new Date(timestamp));
    }
}