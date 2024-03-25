package com.intprog.woodablesapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class ForgotPasswordActivity extends AppCompatActivity {
    EditText forgotemail;
    Button recoverBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        forgotemail = findViewById(R.id.forgotemail);
        recoverBtn = findViewById(R.id.recoverbtn);

        recoverBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = forgotemail.getText().toString().trim();

                Log.d("ForgotPasswordActivity","Email Input is:" +email);
                showConfirmationDialog(email);
            }
        });
    }
    private void showConfirmationDialog(String email) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Please check your email to change your password.")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        Intent intent = new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("mailto:" + email));
                        intent.putExtra(Intent.EXTRA_SUBJECT, "Password Reset Request");
                        intent.putExtra(Intent.EXTRA_TEXT, "Please follow the instructions in the email to reset your password.");
                        startActivity(intent);
                    }
                });
        AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
}