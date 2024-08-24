package com.example.civiladvocacyapplication;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class PoliticianViewHolder extends RecyclerView.ViewHolder
{
    // Protected variables since adapter will require these views to bind
    protected ImageView imageViewEntryPolitician;
    protected TextView textViewEntryName, textViewEntryOffice;



    public PoliticianViewHolder(@NonNull View itemView)
    {
        super(itemView);
        imageViewEntryPolitician = itemView.findViewById(R.id.imageViewEntryPolitician);
        textViewEntryName = itemView.findViewById(R.id.textViewEntryName);
        textViewEntryOffice = itemView.findViewById(R.id.textViewEntryOffice);
    }
}

