package com.intprog.woodablesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AdminAssesmentActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private ListView adminListView;
    private ArrayList<String> assessmentList;
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_assesment);

        Button toListingButton = findViewById(R.id.toListing);
        adminListView = findViewById(R.id.adminListView);

        db = FirebaseFirestore.getInstance();
        assessmentList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, R.layout.admin_assessment_list_item, assessmentList);
        adminListView.setAdapter(adapter);

        // Set an onClickListener on the button
        toListingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create an intent to start AdminActivity
                Intent intent = new Intent(AdminAssesmentActivity.this, AdminActivity.class);
                startActivity(intent);
            }
        });

        // Fetch assessment data from Firestore
        fetchAssessmentData();
    }

    private void fetchAssessmentData() {
        db.collection("assessment")
                .orderBy("lastName", Query.Direction.ASCENDING)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        assessmentList.clear();
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            // Get assessment data and add to the list
                            String lastName = document.getString("lastName");
                            String firstName = document.getString("firstName");
                            String middleName = document.getString("middleName");
                            String dateOfAssessment = document.getString("dateOfAssessment");
                            String expertise = document.getString("expertise");

                            String assessmentDetails = "Name: " + lastName + ", " + firstName + " " + middleName + "\n" +
                                    "Date of Assessment: " + dateOfAssessment + "\n" +
                                    "Expertise: " + expertise;

                            assessmentList.add(assessmentDetails);
                        }
                        adapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(AdminAssesmentActivity.this, "Failed to fetch assessment data", Toast.LENGTH_SHORT).show();
                    }
                });
    }
}