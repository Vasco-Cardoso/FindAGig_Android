package com.findagig;

import android.os.Bundle;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import java.io.IOException;

import com.findagig.MyAppGlideModule;


public class SettingsPage extends AppCompatActivity {
    // Tag
    private static final String TAG = "Settings";

    // Firebase variables
    private FirebaseFirestore db;
    private FirebaseAuth mAuth;
    private FirebaseStorage storage ;

    // User variables to be set
    private String email;
    private String imagePath;
    private String name;
    private String userUID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.settings_page);

        // Initializing Firebase variables
        db = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();
        storage  = FirebaseStorage.getInstance();

        if (mAuth.getCurrentUser() != null) {
            userUID = mAuth.getCurrentUser().getUid();
            getUserInfo(userUID);
        }
        else {
            Log.d(TAG, "Error getting user.");
        }
    }

    private void getUserInfo(final String userUID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (document.getId().equals(userUID)) {
                                name = document.getData().get("name").toString();
                                email = document.getData().get("email").toString();
                                imagePath = document.getData().get("image").toString();
                                try {
                                    getImage(userUID);
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                Log.d(TAG, document.getId() + " => " + name + ", " + email + ", " + imagePath);
                            }
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
            });
    }

    private void getImage(String userUID) throws IOException {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        StorageReference pathReference = storageRef.child("avatars/" + userUID);
        Log.d(TAG, "Image path: " + pathReference.toString());

        // ImageView in your Activity
        ImageView imageView = findViewById(R.id.imageView_settings);

        // Load the image using Glide
        GlideApp.with(this)
                .load(pathReference)
                .into(imageView);



    }

}
