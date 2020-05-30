package com.findagig;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;

import com.findagig.ui.recyclercardview.Model;
import com.findagig.ui.recyclercardview.MyAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class AllGigs extends AppCompatActivity {
    private static final String TAG = "allGigs";
    RecyclerView mRecyclerView;
    MyAdapter myAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_gigs);

        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView_AllGigs);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        myAdapter = new MyAdapter(this, getMyList());
        mRecyclerView.setAdapter(myAdapter);

    }

    private ArrayList<Model> getMyList() {
        final ArrayList<Model> models = new ArrayList<>();

        final FirebaseAuth mAuth = FirebaseAuth.getInstance();;
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        final String user_uid = mAuth.getCurrentUser().getUid();

        db.collection("gigs")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (!document.getBoolean("taken")) {
                                    Log.d(TAG, document.getId() + " => " + document.getData().get("city") + ", " + document.getData().get("description") + ", " + document.getData().get("employer") + ", " + document.getData().get("employee"));

                                    Model m = new Model();
                                    m.setTitle(document.getData().get("name").toString());
                                    m.setTitle(document.getId().toString());
                                    m.setDescription(document.getData().get("description").toString());
                                    m.setImg(R.drawable.bookmark);
                                    models.add(m);

                                }
                            }
                            myAdapter.setModels(models);
                            mRecyclerView.setAdapter(myAdapter);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        return models;
    }
}

