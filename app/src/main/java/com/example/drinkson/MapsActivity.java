package com.example.drinkson;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;

import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.common.api.GoogleApiClient;
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

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener,
        LocationListener {

    private GoogleMap drinkMap;
    private GoogleApiClient googleApiClient;
    private LocationRequest locationRequest;
    private Location myLastLocation;
    private double defaultLat = 55.3949091;
    private double defaultLng = 10.383569899999998;
    private Marker myCurrentLocationMarker;
    private static final int Request_My_Location_Code = 100;
    private Object LatLng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            checkMyLocationPermission();
        }

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        drinkMap = googleMap;

        LatLng defaultLatLng = new LatLng(defaultLat, defaultLng);
        //Default position is set since, the emulator doesn't get live GPS from laptop.
        //The following lines wouldn't be implemented, had this been a project to make an actual app which, should have gone global.
        if (defaultLatLng != null) {
            LatLng newDefaultLatLng = new LatLng(defaultLat, defaultLng);
        } else {
            LatLng latLng = new LatLng(myLastLocation.getLatitude(), myLastLocation.getLongitude());
            //LatLng will always return null as long as the app is run on an emulator - So this if statement is just for show.
            System.out.println("The location is :\n- Latitude : " + myLastLocation.getLatitude() + "\n- Longitude : " + myLastLocation.getLongitude());
        }

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

            buildGoogleApiClient();

            drinkMap.setMyLocationEnabled(true);

        }

        //Locations on places you can get a beer in Odense
        //Marker added to each placed.
        LatLng denBroelende = new LatLng(55.3947386, 10.38689009999996);
        LatLng denIrske = new LatLng(55.395447, 10.383587000000034);
        LatLng viggos = new LatLng(55.3976926, 10.381059100000016);
        LatLng nunChi = new LatLng(55.39768899999999, 10.393644900000027);
        LatLng blomsterOgBien = new LatLng(55.39768899999999, 10.393644900000027);
        LatLng froggies = new LatLng(55.3944139, 10.383610599999997);
        LatLng heidi = new LatLng(55.39449, 10.381629699999962);
        LatLng boogie = new LatLng(55.3978672, 10.388869500000055);
        LatLng laBar = new LatLng(55.3958248, 10.383471699999973);
        LatLng jamesDean = new LatLng(55.3944943, 10.383696600000007);
        //Add more places if you want.

        drinkMap.addMarker(new MarkerOptions().position(denBroelende).title("Den Brølende And"));
        drinkMap.addMarker(new MarkerOptions().position(denIrske).title("Old Irsh Pub"));
        drinkMap.addMarker(new MarkerOptions().position(nunChi).title("Nunchi & Blomster og Bien"));
        drinkMap.addMarker(new MarkerOptions().position(froggies).title("Froggy's"));
        drinkMap.addMarker(new MarkerOptions().position(heidi).title("Heidi's Pub"));
        drinkMap.addMarker(new MarkerOptions().position(boogie).title("Boogie's"));
        drinkMap.addMarker(new MarkerOptions().position(laBar).title("LA Bar"));
        drinkMap.addMarker(new MarkerOptions().position(jamesDean).title("James Dean"));

        if (defaultLatLng != null) {
            drinkMap.moveCamera(CameraUpdateFactory.newLatLngZoom(defaultLatLng, 16));
        } else {
            System.out.println("YOU SHALL NOT PASS\nSINCE YOU FUCKED UP!");
        }
    }

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

        switch (requestCode){

            case Request_My_Location_Code:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                        if (googleApiClient == null) {

                            buildGoogleApiClient();
                        }

                        drinkMap.setMyLocationEnabled(true);
                    }
                } else {

                    Toast.makeText(this, "You denied permission to your location, WHY!\nYou fuck tard??", Toast.LENGTH_SHORT).show();
                }
                return;
        }
    }

    protected synchronized void buildGoogleApiClient(){

        googleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
    }


    public void onLocationChanged(Location location) {

        myLastLocation = location;

        if (myCurrentLocationMarker != null){

            myCurrentLocationMarker.remove();
        }

        LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        MarkerOptions markerOptions = new MarkerOptions();
        markerOptions.position(latLng);
        markerOptions.title("My Location");
        markerOptions.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));

        myCurrentLocationMarker = drinkMap.addMarker(markerOptions);

        drinkMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
        drinkMap.animateCamera(CameraUpdateFactory.zoomBy(12));

        if (googleApiClient != null){

            LocationServices.FusedLocationApi.removeLocationUpdates(googleApiClient,  this);
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

        locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY);

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