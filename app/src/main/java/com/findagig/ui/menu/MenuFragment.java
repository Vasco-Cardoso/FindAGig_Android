package com.findagig.ui.menu;

import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MenuFragment extends Fragment {

    private static final String TAG = "MenuFragment";
    RecyclerView mRecyclerView;
    MyAdapterMenu myAdapter;

    // User variables to be set
    private String wallet;
    private String imagePath;
    private String name;
    private String userUID;

    // Firebase variables
    private FirebaseAuth mAuth;

    // Elements
    TextView wallet_value;
    TextView name_value;

    String[] options = new String[]{"All gigs", "Map", "QRCode", "History","Settings", "Logout" };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_menu, container, false);

        // Initializing Firebase variables
        mAuth = FirebaseAuth.getInstance();
        userUID = mAuth.getCurrentUser().getUid();

        getUserValues();

        // Finding values
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerView_MENU);
        wallet_value = rootView.findViewById(R.id.wallet_value);
        name_value = rootView.findViewById(R.id.name_value);

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

            if (opt.equals("All gigs")) {
                m.setImg(R.drawable.baseline_work_24);
            }
            else if (opt.equals("History")) {
                m.setImg(R.drawable.baseline_history_24);
            }
            else if (opt.equals("Settings")) {
                m.setImg(R.drawable.baseline_settings_24);
            }
            else if (opt.equals("Map")) {
                m.setImg(R.drawable.baseline_map_24);
            }
            else if (opt.equals("QRCode")) {
                m.setImg(R.drawable.baseline_qr_code_24);
            }
            else if (opt.equals("Logout")) {
                m.setImg(R.drawable.baseline_exit_to_app_24);
            }

            models.add(m);
        }

        return models;
    }

    private void getUserValues() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("users")
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            if (document.getId().equals(userUID)) {
                                name = document.getData().get("name").toString();
                                name = document.getData().get("name").toString();
                                imagePath = document.getData().get("image").toString();
                                wallet = document.getData().get("wallet").toString();


                                wallet_value.setText("You currently got " + wallet + " credits!");
                                name_value.setText("Welcome back " + name + ", good to see you here!");
                                name_value.setTypeface(null, Typeface.BOLD);
//                            try {
//                                getImage(userUID);
//                            } catch (IOException e) {
//                                e.printStackTrace();
//                            }
                            }
                        }
                    } else {
                        Log.d(TAG, "Error getting documents: ", task.getException());
                    }
                }
            });
    }

}
