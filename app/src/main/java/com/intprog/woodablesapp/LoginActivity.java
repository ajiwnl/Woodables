package com.intprog.woodablesapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import com.google.android.material.snackbar.Snackbar;

public class LoginActivity extends AppCompatActivity {

    //Default Credential
    public static String username = "bonk";
    public static String password = "alvin123";
    public static String email;
    public static String lastname;
    public static String firstname;
    public static String middlename;
    public static String dateofbirth;
    public static String address;
    public static String phonenum;

    EditText userEditText;
    EditText passEditText;
    Button toProfile;
    TextView toForgot;
    TextView toRegister;
    // Method to show a dialog for invalid username
    private void showInvalidUsernameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Invalid Username")
                .setMessage("The username you entered is incorrect. Please try again.")
                .setPositiveButton("OK", null); // You can add an OnClickListener for the button if needed
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    // Method to show a dialog for invalid password
    private void showInvalidPasswordDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Invalid Password")
                .setMessage("The password you entered is incorrect. Please try again.")
                .setPositiveButton("OK", null); // You can add an OnClickListener for the button if needed
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        userEditText = findViewById(R.id.userIn);
        passEditText = findViewById(R.id.passIn);
        toProfile = findViewById(R.id.toprofilelogin);
        toForgot = findViewById(R.id.forgotpassword);
        toRegister = findViewById(R.id.register);

        Intent intent = getIntent();
        if(intent != null) {
            //Overwrite Data
            username = intent.getStringExtra("Username");
            password = intent.getStringExtra("Password");
            email = intent.getStringExtra("Email");
            lastname = intent.getStringExtra("LName");
            firstname = intent.getStringExtra("FName");
            middlename = intent.getStringExtra("MName");
            dateofbirth = intent.getStringExtra("DOB");
            address = intent.getStringExtra("Address");
            phonenum = intent.getStringExtra("Phone");
        }

        toProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String inUser = userEditText.getText().toString();
                String inPass = passEditText.getText().toString();

                Log.d("LoginActivity", "Clicked login button");
                Log.d("LoginActivity", "Entered username: " + inUser);
                Log.d("LoginActivity", "Entered password: " + inPass);

                if(inUser.equals(username) && inPass.equals(password)) {
                    Intent navprofile = new Intent(LoginActivity.this,ProfileActivity.class);
                  
                    navprofile.putExtra("Username", username);
                    navprofile.putExtra("Password", password);
                    navprofile.putExtra("Email", email);
                    navprofile.putExtra("LName", lastname);
                    navprofile.putExtra("FName", firstname);
                    navprofile.putExtra("MName", middlename);
                    navprofile.putExtra("DOB", dateofbirth);
                    navprofile.putExtra("Address", address);
                    navprofile.putExtra("Phone", phonenum);
                    startActivity(navprofile);
                }else {
                    if (!inUser.equals(username)) {
                        Snackbar.make(v, "Invalid Credentials, please try again", Snackbar.LENGTH_LONG).show();
                    } else {
                        Snackbar.make(v, "Invalid Credentials, please try again", Snackbar.LENGTH_LONG).show();
                    }
                }
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
                Intent navreg = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(navreg);
            }
        });
    }



}