package com.intprog.woodablesapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class AdminAssesmentActivity extends AppCompatActivity {

    private FirebaseFirestore db;
    private ListView listView;
    private AssessmentAdapter adapter;
    private List<Assessment> assessmentList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_assesment);

        db = FirebaseFirestore.getInstance();
        listView = findViewById(R.id.adminListView);
        assessmentList = new ArrayList<>();
        adapter = new AssessmentAdapter(this, assessmentList);
        listView.setAdapter(adapter);

        fetchAssessments();

        Button toListingButton = findViewById(R.id.toListing);
        toListingButton.setOnClickListener(v -> {
            Intent intent = new Intent(AdminAssesmentActivity.this, AdminActivity.class);
            startActivity(intent);
        });
    }

    private void fetchAssessments() {
        CollectionReference assessmentsRef = db.collection("assessments");
        assessmentsRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    assessmentList.clear();
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        Assessment assessment = document.toObject(Assessment.class);
                        assessmentList.add(assessment);
                        // Log the retrieved assessment
                        Log.d("Firestore", "Assessment: " + assessment.getFirstName() + " " + assessment.getLastName());
                    }
                    adapter.notifyDataSetChanged();
                    Log.d("Firestore", "Number of assessments: " + assessmentList.size());
                } else {
                    Toast.makeText(AdminAssesmentActivity.this, "Error getting documents: " + task.getException(), Toast.LENGTH_SHORT).show();
                    Log.e("Firestore", "Error getting documents: ", task.getException());
                }
            }
        });
    }
}