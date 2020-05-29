package com.findagig;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.findagig.ui.recyclercardview.Model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Description extends AppCompatActivity {
    private static final String TAG = "Description";
    private String documentID = null;

    TextView gigName;
    TextView gigEmployer;
    TextView gigDesc;
    TextView gigReward;

    Button seeMapButton;
    Button applyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        // textViews assigned to the respective ID
        gigName = findViewById(R.id.gig_name);
        gigEmployer = findViewById(R.id.gig_employer);
        gigDesc = findViewById(R.id.gig_desc);
        gigReward = findViewById(R.id.gig_reward);

        // buttons assigned to the respective ID
        seeMapButton = findViewById(R.id.btn_map);
        applyButton = findViewById(R.id.btn_apply);

        // Get the ID value so we can gather the document from Firestore
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            Log.d(TAG, "=> Vieram valores no bundle");
            documentID = bundle.getString("id");
            getInfo(documentID);
        }
        else {
            gigName.setText("Error");
            gigEmployer.setText("Error");
        }

        // Listeners for the buttons
        seeMapButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, "=> Click no seeMap");
            }
        });

        applyButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Log.d(TAG, "=> Click no applyButton");

                FirebaseFirestore db = FirebaseFirestore.getInstance();

                db.collection("gigs").document(documentID)
                    .update("taken", true)
                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            Log.d(TAG, "DocumentSnapshot successfully written!");

                            Toast.makeText(Description.this, "You successfully applied to the job.",
                                    Toast.LENGTH_SHORT).show();

                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error writing document", e);
                            Toast.makeText(Description.this, "Error writing document.",
                                    Toast.LENGTH_SHORT).show();
                        }
                    });

            }
        });



    }

    public void getInfo(final String id) {
        Log.d(TAG, "=> Entrei no getInfo");

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("gigs")
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (!document.getBoolean("taken"))
                                if (document.getId().equals(id)) {
                                    Log.d(TAG, document.getId() + " => " + document.getData().get("city") + ", " + document.getData().get("description") + ", " + document.getData().get("employer"));

                                    gigName.setText(document.getData().get("name").toString());
                                    gigEmployer.setText(document.getData().get("employer").toString());
                                    gigDesc.setText(document.getData().get("description").toString());
                                    gigReward.setText(document.getData().get("reward").toString());
                                }
                        }

                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
            });
    }
}


