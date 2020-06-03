package com.findagig.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.findagig.activities.MapsActivity;
import com.findagig.R;

public class HomeFragment extends Fragment {

    private MapsActivity homeViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.activity_maps, container, false);

        return root;
    }
}