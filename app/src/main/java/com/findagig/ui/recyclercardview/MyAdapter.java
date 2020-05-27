package com.findagig.ui.recyclercardview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.findagig.Description;
import com.findagig.MapsActivity;
import com.findagig.R;
import com.google.common.collect.Maps;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyHolder> {

    MapsActivity simpleFragment = MapsActivity.newInstance();
    Bundle dataBundle = new Bundle();

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

                dataBundle.putString("title", gTitle);
                dataBundle.putString("desc", gDesc);

                Intent intent = new Intent(c, Description.class);
                intent.putExtra("id", gTitle);
                c.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}
