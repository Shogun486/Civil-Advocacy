package com.example.civiladvocacyapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Callback;
import java.util.List;

public class PoliticianAdapter extends RecyclerView.Adapter<PoliticianViewHolder>
{
    private final MainActivity mainActivity;
    private final List <Politician> alp;
    private Politician p;
    private String format = "";



    PoliticianAdapter(MainActivity mainActivity, List <Politician> alp)
    {
        this.mainActivity = mainActivity;
        this.alp = alp;
    }



    @NonNull
    @Override
    public PoliticianViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType)
    {
        // Recognize the entries to populate
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.politician_entry, parent, false);

        // onClickListener() can't detect activity if download is done in MainActivity
        // Therefore, download in a separate class (CivicAPI class)
        itemView.setOnClickListener(mainActivity);
        return new PoliticianViewHolder(itemView);
    }



    @Override
    public void onBindViewHolder(@NonNull PoliticianViewHolder holder, int position)
    {
        // Need to properly associate information to each politician
        p = alp.get(position);
        format = p.getName() + " (" + p.getParty() + ")";
        holder.textViewEntryOffice.setText(format);
        format = p.getOffice();
        holder.textViewEntryName.setText(format);

        // Load proper image
        if(!p.getImage().isEmpty())
        {
            Picasso.get().load(p.getImage()).into(holder.imageViewEntryPolitician, new Callback()
            {
                @Override
                public void onSuccess() {} // DO NOTHING, image loaded, so we're done
                @Override
                public void onError(Exception e)
                {
                    holder.imageViewEntryPolitician.setImageResource(R.drawable.brokenimage);
                }
            });
        }
        else
        {
            holder.imageViewEntryPolitician.setImageResource(R.drawable.missing);
        }
    }



    // Default function -- must include
    @Override
    public int getItemCount() { return alp.size(); }
}

