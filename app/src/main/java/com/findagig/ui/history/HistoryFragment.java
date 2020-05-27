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
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {
    private static final String TAG = "HistoryFragment";
    RecyclerView mRecyclerView;
    MyAdapter myAdapter;

    GridLayoutManager grid = new GridLayoutManager(getActivity(), 1);
    String[] cities = new String[]{"Aveiro", "Braga", "Lisboa", "Porto", "Faro", "Vila Real", "Viana do Castelo"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_history, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.RecyclerView_HISTORY);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));


        myAdapter = new MyAdapter(this.getContext(), getMyList());
        mRecyclerView.setAdapter(myAdapter);

        return rootView;
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
                                //Log.d(TAG, document.getId() + " => " + document.getData().get("city") + ", " + document.getData().get("description") + ", " + document.getData().get("employer"));
                                Model m = new Model();
                                m.setTitle(document.getData().get("name").toString());
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
