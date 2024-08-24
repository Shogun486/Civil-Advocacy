package com.example.civiladvocacyapplication;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.UnderlineSpan;
import android.view.View;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

public class AboutActivity extends AppCompatActivity
{
    private TextView textViewAboutClickable;
    private Intent intent;
    private SpannableString ss;
    private final String API_URL = "https://developers.google.com/civic-information/";



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        // Set proper app colors
        getSupportActionBar().setTitle("Civil Advocacy");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#600281")));
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.status_bar));

        // Underline the text to lead to API documentation
        textViewAboutClickable = findViewById(R.id.textViewAboutClickable);
        ss = new SpannableString("Google Civic Information API");
        ss.setSpan(new UnderlineSpan(), 0, ss.length(), 0);
        textViewAboutClickable.setText(ss);
    }



    // Directs user to Civic API documentation
    public void goToCivicAPI(View v)
    {
        intent = new Intent(Intent.ACTION_VIEW, Uri.parse(API_URL));
        startActivity(intent);
    }
}
