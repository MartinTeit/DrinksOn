package com.example.drinkson;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.drinkson.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

public class locations extends AppCompatActivity {

    private TextView latitude;
    private TextView longitude;
    private TextView altitude;
    private TextView accuracy;
    private TextView speed;
    private TextView sensorType;
    private TextView updatesOnOff;
    private ToggleButton switchGpsBalanced;
    private ToggleButton locationOnOff;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int MY_PERMISSION_REQUEST_FINE_LOCATION = 101;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);

        //initialuse View
        latitude = (TextView) findViewById(R.id.tvLatitude);
        longitude = (TextView) findViewById(R.id.tvLongitude);
        altitude = (TextView) findViewById(R.id.tvAltitude);
        accuracy = (TextView) findViewById(R.id.tvAccuracy);
        speed = (TextView) findViewById(R.id.tvSpeed);
        sensorType = (TextView) findViewById(R.id.tvSensor);
        updatesOnOff = (TextView) findViewById(R.id.tvUpdates);
        switchGpsBalanced = (ToggleButton) findViewById(R.id.tbGps_Balanced);
        locationOnOff = (ToggleButton) findViewById(R.id.tvLocationOnOff);

        switchGpsBalanced.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (switchGpsBalanced.isChecked()) {
                    //using GPS only
                    sensorType.setText("GPS");
                } else {
                    //using balanced power accuracy
                    sensorType.setText("Cell Tower and WiFi");
                }

            }
        });

        locationOnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (locationOnOff.isChecked()) {
                    //location updates on
                    updatesOnOff.setText("On");
                } else {
                    //location updates off
                    updatesOnOff.setText("Off");
                }

            }
        });

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        latitude.setText(String.valueOf(location.getLatitude()));
                        longitude.setText(String.valueOf(location.getLongitude()));
                        accuracy.setText(String.valueOf(location.getAccuracy()));
                        if (location.hasAltitude()) {
                            altitude.setText(String.valueOf(location.getAltitude()));
                        } else {
                            altitude.setText("No altitude available");
                        }
                        if (location.hasSpeed()) {
                            speed.setText(String.valueOf(location.getSpeed()) + "m/s");
                        } else {
                            speed.setText("No speed available");
                        }

                    }
                }
            });
        } else {
            // request permissions
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_FINE_LOCATION);
            }
        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case MY_PERMISSION_REQUEST_FINE_LOCATION:

                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //permission was granted do nothing and carry on

                } else {
                    Toast.makeText(getApplicationContext(), "This app requires location permissions to be granted", Toast.LENGTH_SHORT).show();
                    finish();
                }

                break;

        }
    }
}