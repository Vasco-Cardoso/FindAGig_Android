package com.findagig.ui.recyclercardview;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.findagig.activities.AllGigsActivity;
import com.findagig.activities.DescriptionActivity;
import com.findagig.activities.LogInPageActivity;
import com.findagig.activities.MapsActivity;
import com.findagig.R;
import com.findagig.activities.SettingsPageActivity;
import com.findagig.activities.WithdrawCreditsActivity;
import com.findagig.ui.QRCode.QRCode;
import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MyAdapterMenu extends RecyclerView.Adapter<MyHolderMenu> {
    Context c;
    ArrayList<ModelForMenu> models;

    public MyAdapterMenu(Context c, ArrayList<ModelForMenu> models) {
        this.c = c;
        this.models = models;
    }

    public void setModels(ArrayList<ModelForMenu> models) {
        this.models = models;
    }

    @NonNull
    @Override
    public MyHolderMenu onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card2, parent, false);

        return new MyHolderMenu(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final MyHolderMenu holder, final int position) {
        holder.mTitle.setText(models.get(position).getTitle());
        holder.mImage.setImageResource(models.get(position).getImg());

        holder.setItemClickListener(new ItemClickListener() {
            @Override
            public void onItemClickListener(View v, int pos) {
                String gTitle = models.get(position).getTitle();

                if(gTitle.equals("Settings")) {
                    Intent intent = new Intent(c, SettingsPageActivity.class);
                    c.startActivity(intent);
                }
                else if (gTitle.equals("QRCode"))
                {
                    Intent intent = new Intent(c, QRCode.class);
                    c.startActivity(intent);
                }
                else if (gTitle.equals("Logout"))
                {
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    mAuth.signOut();

                    Intent intent = new Intent(c, LogInPageActivity.class);
                    c.startActivity(intent);
                }
                else if (gTitle.equals("Map"))
                {
                    Intent intent = new Intent(c, MapsActivity.class);
                    intent.putExtra("type", "all_gigs_map");
                    c.startActivity(intent);
                }
                else if (gTitle.equals("All gigs"))
                {
                    Intent intent = new Intent(c, AllGigsActivity.class);
                    c.startActivity(intent);
                }
                else if(gTitle.equals("Withdraw")) {
                    Intent intent = new Intent(c, WithdrawCreditsActivity.class);
                    c.startActivity(intent);
                }
                else {
                    Intent intent = new Intent(c, DescriptionActivity.class);
                    intent.putExtra("id", gTitle);
                    c.startActivity(intent);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}
