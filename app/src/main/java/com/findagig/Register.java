package com.findagig;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
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
                    registerUser();
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
                        addUserRegistry();
                    }
                    else {
                        Toast.makeText(Register.this, "Register failed.",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            });

        // [END sign_in_with_email]
    }

    public void addUserRegistry() {
        final FirebaseFirestore db =  FirebaseFirestore.getInstance();

        Map<String, Object> user = new HashMap<>();
        user.put("email", register_mail.getText().toString());
        user.put("image", "https://www.pavilionweb.com/wp-content/uploads/2017/03/man-300x300.png");
        user.put("name", register_user_name.getText().toString());
        user.put("password", register_pass.getText().toString());
        Log.d(TAG, "ADDDING THIS USER: " + user.toString());

        db.collection("users")
            .add(user)
            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    Log.d(TAG, "DocumentSnapshot successfully written!" + documentReference.getId() );
                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Log.w(TAG, "Error writing document", e);
                }
            });
    }
}
