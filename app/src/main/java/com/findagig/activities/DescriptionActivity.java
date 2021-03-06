package com.findagig.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.findagig.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class DescriptionActivity extends AppCompatActivity {
    private static final String TAG = "Description";
    private String documentID = null;
    private int wallet = 0;
    private int valueOfGig = 0;
    String lat_gig, long_gig = "";

    TextView gigName;
    TextView gigEmployer;
    TextView gigDesc;
    TextView gigReward;
    TextView gigContact;

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
        gigContact = findViewById(R.id.gig_contact);

        // buttons assigned to the respective ID
        seeMapButton = findViewById(R.id.btn_map);
        applyButton = findViewById(R.id.btn_apply);

        // Get the ID value so we can gather the document from Firestore
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            documentID = bundle.getString("id");
            Log.d(TAG, "=> Id => " + documentID);

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

                Intent intent = new Intent(getApplicationContext(), MapsActivity.class);
                intent.putExtra("type", "gig_location");
                intent.putExtra("lat", lat_gig);
                intent.putExtra("long", long_gig);
                startActivity(intent);

            }
        });

        applyButton.setOnClickListener(new View.OnClickListener() {
            FirebaseFirestore db = FirebaseFirestore.getInstance();
            FirebaseAuth mAuth = FirebaseAuth.getInstance();

            public void onClick(View v) {
                String buttonText = applyButton.getText().toString();

                if (buttonText.equals("Complete gig!")) {

                    final String userUID = mAuth.getCurrentUser().getUid();

                    db.collection("users")
                        .get()
                        .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if (document.getId().equals(userUID)) {
                                        wallet = Integer.parseInt(document.getData().get("wallet").toString());
                                        Log.d(TAG, "Valor da wallet é de : " + wallet);

                                        updateWallet(valueOfGig, wallet, userUID);
                                        break;
                                    }
                                }
                            }
                            else {
                                Log.d(TAG, "Error getting documents: ", task.getException());
                            }
                        }
                    });


                    db.collection("gigs").document(documentID)
                            .update("completed", true)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully written!");

                                    Toast.makeText(DescriptionActivity.this, "You successfully applied to the job.",
                                            Toast.LENGTH_SHORT).show();

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error writing document", e);
                                    Toast.makeText(DescriptionActivity.this, "Error writing document.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });
                }
                else {

                    db.collection("gigs").document(documentID)
                            .update("taken", true)
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully written!");

                                    Toast.makeText(DescriptionActivity.this, "You successfully applied to the job.",
                                            Toast.LENGTH_SHORT).show();

                                }
                            })
                            .addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    Log.w(TAG, "Error writing document", e);
                                    Toast.makeText(DescriptionActivity.this, "Error writing document.",
                                            Toast.LENGTH_SHORT).show();
                                }
                            });


                    db.collection("gigs").document(documentID)
                            .update("employee", mAuth.getCurrentUser().getUid().toString())
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Log.d(TAG, "DocumentSnapshot successfully written!");
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
                        if (document.getId().equals(id)) {
                            Log.d(TAG, document.getId() + " => " + document.getData().get("city") + ", " + document.getData().get("description") + ", " + document.getData().get("employer"));

                            GeoPoint local = (GeoPoint) document.getData().get("local");
                            lat_gig = String.valueOf(local.getLatitude());
                            long_gig = String.valueOf(local.getLongitude());

                            valueOfGig = Integer.parseInt(document.getData().get("reward").toString());
                            gigName.setText(document.getData().get("name").toString());
                            gigEmployer.setText(document.getData().get("employer").toString());
                            gigDesc.setText(document.getData().get("description").toString());
                            gigContact.setText(document.getData().get("contact").toString());
                            gigReward.setText(document.getData().get("reward").toString() + " credits.");

                            if(document.getBoolean("taken")) {
                                if (document.getBoolean("completed")) {
                                    applyButton.setVisibility(View.GONE);
                                }
                                else
                                {
                                    applyButton.setText("Complete gig!");
                                    applyButton.setBackgroundColor(0xFF00FF00);
                                }

                            }
                            break;
                        }
                    }

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
                }
            });
    }

    public void updateWallet(int gigValue, int wallet, String userUID) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("users").document(userUID)
                .update("wallet", wallet + gigValue)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "You successfully updated your wallet!");

                        Toast.makeText(DescriptionActivity.this, "You successfully updated your wallet.",
                                Toast.LENGTH_SHORT).show();

                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w(TAG, "Error writing document", e);
                        Toast.makeText(DescriptionActivity.this, "Error writing document [wallet].",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
}


