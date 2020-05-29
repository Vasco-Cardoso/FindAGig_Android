package com.findagig.ui.menu;

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

public class MenuFragment extends Fragment {

    private static final String TAG = "MenuFragment";
    RecyclerView mRecyclerView;
    MyAdapter myAdapter;

    GridLayoutManager grid = new GridLayoutManager(getActivity(), 1);
    String[] options = new String[]{"Settings", "QRCode", "Map", "History"};

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_menu, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView_MENU);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 1));

        myAdapter = new MyAdapter(this.getContext(), getMyList());
        mRecyclerView.setAdapter(myAdapter);

        return rootView;
    }

    private ArrayList<Model> getMyList() {
        final ArrayList<Model> models = new ArrayList<>();

        for(String opt : options){

            Model m = new Model();
            m.setTitle(opt);
            m.setDescription("WIWI");
            m.setImg(R.drawable.bookmark);
            models.add(m);
        }

        return models;
    }
}
