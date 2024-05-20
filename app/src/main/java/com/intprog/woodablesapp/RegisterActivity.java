package com.intprog.woodablesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class RegisterActivity extends AppCompatActivity {

    TextView logclick;
    Button registerbtn;
    EditText passText, emailText;
    Intent tologin;
    ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    FirebaseUser mUser;
    FirebaseFirestore db;

    private static final String emailPattern = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        logclick = findViewById(R.id.loginhere);
        registerbtn = findViewById(R.id.regclick);
        passText = findViewById(R.id.password);
        emailText = findViewById(R.id.email);
        progressDialog = new ProgressDialog(this);
        mAuth = FirebaseAuth.getInstance();
        mUser = mAuth.getCurrentUser();
        db = FirebaseFirestore.getInstance();

        logclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tologin = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(tologin);
            }
        });

        registerbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PerformAuth();
            }
        });
    }

    private boolean isPasswordValid(String password) {
        if (password.length() < 8) {
            return false;
        }
        boolean hasSpecialChar = false;
        boolean hasUppercase = false;
        boolean hasLowercase = false;
        boolean hasNumber = false;

        for (char c : password.toCharArray()) {
            if ("!@#$%^&*()-_=+\\|[{]};:'\",<.>/?".indexOf(c) != -1) {
                hasSpecialChar = true;
            } else if (Character.isUpperCase(c)) {
                hasUppercase = true;
            } else if (Character.isLowerCase(c)) {
                hasLowercase = true;
            } else if (Character.isDigit(c)) {
                hasNumber = true;
            }
        }
        return hasSpecialChar && hasUppercase && hasLowercase && hasNumber;
    }

    private void PerformAuth() {
        String password = passText.getText().toString();
        String email = emailText.getText().toString();

        if (!email.matches(emailPattern)) {
            emailText.setError("Enter Correct Email");
        } else if (!isPasswordValid(password)) {
            passText.setError("Password must contain at least 8 characters, including at least one uppercase letter, one or more lowercase letters, one or more numbers, and one special character");
        } else {
            progressDialog.setTitle("Registration");
            progressDialog.setMessage("Registering for a while, Please wait");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if (task.getResult().getSignInMethods().size() > 0) {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Email already registered", Toast.LENGTH_SHORT).show();
                        // Clear email and password fields
                        emailText.setText("");
                        passText.setText("");
                    } else {
                        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    sendEmailVerification();
                                } else {
                                    progressDialog.dismiss();
                                    Toast.makeText(RegisterActivity.this, "" + task.getException(), Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
                    }
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "Failed to check email registration", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }
    private void sendEmailVerification() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if (task.isSuccessful()) {
                        saveUserData();
                        Toast.makeText(RegisterActivity.this, "Verification email sent. Please check your email.", Toast.LENGTH_SHORT).show();
                    } else {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Failed to send verification email: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }

    private void saveUserData() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String userID = user.getUid();

            Intent intent = getIntent();
            String lName = intent.getStringExtra("LName");
            String fName = intent.getStringExtra("FName");
            String mName = intent.getStringExtra("MName");
            String cName = intent.getStringExtra("CName");
            String role = intent.getStringExtra("Role");

            Map<String, Object> userData = new HashMap<>();
            userData.put("Email", user.getEmail());
            userData.put("Role", role);
            userData.put("First Name", fName);
            userData.put("Last Name", lName);
            userData.put("Middle Name", mName);

            if ("client".equals(role)) {
                userData.put("Company Name", cName);
            }

            db.collection("users").document(userID)
                    .set(userData)
                    .addOnSuccessListener(aVoid -> {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Registration Successful. Please verify your email.", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Failed to save user data: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
        }
    }
}