package com.intprog.woodablesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class RegisterActivity extends AppCompatActivity {

    TextView logclick;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        logclick = findViewById(R.id.loginhere);

        logclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent tologin = new Intent(RegisterActivity.this, LoginActivity.class);
                startActivity(tologin);
            }
        });

    }
}