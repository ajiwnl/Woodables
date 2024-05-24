package com.intprog.woodablesapp;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class ProfilePictureManager {
    private static final String PREFS_NAME = "user_info";
    private static final String PROFILE_PICTURE_KEY = "profile_picture_url";

    public static void fetchProfilePicture(Context context, ImageView imageView) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        String profilePictureUrl = preferences.getString(PROFILE_PICTURE_KEY, null);

        if (profilePictureUrl != null) {
            Glide.with(context).load(profilePictureUrl).into(imageView);
        } else {
            String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
            StorageReference storageReference = FirebaseStorage.getInstance().getReference()
                    .child("profile_pictures/" + userId);
            storageReference.getDownloadUrl().addOnSuccessListener(uri -> {
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString(PROFILE_PICTURE_KEY, uri.toString());
                editor.apply();
                Glide.with(context).load(uri).into(imageView);
            }).addOnFailureListener(e -> {
                // Handle error
            });
        }
    }

    public static void updateProfilePicture(Context context, Uri uri) {
        SharedPreferences preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(PROFILE_PICTURE_KEY, uri.toString());
        editor.apply();
    }
}