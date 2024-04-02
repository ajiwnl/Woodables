package com.intprog.woodablesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    TextView logclick;
    Button registerbtn;
    EditText userText;
    EditText passText;
    EditText emailText;
    EditText lNameText;
    EditText fNameText;
    EditText mNameText;
    EditText dobText;
    EditText addText;
    EditText phoneText;
    Intent tologin;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        logclick = findViewById(R.id.loginhere);
        registerbtn = findViewById(R.id.regclick);
        userText = findViewById(R.id.userName);
        passText = findViewById(R.id.password);
        emailText = findViewById(R.id.email);
        lNameText = findViewById(R.id.lName);
        fNameText = findViewById(R.id.fName);
        mNameText = findViewById(R.id.mName);
        dobText = findViewById(R.id.dobpick);
        addText = findViewById(R.id.address);
        phoneText = findViewById(R.id.phonenum);


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
                String username = userText.getText().toString();
                String password = passText.getText().toString();
                String email = emailText.getText().toString();
                String lname = lNameText.getText().toString();
                String fname = fNameText.getText().toString();
                String mname = mNameText.getText().toString();
                String dob = dobText.getText().toString();
                String address = addText.getText().toString();
                String pnumber = phoneText.getText().toString();

                tologin = new Intent(RegisterActivity.this, LoginActivity.class);
                tologin.putExtra("Username", username);
                tologin.putExtra("Password", password);
                tologin.putExtra("Email", email);
                tologin.putExtra("LName", lname);
                tologin.putExtra("FName", fname);
                tologin.putExtra("MName", mname);
                tologin.putExtra("DOB", dob);
                tologin.putExtra("Address", address);
                tologin.putExtra("Phone", pnumber);
                startActivity(tologin);
            }
        });

    }
}