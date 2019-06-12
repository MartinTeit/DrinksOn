package com.example.drinkson;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private List<messages> myMessages;
    private GoogleMap drinkMap;
    private GoogleApiClient googleApiClient;
    private Repository repository;
    private LocationRequest locationRequest;
    private String markerLong;
    private String markerlat;
    private String Reciever;

    private Location userLastLocation;

    private Marker myCurrentLocationMarker;

    private static final int Request_My_Location_Code = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);


        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        // Creates the map fragment shown in the map.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkMyLocationPermission();
        }

        int newestmessageID = 0;
        long higheststamp = 0;
        repository = new Repository(this);
        myMessages = repository.getAllMyMessages();
        int i = 0;
        for (messages messages: myMessages) {
            if (messages.body.contains("%GPS") && messages.stamp > higheststamp) {
                newestmessageID = messages.id;
            }
        }

        for (messages messages1: myMessages)
            if (messages1.body.contains("%GPS") && (messages1.id == newestmessageID)) {
                String[] f = messages1.body.split( " ");
                markerlat = f[1];
                markerLong = f[2];
                Reciever = messages1.receiver;
            }

    }

    // Checks the permission and asks for users persmission to use the location.
    public boolean checkMyLocationPermission(){

        //Checks if user has allowed acces to users location.
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            //If acces not given it will ask for it.
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION)){

                //Show the acces options
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION},Request_My_Location_Code );
            } else {

                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, Request_My_Location_Code);
            }

            //Should user decide on not having hes location shared map not show current location.
            return false;
        } else {

            return true;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {

        if (requestCode == Request_My_Location_Code) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    if (googleApiClient == null) {

                        buildGoogleApiClient();
                    }

                    drinkMap.setMyLocationEnabled(true);
                }
            } else
                Toast.makeText(this, "You denied permission to your location, WHY!\nYou fuck tard??", Toast.LENGTH_LONG).show();
        }
    }

    protected synchronized void buildGoogleApiClient(){

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }

    @Override
    public void onLocationChanged(Location location) {

        userLastLocation = location;

        if (myCurrentLocationMarker != null){

            myCurrentLocationMarker.remove();
        }

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());

        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

        myCurrentLocationMarker = drinkMap.addMarker(markerOptions);

        drinkMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        drinkMap.animateCamera(CameraUpdateFactory.zoomBy(12));

        if (googleApiClient != null){
            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,  this);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {

        drinkMap = googleMap;

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            buildGoogleApiClient();
            drinkMap.setMyLocationEnabled(true);

        }

        LatLng denBroelende = new LatLng(Double.parseDouble(markerlat), Double.parseDouble(markerLong));
        drinkMap.addMarker(new MarkerOptions().position(denBroelende).title(Reciever));
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            LocationServices.FusedLocationApi.requestLocationUpdates(googleApiClient, locationRequest, this);
        }
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


}

