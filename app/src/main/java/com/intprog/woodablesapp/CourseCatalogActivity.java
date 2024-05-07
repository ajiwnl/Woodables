package com.intprog.woodablesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class CourseCatalogActivity extends AppCompatActivity {

    Button toSkillAssess;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_catalog);

        toSkillAssess = findViewById(R.id.skillassess);

        toSkillAssess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    Intent toAssessment = new Intent(CourseCatalogActivity.this, AssessmentActivity.class);
                    startActivity(toAssessment);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
}