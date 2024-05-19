package com.intprog.woodablesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AdminAssesmentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_assesment);

        Button toListingButton = findViewById(R.id.toListing);

        // Set an onClickListener on the button
        toListingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to start AdminActivity
                Intent intent = new Intent(AdminAssesmentActivity.this, AdminActivity.class);
                startActivity(intent);
            }
        });
    }
}