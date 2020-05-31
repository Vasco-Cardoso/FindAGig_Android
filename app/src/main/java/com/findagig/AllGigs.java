package com.findagig;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.findagig.ui.recyclercardview.Model;
import com.findagig.ui.recyclercardview.MyAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.HashMap;

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

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("gigs")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (!document.getBoolean("taken")) {
                                    Model m = new Model();
                                    m.setTitle(document.getData().get("name").toString());
//                                    m.setTitle(document.getId().toString());
                                    m.setDescription(document.getData().get("description").toString());

                                    if(document.getData().get("type").toString().contains("CLEANING")) {
                                        m.setImg(R.drawable.baseline_cleaning_services_24);
                                    }
                                    else if(document.getData().get("type").toString().contains("OTHER")) {
                                        m.setImg(R.drawable.baseline_add_circle_outline_24);
                                    }
                                    else if(document.getData().get("type").toString().contains("ELETRONICS")) {
                                        m.setImg(R.drawable.baseline_flash_on_24);
                                    }
                                    else if(document.getData().get("type").toString().contains("HOUSE")) {
                                        m.setImg(R.drawable.baseline_house_24);
                                    }
                                    else if(document.getData().get("type").toString().contains("SECURITY")) {
                                        m.setImg(R.drawable.baseline_security_24);
                                    }
                                    else {
                                        m.setImg(R.drawable.baseline_add_circle_outline_24);
                                    }


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

    private ArrayList<Model> getMyList(final String search) {
        final ArrayList<Model> models = new ArrayList<>();

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("gigs")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                if (!document.getBoolean("taken") && document.getData().get("name").toString().toLowerCase().contains(search.toLowerCase())) {
                                    Model m = new Model();
                                    m.setTitle(document.getData().get("name").toString());
                                    m.setDescription(document.getData().get("description").toString());

                                    if(document.getData().get("type").toString().contains("CLEANING")) {
                                        m.setImg(R.drawable.baseline_cleaning_services_24);
                                    }
                                    else if(document.getData().get("type").toString().contains("OTHER")) {
                                        m.setImg(R.drawable.baseline_add_circle_outline_24);
                                    }
                                    else if(document.getData().get("type").toString().contains("ELETRONICS")) {
                                        m.setImg(R.drawable.baseline_flash_on_24);
                                    }
                                    else if(document.getData().get("type").toString().contains("HOUSE")) {
                                        m.setImg(R.drawable.baseline_house_24);
                                    }
                                    else if(document.getData().get("type").toString().contains("SECURITY")) {
                                        m.setImg(R.drawable.baseline_security_24);
                                    }
                                    else {
                                        m.setImg(R.drawable.baseline_add_circle_outline_24);
                                    }
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

    public void searchGig(View view) {
        EditText searchBar = findViewById(R.id.search_bar);
        getMyList(searchBar.getText().toString().trim());
    }
}

