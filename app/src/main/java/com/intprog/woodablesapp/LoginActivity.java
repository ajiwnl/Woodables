package com.intprog.woodablesapp;

import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.content.SharedPreferences;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class LoginActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    EditText emailEditText;
    EditText passEditText;
    Button toProfile;
    TextView toForgot;
    TextView toRegister;
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mAuth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        emailEditText = findViewById(R.id.email);
        passEditText = findViewById(R.id.password);
        toProfile = findViewById(R.id.toprofilelogin);
        toForgot = findViewById(R.id.forgotpassword);
        toRegister = findViewById(R.id.register);

        toProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = emailEditText.getText().toString().trim();
                String password = passEditText.getText().toString().trim();

                if (TextUtils.isEmpty(email)) {
                    emailEditText.setError("Email is required");
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    passEditText.setError("Password is required");
                    return;
                }

                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener(LoginActivity.this, task -> {
                            if (task.isSuccessful()) {
                                FirebaseUser user = mAuth.getCurrentUser();

                                if ("admin@admin.com".equals(email) || user.isEmailVerified()) {
                                    // Fetch user information from Firestore
                                    db.collection("users").document(user.getUid()).get()
                                            .addOnSuccessListener(documentSnapshot -> {
                                                if (documentSnapshot.exists()) {
                                                    String firstname = documentSnapshot.getString("First Name");
                                                    String lastname = documentSnapshot.getString("Last Name");
                                                    String middlename = documentSnapshot.getString("Middle Name");
                                                    String role = documentSnapshot.getString("Role");
                                                    Log.d("LoginActivity", "First Name: " + firstname + ", Middle Name: " + middlename + ", Role: " + role);

                                                    Intent toUserProfile = new Intent(LoginActivity.this, MainScreenActivity.class);
                                                    String fullName = firstname + " " + middlename + " " + lastname;
                                                    toUserProfile.putExtra("ROLE", role); // Add the role to the intent
                                                    toUserProfile.putExtra("FullName", fullName);
                                                    SharedPreferences preferences = getSharedPreferences("user_info", MODE_PRIVATE);
                                                    SharedPreferences.Editor editor = preferences.edit();
                                                    editor.putString("fullname", fullName);
                                                    editor.putString("role", role);
                                                    editor.apply();
                                                    startActivity(toUserProfile);
                                                    Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_LONG).show();
                                                } else {
                                                    Log.d("LoginActivity", "User document does not exist");
                                                    Toast.makeText(LoginActivity.this, "User document does not exist", Toast.LENGTH_LONG).show();
                                                }
                                            })
                                            .addOnFailureListener(e -> {
                                                Log.e("LoginActivity", "Error fetching user information", e);
                                                Toast.makeText(LoginActivity.this, "Error fetching user information", Toast.LENGTH_LONG).show();
                                            });
                                } else {
                                    Toast.makeText(LoginActivity.this, "Please verify your email address first.", Toast.LENGTH_LONG).show();
                                }
                            } else {
                                // If the task fails, check the exception to determine the cause
                                if (task.getException() != null) {
                                    String errorMessage = task.getException().getMessage();
                                    if (errorMessage.contains("password")) {
                                        // Password is incorrect
                                        Toast.makeText(LoginActivity.this, "Incorrect password. Please try again.", Toast.LENGTH_LONG).show();
                                    } else if (errorMessage.contains("email")) {
                                        // Email does not exist
                                        Toast.makeText(LoginActivity.this, "Email does not exist. Please check your email or register.", Toast.LENGTH_LONG).show();
                                    } else {
                                        // Other authentication errors
                                        Toast.makeText(LoginActivity.this, "Authentication failed: " + errorMessage, Toast.LENGTH_LONG).show();
                                    }
                                } else {
                                    // Other unexpected errors
                                    Toast.makeText(LoginActivity.this, "Authentication failed. Please check your credentials.", Toast.LENGTH_LONG).show();
                                }
                            }
                        });
            }
        });

        toForgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent navforgot = new Intent(LoginActivity.this, ForgotPasswordActivity.class);
                startActivity(navforgot);
            }
        });

        toRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent navreg = new Intent(LoginActivity.this, UserInfoActivity.class);
                startActivity(navreg);
            }
        });
    }
}