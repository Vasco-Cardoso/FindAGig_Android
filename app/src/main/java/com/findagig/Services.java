package com.findagig;

import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Services {
    private static final String TAG = "Services";
    private FirebaseAuth mAuth;

    public static void uploadImage() {
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();
        Uri file = Uri.fromFile(new File("path/to/images/rivers.jpg"));
        StorageReference riversRef = storageRef.child("avatars/"+"afonso");
        UploadTask uploadTask = riversRef.putFile(file);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                Log.d(TAG, "=> Image written!");
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Log.d(TAG, "=> Image not written!");
            }
        });
    }

    public static void addUserRegistry(String mail, String password, String name) {

        final FirebaseFirestore db =  FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        Map<String, Object> user = new HashMap<>();
        user.put("email", mail);
        user.put("image", "https://www.pavilionweb.com/wp-content/uploads/2017/03/man-300x300.png");
        user.put("name", name);
        user.put("password", password);
        user.put("wallet", 0);

        Log.d(TAG, "ADDDING THIS USER: " + user.toString());

        db.collection("users")
                .document(mAuth.getCurrentUser().getUid())
                .set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "DocumentSnapshot successfully written!");
                        uploadImage();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    public static boolean registerUser(String mail, String password) {
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final boolean[] aux = new boolean[1];
        mAuth.createUserWithEmailAndPassword(mail, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()) {
                            aux[0] = true;
                            //logInUser();
                        }
                        else {
                            aux[0] = false;
                        }
                    }
                });

        // [END sign_in_with_email]
        return aux[0];
    }

    public static boolean logginIn(String email, String password) {
        final FirebaseAuth mAuth = FirebaseAuth.getInstance();
        final boolean[] aux = new boolean[1];

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            aux[0] = true;

                        } else {
                            aux[0] = false;
                            // If sign in fails, display a message to the user.

                        }
                    }
                });
    return aux[0];
    }
}
