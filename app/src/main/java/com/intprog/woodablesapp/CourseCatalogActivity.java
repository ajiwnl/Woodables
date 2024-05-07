package com.intprog.woodablesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class CourseCatalogActivity extends AppCompatActivity {

    Button toSkillAssess;
    ImageView backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_course_catalog);

        toSkillAssess = findViewById(R.id.skillassess);
        backBtn = findViewById(R.id.backbutton);

        toSkillAssess.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    Intent toAssessment = new Intent(CourseCatalogActivity.this, AssessmentActivity.class);
                    startActivity(toAssessment);
            }

        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent backtoCourse = new Intent(CourseCatalogActivity.this, LearnCourseActivity.class);
                startActivity(backtoCourse);
            }
        });

    }
}