package com.example.civiladvocacyapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity implements View.OnClickListener
{
    private FusedLocationProviderClient mFusedLocationClient;
    private static final int LOCATION_REQUEST = 111;
    private static TextView textViewMainUserLocation;
    private TextView textViewNoInternetConnection, textViewNoData;
    private EditText enterLocation;
    protected RecyclerView recyclerView;
    private Intent intent;
    protected static final ArrayList <Politician> alp = new ArrayList<>();
    private PoliticianAdapter pa;
    private Politician p;
    protected static String queriedLocation;
    private String noConnection = "No Data For Location";
    private boolean queried = false;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().setTitle("Civil Advocacy");
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#600281")));
        getWindow().setStatusBarColor(ContextCompat.getColor(this, R.color.status_bar));

        // Connection notification
        textViewMainUserLocation = findViewById(R.id.textViewMainUserLocation);
        textViewNoInternetConnection = findViewById(R.id.textViewNoNetworkConnection);
        textViewNoData = findViewById(R.id.textViewNoData);

        // Set up only other UI: the recycler view
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Based on network connection, make proper UI changes
        if (hasNetworkConnection())
        {
            recyclerView.setVisibility(View.VISIBLE);
        }
        else
        {
            getSupportActionBar().setTitle("Know Your Government");
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#D2D1D3")));
            recyclerView.setVisibility(View.INVISIBLE);
            textViewMainUserLocation.setText(noConnection);
            textViewNoInternetConnection.setVisibility(View.VISIBLE);
            textViewNoData.setVisibility(View.VISIBLE);
        }

        // Must indicate adapter for recycler view
        pa = new PoliticianAdapter(this, alp);
        recyclerView.setAdapter(pa);

        // Same as FusedLocation() example
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // On screen rotation, set views for current location
        if (savedInstanceState != null)
        {
            if (!hasNetworkConnection() && queried == true)
            {
                getSupportActionBar().setTitle("Know Your Government");
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#D2D1D3")));
                textViewMainUserLocation.setVisibility(View.VISIBLE);
                textViewMainUserLocation.setText(noConnection);
                textViewNoInternetConnection.setVisibility(View.INVISIBLE);
                textViewNoData.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
            }
            else
            {
                getSupportActionBar().setTitle("Civil Advocacy");
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#600281")));
                textViewMainUserLocation.setText(savedInstanceState.getString("NORMALIZED INPUT"));
                queriedLocation = savedInstanceState.getString("QUERIED LOCATION");
                textViewMainUserLocation.setVisibility(View.VISIBLE);
                textViewNoInternetConnection.setVisibility(View.INVISIBLE);
                textViewNoData.setVisibility(View.INVISIBLE);
                recyclerView.setVisibility(View.VISIBLE);
            }
        }
        else
        {
            // This is only upon startup, where the app gets the user's location: proper displays are shown
            if (hasNetworkConnection())
            {
                determineLocation();
            }
            else
            {
                getSupportActionBar().setTitle("Know Your Government");
                getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#D2D1D3")));
                textViewMainUserLocation.setText(noConnection);
                textViewNoInternetConnection.setVisibility(View.VISIBLE);
                textViewNoData.setVisibility(View.VISIBLE);
                recyclerView.setVisibility(View.INVISIBLE);
            }
        }
    }



    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        switch (item.getItemId())
        {
            case R.id.menuItemAbout:
                intent = new Intent(this, AboutActivity.class);
                startActivity(intent);
            case R.id.menuItemSearch:
                queried = true;
                AlertDialog.Builder adb = new AlertDialog.Builder(this);
                AlertDialog ad;
                enterLocation = new EditText(this);

                // Similar to WeatherApp location prompt
                enterLocation.setGravity(1);
                enterLocation.setInputType(1);
                adb.setView(enterLocation);
                adb.setPositiveButton("OK", new DialogInterface.OnClickListener()
                {
                    @Override
                    public void onClick(DialogInterface dialog, int id)
                    {
                        queriedLocation = enterLocation.getText().toString();

                        // Download API query in different class: prevents activity-recognition errors
                        performDownload(queriedLocation);
                    }
                });
                adb.setNegativeButton("CANCEL", new DialogInterface.OnClickListener()
                {
                    public void onClick(DialogInterface dialog, int id)
                    {} // DO NOTHING, STAY
                });
                adb.setTitle("Enter Address");
                ad = adb.create();
                ad.show();
        }
        return super.onOptionsItemSelected(item);
    }



    // This is where the call to the API will eventually be made (in separate class CivicAPI)
    private void performDownload(String queriedLocation)
    {
        if (hasNetworkConnection())
        {
            getSupportActionBar().setTitle("Civil Advocacy");
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#600281")));
            textViewMainUserLocation.setText(queriedLocation);
            textViewNoData.setVisibility(View.INVISIBLE);
            textViewNoInternetConnection.setVisibility(View.INVISIBLE);
            recyclerView.setVisibility(View.VISIBLE);

            // Make the official API call
            CivicAPI.callAPI(this, queriedLocation);
        }
        else
        {
            getSupportActionBar().setTitle("Know Your Government");
            getSupportActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#D2D1D3")));
            textViewMainUserLocation.setText(noConnection);
            textViewNoData.setVisibility(View.VISIBLE);
            textViewNoInternetConnection.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.INVISIBLE);
        }
    }



    // From FusedLocation() example
    private void determineLocation()
    {
        // Check perm - if not then start the  request and return
        if (ContextCompat.checkSelfPermission(this,
                android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION}, LOCATION_REQUEST);
            return;
        }
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, location ->
                {
                    // Got last known location. In some situations this can be null.
                    if (location != null)
                    {
                        queriedLocation = getPlace(location);
                        textViewMainUserLocation.setText(queriedLocation);
                        performDownload(queriedLocation);
                    }
                })
                .addOnFailureListener(this, e ->
                        Toast.makeText(MainActivity.this,
                                e.getMessage(), Toast.LENGTH_LONG).show());
    }



    // From FusedLocation() example
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults)
    {

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_REQUEST)
        {
            if (permissions[0].equals(Manifest.permission.ACCESS_FINE_LOCATION))
            {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                {
                    determineLocation();
                } else { textViewMainUserLocation.setText("PLEASE ENABLE LOCATION PERMISSIONS"); }
            }
        }
    }



    // From FusedLocation() example
    private String getPlace(Location loc)
    {
        StringBuilder sb = new StringBuilder();
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses;
        String userLocation;
        try
        {
            // Default way of getting location
            addresses = geocoder.getFromLocation(loc.getLatitude(), loc.getLongitude(), 1);

            // Fetch whole address instead of fetching separate address-components
            userLocation = addresses.get(0).getAddressLine(0);
            sb.append(String.format(Locale.getDefault(), "%s", userLocation));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return sb.toString();
    }



    // onClick() listener set in adapter for recycler view
    @Override
    public void onClick(View view)
    {
        p = alp.get(recyclerView.getChildLayoutPosition(view));
        intent = new Intent(this, OfficialActivity.class);
        intent.putExtra("POLITICIAN", p);
        intent.putExtra("NORMALIZED INPUT", textViewMainUserLocation.getText().toString());
        startActivity(intent);
    }



    // For showing user location / queried location in MainActivity
    public static void setNormalizedInput(String normalized)
    {
        textViewMainUserLocation.setText(normalized);
    }



    // Code for connectivity purposes
    private boolean hasNetworkConnection()
    {
        ConnectivityManager connectivityManager = getSystemService(ConnectivityManager.class);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return (networkInfo != null && networkInfo.isConnectedOrConnecting());
    }



    // Whenever screen is rotated, ensure data persists
    @Override
    public void onSaveInstanceState(Bundle outState)
    {
        outState.putString("NORMALIZED INPUT", textViewMainUserLocation.getText().toString());
        outState.putString("QUERIED LOCATION", queriedLocation);

        // Call super last
        super.onSaveInstanceState(outState);
    }
}