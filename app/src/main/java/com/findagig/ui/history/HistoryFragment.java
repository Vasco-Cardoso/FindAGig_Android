package com.findagig.ui.history;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.findagig.R;
import com.findagig.ui.recyclercardview.Model;
import com.findagig.ui.recyclercardview.MyAdapter;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {
    private static final String TAG = "HistoryFragment";
    RecyclerView mRecyclerView;
    MyAdapter myAdapter;

    RecyclerView mRecyclerView2;
    MyAdapter myAdapter2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.RecyclerView_HISTORY);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));

        myAdapter = new MyAdapter(this.getContext(), getMyList());
        mRecyclerView.setAdapter(myAdapter);

        mRecyclerView2 = (RecyclerView) rootView.findViewById(R.id.RecyclerView_HISTORY_completed);
        mRecyclerView2.setLayoutManager(new GridLayoutManager(getActivity(), 1));

        myAdapter2 = new MyAdapter(this.getContext(), getMyList2());
        mRecyclerView2.setAdapter(myAdapter2);

        return rootView;
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
                        if (document.getBoolean("taken") && !document.getBoolean("completed")) {
                            if (document.getData().get("employee").toString().equals(user_uid)) {
                                Model m = new Model();
                                m.setTitle(document.getData().get("name").toString());
                                m.setDescription(document.getData().get("description").toString());
                                m.setImg(R.drawable.bookmark);
                                models.add(m);
                            }

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

    private ArrayList<Model> getMyList2() {
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
                                if (document.getBoolean("taken") && document.getBoolean("completed")) {
                                    if (document.getData().get("employee").toString().equals(user_uid)) {
                                        Model m = new Model();
                                        m.setTitle(document.getData().get("name").toString());
                                        m.setDescription(document.getData().get("description").toString());
                                        m.setImg(R.drawable.bookmark);
                                        models.add(m);
                                    }

                                }
                            }
                            myAdapter2.setModels(models);
                            mRecyclerView2.setAdapter(myAdapter2);
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });

        return models;
    }
}
