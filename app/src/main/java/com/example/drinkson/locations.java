package com.example.drinkson;

import java.util.Random;

import android.Manifest;
import android.arch.persistence.room.Room;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.example.drinkson.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

import org.w3c.dom.Text;

import javax.net.ssl.HttpsURLConnection;

public class locations extends AppCompatActivity {

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

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        latitude.setText(String.valueOf(location.getLatitude()));
                        longitude.setText(String.valueOf(location.getLongitude()));

                    }
                }
            });
        } else {
            // request permissions
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSION_REQUEST_FINE_LOCATION);
            }
        }
        System.out.println(latitude.getText().toString()+ "hejsa");
        button.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {

                long id;
                int responseCode;
                final EditText target = findViewById(R.id.sharewith);

                Random randInt = new Random();

                // create new message/*
                messages newMessage = new messages();
                newMessage.sender = currentuser.getCurrentUser();
                newMessage.receiver = target.getText().toString();
                newMessage.body = "hej" + textView.getText().toString();
                newMessage.stamp = System.currentTimeMillis();
                System.out.println(newMessage.body);

                do {
                    id = randInt.nextInt();
                    newMessage.id = (int) id;
                    responseCode = repository.remotePost(Repository.MESSAGES, JSONConverter.encodeMessages(newMessage));
                } while (responseCode == HttpsURLConnection.HTTP_CONFLICT);

                repository.insertMessage(newMessage);
            }
        });
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


}}