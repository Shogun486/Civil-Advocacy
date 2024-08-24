package com.example.civiladvocacyapplication;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class PhotoDetailActivity extends AppCompatActivity
{
    private TextView textViewPhotoDetailUserLocation, textViewPhotoDetailName, textViewPhotoDetailOffice;
    private ImageView imageViewPhotoDetailParty, imageViewPhotoDetailPolitician;
    private ConstraintLayout constraintLayoutPhotoDetail;
    private Intent intent;
    private Politician p;
    private String fetchLocationDisplay, party;
    private final String DEMOCRATIC_PAGE = "https://democrats.org", REPUBLICAN_PAGE = "https://www.gop.com";



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_photo_detail);
        getSupportActionBar().setTitle("Civil Advocacy");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#600281")));
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.status_bar));

        // Intent from OfficialActivity to PhotoDetail only requires Politician-object
        intent = getIntent();
        fetchLocationDisplay = intent.getStringExtra("NORMALIZED INPUT");

        // Set up basic views
        textViewPhotoDetailUserLocation = findViewById(R.id.textViewPhotoDetailUserLocation);
        textViewPhotoDetailUserLocation.setText(fetchLocationDisplay);
        textViewPhotoDetailName = findViewById(R.id.textViewPhotoDetailName);
        textViewPhotoDetailOffice = findViewById(R.id.textViewPhotoDetailOffice);
        imageViewPhotoDetailParty = findViewById(R.id.imageViewPhotoDetailParty);
        imageViewPhotoDetailPolitician = findViewById(R.id.imageViewPhotoDetailPolitician);
        constraintLayoutPhotoDetail = findViewById(R.id.constraintLayoutPhotoDetail);

        // Similar to OfficialActivity code: get politician object from calling activity
        p = (Politician) intent.getSerializableExtra("POLITICIAN");
        textViewPhotoDetailName.setText(p.getName());
        textViewPhotoDetailOffice.setText(p.getOffice());

        // Change colors/party-logo accordingly
        party = p.getParty();
        if (party.equals("Democratic Party"))
        {
            imageViewPhotoDetailParty.setImageDrawable((Drawable)getDrawable(R.drawable.dem_logo));
            constraintLayoutPhotoDetail.setBackgroundColor(Color.BLUE);
        }
        else if (party.equals("Republican Party"))
        {
            imageViewPhotoDetailParty.setImageDrawable((Drawable) getDrawable(R.drawable.rep_logo));
            constraintLayoutPhotoDetail.setBackgroundColor(Color.RED);
        }
        else if (p.getParty().equals("Nonpartisan"))
        {
            imageViewPhotoDetailParty.setVisibility(View.INVISIBLE);
            constraintLayoutPhotoDetail.setBackgroundColor(Color.BLACK);
        }

        // Get proper image to show (based on availability)
        // Glide requires activity as a parameter, whereas Picasso will simply require image-URL
        if (!p.getImage().isEmpty())
        {
            Picasso.get().load(p.getImage()).into(imageViewPhotoDetailPolitician, new Callback()
            {
                @Override
                public void onSuccess() {} // DO NOTHING, image loaded, so we're done

                @Override
                public void onError(Exception e)
                {
                    imageViewPhotoDetailPolitician.setImageResource(R.drawable.brokenimage);
                }
            });
        } else { imageViewPhotoDetailPolitician.setImageResource(R.drawable.missing); }
    }



    // An onClick() function for the party-logo
    public void goToPartyPage(View v)
    {
        if (party.equals("Democratic Party"))
        {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(DEMOCRATIC_PAGE));
            startActivity(intent);
        }
        else if (party.equals("Republican Party"))
        {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(REPUBLICAN_PAGE));
            startActivity(intent);
        }
    }
}
