package com.findagig.recyclercardview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class NewAdapter extends RecyclerView.Adapter<NewHolder> {

    Context c;
    ArrayList<Model> models;

    public NewAdapter(Context c, ArrayList<Model> models) {
        this.c = c;
        this.models = models;
    }

    public void setModels(ArrayList<Model> models) {
        this.models = models;
    }

    @NonNull
    @Override
    public NewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card, parent, false);

        return new NewHolder(view);
    }


    @Override
    public void onBindViewHolder(@NonNull final NewHolder holder, final int position) {
        holder.mTitle.setText(models.get(position).getTitle());
        holder.mdes.setText(models.get(position).getDescription());
        holder.mImage.setImageResource(models.get(position).getImg());
    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}
