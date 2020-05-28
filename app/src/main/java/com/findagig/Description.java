package com.findagig;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.findagig.ui.recyclercardview.Model;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Description extends AppCompatActivity {
    private static final String TAG = "Description";

    TextView gigName;
    TextView gigEmployer;
    TextView gigDesc;
    TextView gigReward;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_description);

        gigName = findViewById(R.id.gig_name);
        gigEmployer = findViewById(R.id.gig_employer);
        gigDesc = findViewById(R.id.gig_desc);
        gigReward = findViewById(R.id.gig_reward);

        Bundle bundle = getIntent().getExtras();

        if (bundle != null) {
            Log.d(TAG, "=> Vieram valores no bundle");

            getInfo(bundle.getString("id"));
        }
        else {
            gigName.setText("Error");
            gigEmployer.setText("Error");
        }

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


