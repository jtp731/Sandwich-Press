package com.example.roadsideassistance;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class RoadsideServiceSelect extends FragmentActivity implements OnMapReadyCallback {
    private GoogleMap map;
    private Location currLocation = null;
    private double radius = 10;
    public LatLng diffLatLang;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roadside_service_select);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.serviceSelectMap);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map = googleMap;
        map.getUiSettings().setRotateGesturesEnabled(false);

        if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            Task<Location> locationResult = LocationServices.getFusedLocationProviderClient(this).getLastLocation();
            locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                @Override
                public void onComplete(@NonNull Task<Location> task) {
                    if (task.isSuccessful()) {
                        //Toast.makeText(getApplicationContext(), "Task good", Toast.LENGTH_LONG).show();
                        currLocation = task.getResult();
                        if (currLocation != null) {
                            LatLng currentPosition = new LatLng(currLocation.getLatitude(), currLocation.getLongitude());
                            LatLng diffLatLng = LatitudeLongitude.getDifferences(currentPosition.latitude, currentPosition.longitude, radius);

                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 10));

                            map.addMarker(new MarkerOptions().position(currentPosition));
                            map.addMarker(new MarkerOptions().position(new LatLng(currentPosition.latitude - diffLatLng.latitude, currentPosition.longitude - diffLatLng.longitude)));
                            map.addMarker(new MarkerOptions().position(new LatLng(currentPosition.latitude + diffLatLng.latitude, currentPosition.longitude + diffLatLng.longitude)));
                            map.addMarker(new MarkerOptions().position(new LatLng(currentPosition.latitude + diffLatLng.latitude, currentPosition.longitude - diffLatLng.longitude)));
                            map.addMarker(new MarkerOptions().position(new LatLng(currentPosition.latitude - diffLatLng.latitude, currentPosition.longitude + diffLatLng.longitude)));
                        }
                    }
                }
            });
            //Get services in area
        }
        else
            Toast.makeText(this, "not permitted", Toast.LENGTH_LONG).show();
    }
}
