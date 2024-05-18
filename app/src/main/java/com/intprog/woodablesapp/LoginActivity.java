package com.intprog.woodablesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

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
                                if (user.isEmailVerified()) {
                                    Intent toUserProfile = new Intent(LoginActivity.this, MainScreenActivity.class);

                                    startActivity(toUserProfile);
                                    Toast.makeText(LoginActivity.this, "Login successful", Toast.LENGTH_LONG).show();
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
