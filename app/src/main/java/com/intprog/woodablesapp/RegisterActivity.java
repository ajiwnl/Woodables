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

public class RegisterActivity extends AppCompatActivity {

    TextView logclick;
    Button registerbtn;
    EditText passText,  emailText;
    Intent tologin;
    ProgressDialog progressDialog;

    FirebaseAuth mAuth;
    FirebaseUser mUser;



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
        // Check password
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


    private void PerformAuth(){
        String password = passText.getText().toString();
        String email = emailText.getText().toString();

        if(!email.matches(emailPattern)){
            emailText.setError("Enter Correct Email");
        } else if (!isPasswordValid(password)){
            passText.setError("Password must contain at least 8 characters, including at least one uppercase letter, one or more lowercase letters, one or more numbers, and one special character");
        } else {
            progressDialog.setTitle("Registration");
            progressDialog.setMessage("Registering for a while, Please wait");
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();

            // Check if email already exists
            mAuth.fetchSignInMethodsForEmail(email).addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    if (task.getResult().getSignInMethods().size() > 0) {
                        // Email already exists
                        progressDialog.dismiss();
                        Toast.makeText(RegisterActivity.this, "Email already exists, please use a different email.", Toast.LENGTH_LONG).show();
                        // Clear input fields
                        emailText.setText("");
                        passText.setText("");
                    } else {
                        // Email does not exist, proceed with registration
                        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(authTask -> {
                            if (authTask.isSuccessful()){
                                // Send verification email
                                sendVerificationEmail();
                            } else {
                                progressDialog.dismiss();
                                Toast.makeText(RegisterActivity.this, "Registration failed: " + authTask.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                } else {
                    progressDialog.dismiss();
                    Toast.makeText(RegisterActivity.this, "Error checking email existence: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    private void sendVerificationEmail() {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            user.sendEmailVerification().addOnCompleteListener(task -> {
                progressDialog.dismiss();
                if (task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Verification email sent. Please check your email to verify your account.", Toast.LENGTH_LONG).show();
                    mAuth.signOut(); // Sign out the user after sending the verification email
                    toLoginPage();
                } else {
                    Toast.makeText(RegisterActivity.this, "Failed to send verification email: " + task.getException().getMessage(), Toast.LENGTH_LONG).show();
                }
            });
        }
    }


    private void toLoginPage() {

        tologin = new Intent(RegisterActivity.this, LoginActivity.class);
        tologin.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(tologin);
    }

}