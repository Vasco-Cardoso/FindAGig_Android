package com.findagig.ui.recyclercardview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.findagig.AllGigs;
import com.findagig.Description;
import com.findagig.LogInPage;
import com.findagig.MapsActivity;
import com.findagig.R;
import com.findagig.SettingsPage;
import com.findagig.ui.QRCode.QRCode;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyHolder> {
    private static final String TAG = "MyAdapter";
    Context c;
    ArrayList<Model> models;

    public MyAdapter(Context c, ArrayList<Model> models) {
        this.c = c;
        this.models = models;
    }

    public void setModels(ArrayList<Model> models) {
        this.models = models;
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);

        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolder holder, final int position) {
        holder.mTitle.setText(models.get(position).getTitle());
        holder.mdes.setText(models.get(position).getDescription());
        holder.mImage.setImageResource(models.get(position).getImg());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClickListener(View v, int pos) {
                String gTitle = models.get(position).getTitle();
                String gDesc = models.get(position).getDescription();

                if (gTitle.equals("All gigs"))
                {
                    Intent intent = new Intent(c, AllGigs.class);
                    c.startActivity(intent);
                }
                else if (gTitle.equals("Map"))
                {
                    Intent intent = new Intent(c, MapsActivity.class);
                    c.startActivity(intent);
                }
                else if (gTitle.equals("QRCode"))
                {
                    Intent intent = new Intent(c, QRCode.class);
                    c.startActivity(intent);
                }
                else if (gTitle.equals("History"))
                {
                    Intent intent = new Intent(c, QRCode.class);
                    c.startActivity(intent);
                }
                else if(gTitle.equals("Settings")) {
                    Intent intent = new Intent(c, SettingsPage.class);
                    c.startActivity(intent);
                }
                else if (gTitle.equals("Logout"))
                {
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    mAuth.signOut();
                    Intent intent = new Intent(c, LogInPage.class);
                    c.startActivity(intent);
                }
                else {
                    getIdFromName(gTitle);
                }


            }
        });
    }

    private void getIdFromName(final String gTitle) {
        final String[] id = new String[1];

        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection("gigs")
            .get()
            .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    for (QueryDocumentSnapshot document : task.getResult()) {
                        if(document.getData().get("name").toString().equals(gTitle)) {
                            id[0] = document.getId();

                            Intent intent = new Intent(c, Description.class);
                            intent.putExtra("id", id[0]);
                            c.startActivity(intent);

                            break;
                        }
                    }

                } else {
                    Log.d(TAG, "Error getting documents: ", task.getException());
                }
                }
            });



    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}
