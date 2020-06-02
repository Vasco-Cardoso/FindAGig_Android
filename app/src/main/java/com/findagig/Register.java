package com.findagig;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class Register extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final String TAG = "Register";

    EditText register_mail;
    EditText register_pass;
    EditText register_user_name;
    Button register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        register_mail = findViewById(R.id.username);
        register_pass = findViewById(R.id.password);
        register_user_name = findViewById(R.id.nickname);
        register = findViewById(R.id.register_btn);
        mAuth = FirebaseAuth.getInstance();

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "=> " + register_mail.getText().toString() + " -> " + register_pass.getText().toString());
                if(register_mail.getText().toString() != null
                        && register_mail.getText().toString().contains("@")
                        && register_pass.getText().toString() != null
                        && register_pass.getText().toString().length() > 6)
                {
                    boolean result_from_register = Services.registerUser(register_mail.getText().toString() , register_pass.getText().toString());
                    if (result_from_register) {
                        Toast.makeText(Register.this, "Register success.",
                                Toast.LENGTH_SHORT).show();
                    }
                    else {
                        Toast.makeText(Register.this, "Failed register.",
                                Toast.LENGTH_SHORT).show();
                    }
                    //registerUser();
                }
                else {
                    Toast.makeText(Register.this, "Check your parameters.",
                            Toast.LENGTH_SHORT).show();
                }

            }
        });

    }

    public void registerUser() {
        mAuth.createUserWithEmailAndPassword(register_mail.getText().toString(), register_pass.getText().toString())
            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if(task.isSuccessful()) {
                        Toast.makeText(Register.this, "Register success.",
                                Toast.LENGTH_SHORT).show();
                        logInUser();
                    }
                    else {
                        Toast.makeText(Register.this, "Register failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });

        // [END sign_in_with_email]
    }

    public void logInUser() {
        mAuth.signInWithEmailAndPassword(register_mail.getText().toString(), register_pass.getText().toString())
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(Register.this, "Authentication success.",
                                Toast.LENGTH_SHORT).show();

                        addUserRegistry(register_mail.getText().toString(), register_user_name.getText().toString(), register_pass.getText().toString());

                    } else {
                        // If sign in fails, display a message to the user.
                        Toast.makeText(Register.this, "Authentication failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });
    }

    public void addUserRegistry(String mail, String password, String name) {
        final FirebaseFirestore db =  FirebaseFirestore.getInstance();

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

                    Intent i = new Intent(getApplicationContext(), MainMenu.class);
                    startActivity(i);
                }
            })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                    }
                });
    }

    public void uploadImage() {
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
}
