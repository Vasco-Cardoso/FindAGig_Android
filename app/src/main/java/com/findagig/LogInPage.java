package com.findagig;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class LogInPage extends AppCompatActivity {
    private FirebaseAuth mAuth;
    private static final String TAG = "LogIn";

    EditText login_mail;
    EditText login_pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in_page);

        mAuth = FirebaseAuth.getInstance();

        if (mAuth.getCurrentUser() != null) {
            Intent i = new Intent(getApplicationContext(), MainMenu.class);
            startActivity(i);
        }
        else {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("gigs")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (!document.getBoolean("taken")) {
                                Log.d(TAG, document.getId() + " => " + document.getData().get("city") + ", " + document.getData().get("description") + ", " + document.getData().get("employer"));
                            }
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                    }
                });
        }
    }

    public void signIn(View view) {
        login_mail = findViewById(R.id.username);
        login_pass = findViewById(R.id.password);

        // [START sign_in_with_email]
        mAuth.signInWithEmailAndPassword(login_mail.getText().toString(), login_pass.getText().toString())
            .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    // Sign in success, update UI with the signed-in user's information
                    FirebaseUser user = mAuth.getCurrentUser();
                    Toast.makeText(LogInPage.this, "Authentication success.",
                            Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(getApplicationContext(), MainMenu.class);
                    startActivity(i);

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(LogInPage.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
                }
            });
        // [END sign_in_with_email]
    }

}
