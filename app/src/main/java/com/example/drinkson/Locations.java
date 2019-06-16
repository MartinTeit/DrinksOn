package com.example.drinkson;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import java.util.Random;

import javax.net.ssl.HttpsURLConnection;

public class Locations extends AppCompatActivity {

    Button button;
    TextView textView;
    private TextView latitude;
    private TextView longitude;
    private FusedLocationProviderClient fusedLocationProviderClient;
    private static final int MY_PERMISSION_REQUEST_FINE_LOCATION = 101;
    private Repository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_locations);

        repository = new Repository(this);

        button = (Button) findViewById(R.id.share);
        textView = (TextView) findViewById(R.id.tvLatitude);

        //initialuse View
        latitude = (TextView) findViewById(R.id.tvLatitude);
        longitude = (TextView) findViewById(R.id.tvLongitude);

        // Gets the current/set latitude and longitude of the current User and sends it to display.
        // Checks for permission to lock in GPS location
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        latitude.setText(String.valueOf(location.getLatitude()));
                        longitude.setText(String.valueOf(location.getLongitude()));
                        String.valueOf(location.getLongitude());

                        System.out.println(latitude.getText().toString() + " - " + longitude.getText().toString());

                    }
                }
            });
        } else {
            // request permissions
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_FINE_LOCATION);
            }
        }

        // Button made to share location with the typed in User.
        // Sends Messages to selected User with the text made in "newMessage.body".
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                long id;
                int responseCode;
                final EditText target = findViewById(R.id.sharewith);
                int maxIteration = 10;
                int iteration = 0;


                Random randInt = new Random();

                // create new message/*
                Messages newMessage = new Messages();
                newMessage.sender = CurrentUser.getCurrentUser();
                newMessage.receiver = target.getText().toString();
                newMessage.body = "%GPS " + latitude.getText().toString() + " " + longitude.getText().toString();
                newMessage.stamp = System.currentTimeMillis();

                do {
                    id = randInt.nextInt();
                    System.out.println();
                    newMessage.id = (int) id;
                    responseCode = repository.remotePost(Repository.MESSAGES, JSONConverter.encodeMessages(newMessage));

                    iteration++;

                    // If there was a conflict on the remote database try again with another id
                    // while max iteration isn't exceeded
                } while (responseCode == HttpsURLConnection.HTTP_CONFLICT && iteration<maxIteration);

                if(iteration >= maxIteration && responseCode == HttpsURLConnection.HTTP_CONFLICT) {
                    // If max iteration exceeded write it in console, and don't post the message
                    System.out.println("max iteration exceeded");
                } else if (responseCode == HttpsURLConnection.HTTP_CREATED ) {
                    // If nothing went wrong insert the shared location in the local database
                    repository.insertMessage(newMessage);

                } else {
                    System.out.println(responseCode);
                }

            }
        });
    }

    // Should GPS location permission be denied pop-up text is made.
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


}}