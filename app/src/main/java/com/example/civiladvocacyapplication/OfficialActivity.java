package com.example.civiladvocacyapplication;

import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

public class OfficialActivity extends AppCompatActivity
{
    private TextView textViewOfficialUserLocation, textViewOfficialName, textViewOfficialOffice,
            textViewOfficialParty, textViewOfficialAddressData , textViewOfficialPhoneData,
            textViewOfficialEmailData, textViewOfficialWebsiteData, textViewOfficialAddress,
            textViewOfficialPhone, textViewOfficialEmail, textViewOfficialWebsite;
    private ImageView imageViewOfficialPolitician, imageViewOfficialParty,
            imageViewFacebook, imageViewYoutube, imageViewTwitter;
    private ConstraintLayout constraintLayoutOfficial;
    private Intent intent;
    private Politician p;
    private String [] emailAddress;
    private String fetchLocationDisplay, party, address, number, format = "", facebook_user, youtube_user, twitter_user, channelURL;
    private final String FACEBOOK_APP = "fb://facewebmodal/f?href=", FACEBOOK_PACKAGE = "com.facebook.katana", FACEBOOK_WEB = "https://www.facebook.com/" ,
            YOUTUBE_WEB_OR_APP = "https://www.youtube.com/", YOUTUBE_PACKAGE = "com.google.android.youtube",
            TWITTER_APP = "twitter://user?screen_name=", TWITTER_WEB = "https://twitter.com/", TWITTER_PACKAGE = "com.twitter.android",
            DEMOCRATIC_PAGE = "https://democrats.org", REPUBLICAN_PAGE = "https://www.gop.com";
    private SpannableString ss;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_official);
        getSupportActionBar().setTitle("Civil Advocacy");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#600281")));
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.status_bar));

        // Intent from MainActivity to OfficialActivity only requires Politician-object
        intent = getIntent();
        fetchLocationDisplay = intent.getStringExtra("NORMALIZED INPUT");

        // Social media and other image-views
        constraintLayoutOfficial = findViewById(R.id.constraintLayoutOfficial);
        imageViewOfficialPolitician = findViewById(R.id.imageViewOfficialPolitician);
        imageViewOfficialParty = findViewById(R.id.imageViewOfficialParty);
        imageViewFacebook = findViewById(R.id.imageViewOfficialFacebook);
        imageViewYoutube = findViewById(R.id.imageViewOfficialYoutube);
        imageViewTwitter = findViewById(R.id.imageViewOfficialTwitter);

        // User's current location must be displayed
        textViewOfficialUserLocation = findViewById(R.id.textViewOfficialUserLocation);
        textViewOfficialUserLocation.setText(fetchLocationDisplay);

        // Key data about the politician
        textViewOfficialName = findViewById(R.id.textViewOfficialName);
        textViewOfficialOffice = findViewById(R.id.textViewOfficialOffice);
        textViewOfficialParty = findViewById(R.id.textViewOfficialParty);
        textViewOfficialAddressData = findViewById(R.id.textViewOfficialAddressData);
        textViewOfficialPhoneData = findViewById(R.id.textViewOfficialPhoneData);
        textViewOfficialEmailData = findViewById(R.id.textViewOfficialEmailData);
        textViewOfficialWebsiteData = findViewById(R.id.textViewOfficialWebsiteData);

        // Title-text-views (may need to be set invisible later for better appearance)
        textViewOfficialAddress = findViewById(R.id.textViewOfficialAddress);
        textViewOfficialPhone = findViewById(R.id.textViewOfficialPhone);
        textViewOfficialEmail = findViewById(R.id.textViewOfficialEmail);
        textViewOfficialWebsite = findViewById(R.id.textViewOfficialWebsite);

        // From intent, get politician object
        p = (Politician) intent.getSerializableExtra("POLITICIAN");
        textViewOfficialName.setText(p.getName());
        textViewOfficialOffice.setText(p.getOffice());
        textViewOfficialParty.setText("(" + p.getParty() + ")");

        // Get proper image to show (based on availability)
        if (!p.getImage().isEmpty())
        {
            Picasso.get().load(p.getImage()).into(imageViewOfficialPolitician, new Callback() {
                @Override
                public void onSuccess() {} // DO NOTHING, image loaded, so we're done

                @Override
                public void onError(Exception e)
                {
                    imageViewOfficialPolitician.setImageResource(R.drawable.brokenimage);
                }
            });
        } else { imageViewOfficialPolitician.setImageResource(R.drawable.missing); }

        // Set proper background color based on party (if in a party)
        party = p.getParty();
        if (party.equals("Democratic Party"))
        {
            imageViewOfficialParty.setImageDrawable((Drawable)getDrawable(R.drawable.dem_logo));
            constraintLayoutOfficial.setBackgroundColor(Color.BLACK);
        }
        else if (party.equals("Republican Party"))
        {
            imageViewOfficialParty.setImageDrawable((Drawable) getDrawable(R.drawable.rep_logo));
            constraintLayoutOfficial.setBackgroundColor(Color.BLACK);
        }
        else if (p.getParty().equals("Nonpartisan"))
        {
            imageViewOfficialParty.setVisibility(View.INVISIBLE);
            constraintLayoutOfficial.setBackgroundColor(Color.BLACK);
        }

        // Social media channels
        format = p.getFacebook();
        if (format.isEmpty())
            imageViewFacebook.setVisibility(View.INVISIBLE);

        format = p.getYoutube();
        if (format.isEmpty())
            imageViewYoutube.setVisibility(View.INVISIBLE);

        format = p.getTwitter();
        if (format.isEmpty())
            imageViewTwitter.setVisibility(View.INVISIBLE);

        // Relevant contact info
        format = p.getPhone();
        if (format.isEmpty())
        {
            textViewOfficialPhoneData.setVisibility(View.GONE);
            textViewOfficialPhone.setVisibility(View.GONE);
        }
        else
        {
            ss = new SpannableString(format);
            ss.setSpan(new UnderlineSpan(), 0, ss.length(), 0);
            textViewOfficialPhoneData.setText(ss);
        }

        format = p.getEmail();
        if (format.isEmpty())
        {
            textViewOfficialEmailData.setVisibility(View.GONE);
            textViewOfficialEmail.setVisibility(View.GONE);
        }
        else
        {
            ss = new SpannableString(format);
            ss.setSpan(new UnderlineSpan(), 0, ss.length(), 0);
            textViewOfficialEmailData.setText(ss);
        }

        format = p.getWebsite();
        if (format.isEmpty())
        {
            textViewOfficialWebsiteData.setVisibility(View.GONE);
            textViewOfficialWebsite.setVisibility(View.GONE);
        }
        else
        {
            ss = new SpannableString(format);
            ss.setSpan(new UnderlineSpan(), 0, ss.length(), 0);
            textViewOfficialWebsiteData.setText(ss);
        }

        format = p.getAddress();
        if (format.isEmpty())
        {
            textViewOfficialAddressData.setVisibility(View.GONE);
            textViewOfficialAddress.setVisibility(View.GONE);
        }
        else
        {
            ss = new SpannableString(format);
            ss.setSpan(new UnderlineSpan(), 0, ss.length(), 0);
            textViewOfficialAddressData.setText(ss);
        }
    }



    // An onClick() function for the image
    public void goToPhotoDetail(View view)
    {
        if (!p.getImage().isEmpty())
        {
            intent = new Intent(OfficialActivity.this, PhotoDetailActivity.class);
            intent.putExtra("POLITICIAN", p);
            intent.putExtra("NORMALIZED INPUT", textViewOfficialUserLocation.getText().toString());
            startActivity(intent);
        }
    }



    // An onClick() function for the party logo
    public void goToPartyPage(View view)
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



    // onClick() functions for proper social-media channels
    public void goToFacebook(View v)
    {
        facebook_user = p.getFacebook();
        try
        {
            // ImpliedIntents example
            channelURL = FACEBOOK_APP + facebook_user;
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(channelURL));
            intent.setPackage(FACEBOOK_PACKAGE);
            startActivity(intent);
        }
        catch (ActivityNotFoundException e)
        {
            channelURL = FACEBOOK_WEB + facebook_user;
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(channelURL)));
        }
    }



    public void goToYoutube(View v)
    {
        youtube_user = p.getYoutube();
        try
        {
            // Example within assignment description
            channelURL = YOUTUBE_WEB_OR_APP + youtube_user;
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(channelURL));
            intent.setPackage(YOUTUBE_PACKAGE);
            startActivity(intent);
        }
        catch (ActivityNotFoundException e)
        {
            startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(channelURL)));
        }
    }



    public void goToTwitter(View v)
    {
        twitter_user = p.getTwitter();
        try
        {
            // Example provided with assignment
            // Get the Twitter app if possible
            getPackageManager().getPackageInfo(TWITTER_PACKAGE, 0);
            channelURL = TWITTER_APP + twitter_user;
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(channelURL));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        }
        catch (Exception e)
        {
            // If no Twitter app, revert to browser
            channelURL = TWITTER_WEB + twitter_user;
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(channelURL));
        }
        startActivity(intent);
    }



    // An onClick() function for the underlined address
    public void goToAddress(View v)
    {
        if (!p.getAddress().isEmpty())
        {
            address = p.getAddress();
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse("geo:0,0?q=" + Uri.encode(address)));
            startActivity(intent);
        }
    }



    // An onClick() function for the underlined phone number
    public void goToCall(View v)
    {
        if (!p.getPhone().isEmpty())
        {
            // ImpliedIntents example
            number = p.getPhone();
            intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + number));
            startActivity(intent);
        }
    }



    // An onClick() function for the underlined email
    public void goToEmail(View v)
    {
        if (!p.getEmail().isEmpty())
        {
            // ImpliedIntents example
            emailAddress = new String[] { p.getAddress() };
            intent = new Intent(Intent.ACTION_SENDTO, Uri.parse("mailto:"));
            intent.putExtra(Intent.EXTRA_EMAIL, emailAddress);
            intent.putExtra(Intent.EXTRA_SUBJECT, "This comes from EXTRA_SUBJECT");
            intent.putExtra(Intent.EXTRA_TEXT, "Email text body from EXTRA_TEXT...");
            startActivity(intent);
        }
    }



    // An onClick() function for the underlined website
    public void goToWebsite(View v)
    {
        if (!p.getWebsite().isEmpty())
        {
            intent = new Intent(Intent.ACTION_VIEW, Uri.parse(p.getWebsite()));
            startActivity(intent);
        }
    }
}
