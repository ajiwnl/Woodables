package com.intprog.woodablesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class StartUpActivity extends AppCompatActivity {

    Button loginbtn,regbtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start_up);
        loginbtn = findViewById(R.id.loginBtn);
        regbtn = findViewById(R.id.regBtn);
        regbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent navreg = new Intent(StartUpActivity.this,UserInfoActivity.class);
                startActivity(navreg);
            }
        });
        loginbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent navlogin = new Intent(StartUpActivity.this,LoginActivity.class);
                startActivity(navlogin);
            }
        });
    }
}