package com.findagig.recyclercardview;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.ui.fragments.CityInfo;
import com.findagig.R;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyHolder> {

    Bundle dataBundle = new Bundle();

    Context c;
    ArrayList<Model> models;

    public MyAdapter(Context c, ArrayList<Model> models) {
        this.c = c;
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

//                dataBundle.putString("title", gTitle);
//                dataBundle.putString("desc", gDesc);
//                simpleFragment.setArguments(dataBundle);
//
//                AppCompatActivity activity = (AppCompatActivity) v.getContext();
//                activity.getSupportFragmentManager().beginTransaction().replace(R.id.cityListFragment, simpleFragment).addToBackStack(null).commit();
            }
        });
    }

    @Override
    public int getItemCount() {
        return models.size();
    }
}
