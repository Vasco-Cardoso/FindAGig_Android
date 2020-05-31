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
import com.findagig.ui.recyclercardview.ModelForMenu;
import com.findagig.ui.recyclercardview.MyAdapter;
import com.findagig.ui.recyclercardview.MyAdapterMenu;

import java.util.ArrayList;

public class MenuFragment extends Fragment {

    private static final String TAG = "MenuFragment";
    RecyclerView mRecyclerView;
    MyAdapterMenu myAdapter;

    String[] options = new String[]{"All gigs","History","Settings", "Map", "QRCode", "Logout" };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_menu, container, false);

        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView_MENU);
        mRecyclerView.setLayoutManager(new GridLayoutManager(getActivity(), 2));

        myAdapter = new MyAdapterMenu(this.getContext(), getMyList());
        mRecyclerView.setAdapter(myAdapter);

        return rootView;
    }

    private ArrayList<ModelForMenu> getMyList() {
        final ArrayList<ModelForMenu> models = new ArrayList<>();

        for(String opt : options) {
            ModelForMenu m = new ModelForMenu();
            m.setTitle(opt);

            m.setImg(R.drawable.bookmark);
            models.add(m);
        }

        return models;
    }
}
