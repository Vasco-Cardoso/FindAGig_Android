package com.findagig;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

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
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_gigs);

        progressDialog = new ProgressDialog(this);

        mRecyclerView = (RecyclerView) findViewById(R.id.RecyclerView_AllGigs);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 1));

        myAdapter = new MyAdapter(this, getMyList());
        mRecyclerView.setAdapter(myAdapter);

    }

    @Override
    protected void onResume() {
        super.onResume();

        getMyList();
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
                                    m.setDescription(document.getData().get("description").toString());
                                    m.setImg(chooseImage(document));
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
                                if (!document.getBoolean("taken") &&
                                          (document.getData().get("name").toString().toLowerCase().contains(search.toLowerCase())
                                        || document.getData().get("city").toString().toLowerCase().contains(search.toLowerCase()))) {
                                    Model m = new Model();
                                    m.setTitle(document.getData().get("name").toString());
                                    m.setDescription(document.getData().get("description").toString());
                                    m.setImg(chooseImage(document));
                                    models.add(m);

                                }
                            }
                            myAdapter.setModels(models);
                            mRecyclerView.setAdapter(myAdapter);
                            progressDialog.dismiss();

                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        return models;
    }


    public int chooseImage(QueryDocumentSnapshot document) {
        if(document.getData().get("type").toString().contains("CLEANING")) {
            return (R.drawable.baseline_cleaning_services_24);
        }
        else if(document.getData().get("type").toString().contains("OTHER")) {
            return (R.drawable.baseline_add_circle_outline_24);
        }
        else if(document.getData().get("type").toString().contains("ELETRONICS")) {
            return (R.drawable.baseline_flash_on_24);
        }
        else if(document.getData().get("type").toString().contains("HOUSE")) {
            return (R.drawable.baseline_house_24);
        }
        else if(document.getData().get("type").toString().contains("Withdraw")) {
            return (R.drawable.baseline_monetization_on_24);
        }
        else if(document.getData().get("type").toString().contains("SECURITY")) {
            return (R.drawable.baseline_security_24);
        }
        else {
            return (R.drawable.baseline_add_circle_outline_24);
        }
    }

    public void searchGig(View view) {
        progressDialog.show();

        EditText searchBar = findViewById(R.id.search_bar);
        getMyList(searchBar.getText().toString().trim());
    }
}

