package com.findagig.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.findagig.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LogInPageActivity extends AppCompatActivity {
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
            Intent i = new Intent(getApplicationContext(), MainMenuActivity.class);
            startActivity(i);
        }
        else {

        }

    }

    public void signIn(View view) {
        Toast.makeText(getApplicationContext(), "Logging in...", Toast.LENGTH_SHORT).show();

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
                    Toast.makeText(LogInPageActivity.this, "Authentication success.",
                            Toast.LENGTH_SHORT).show();

                    Intent i = new Intent(getApplicationContext(), MainMenuActivity.class);
                    startActivity(i);

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(LogInPageActivity.this, "Authentication failed.",
                            Toast.LENGTH_SHORT).show();
                }
                }
            });
        // [END sign_in_with_email]

    }

    public void onClick(View view) {
        Intent i = new Intent(getApplicationContext(), RegisterActivity.class);
        startActivity(i);
    }
}
