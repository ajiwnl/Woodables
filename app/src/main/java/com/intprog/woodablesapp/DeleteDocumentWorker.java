package com.intprog.woodablesapp;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.work.Worker;
import androidx.work.WorkerParameters;
import com.google.firebase.firestore.FirebaseFirestore;

public class DeleteDocumentWorker extends Worker {
    public DeleteDocumentWorker(@NonNull Context context, @NonNull WorkerParameters params) {
        super(context, params);
    }

    @NonNull
    @Override
    public Result doWork() {
        String documentId = getInputData().getString("documentId");
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        if (documentId != null) {
            db.collection("assessment").document(documentId)
                    .delete()
                    .addOnSuccessListener(aVoid -> {
                        // Successfully deleted document
                    })
                    .addOnFailureListener(e -> {
                        // Handle the error
                    });
        }

        return Result.success();
    }
}