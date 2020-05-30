package com.findagig.ui.recyclercardview;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.findagig.R;

public class MyHolderMenu extends RecyclerView.ViewHolder implements View.OnClickListener {

    ImageView mImage;
    TextView mTitle;
    ItemClickListener itemClickListener;


    public MyHolderMenu(@NonNull View itemView) {
        super(itemView);

        this.mImage = itemView.findViewById(R.id.imageIv);
        this.mTitle = itemView.findViewById(R.id.titleIv);

        itemView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        this.itemClickListener.onItemClickListener(v, getLayoutPosition());
    }
    public void setItemClickListener(ItemClickListener ic) {
        this.itemClickListener = ic;
    }

}
