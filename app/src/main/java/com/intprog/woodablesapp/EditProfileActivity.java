package com.intprog.woodablesapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class EditProfileActivity extends AppCompatActivity {

    ImageView backBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Button saveButton = findViewById(R.id.saveEdit);
        EditText nameEditText = findViewById(R.id.editName1);
        EditText desc2EditText = findViewById(R.id.profileDesc2);
        EditText desc3EditText = findViewById(R.id.profileDesc3);
        EditText desc4EditText = findViewById(R.id.profileDesc4);
        EditText desc5EditText = findViewById(R.id.profileDesc5);
        EditText desc6EditText = findViewById(R.id.profileDesc6);
        EditText desc7EditText = findViewById(R.id.profileDesc7);

        backBtn = findViewById(R.id.backbutton);


        backBtn.setOnClickListener(v -> {
            finish();
        });

        saveButton.setOnClickListener(v -> {
            String name = nameEditText.getText().toString();
            String desc2 = desc2EditText.getText().toString();
            String desc3 = desc3EditText.getText().toString();
            String desc4 = desc4EditText.getText().toString();
            String desc5 = desc5EditText.getText().toString();
            String desc6 = desc6EditText.getText().toString();
            String desc7 = desc7EditText.getText().toString();

            Intent intent = new Intent();
            intent.putExtra("NAME", name);
            intent.putExtra("DESC2", desc2);
            intent.putExtra("DESC3", desc3);
            intent.putExtra("DESC4", desc4);
            intent.putExtra("DESC5", desc5);
            intent.putExtra("DESC6", desc6);
            intent.putExtra("DESC7", desc7);

            setResult(Activity.RESULT_OK, intent);
            finish();
        });
    }
}