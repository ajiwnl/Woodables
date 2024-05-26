package com.intprog.woodablesapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.firebase.auth.FirebaseAuth;

public class AdminMainScreen extends AppCompatActivity {

    Button tolisting, toassessment, topost;
    ImageView logoutimg;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_main_screen);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize FirebaseAuth
        mAuth = FirebaseAuth.getInstance();

        tolisting = findViewById(R.id.toListings);
        toassessment = findViewById(R.id.toAssessment);
        topost = findViewById(R.id.toPosts);
        logoutimg = findViewById(R.id.logout); // Initialize the logout image view

        replaceFragment(new AdminListingFragment());

        tolisting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new AdminListingFragment());
            }
        });

        toassessment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new AdminAssesmentFragment());
            }
        });

        topost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                replaceFragment(new AdminPostFragment());
            }
        });

        logoutimg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Sign out from Firebase
                mAuth.signOut();
                // Navigate back to the LoginActivity and clear the back stack
                Intent intent = new Intent(AdminMainScreen.this, LoginActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                // finish the current activity
                finish();
            }
        });
    }

    private void replaceFragment(Fragment frag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.anim.slide_out_right, R.anim.slide_in_left);
        fragmentTransaction.replace(R.id.contentViewAdmin, frag);
        fragmentTransaction.commit();
    }
}