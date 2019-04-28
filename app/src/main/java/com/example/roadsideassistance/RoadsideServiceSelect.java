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
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Date;
import java.util.List;

public class RoadsideServiceSelect extends FragmentActivity implements OnMapReadyCallback {
    public AppDatabase database;
    private GoogleMap map;
    private Location currLocation = null;
    private double radius = 10;
    public LatLng diffLatLang;
    private List<Service> services;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roadside_service_select);

        //delete database before use
        this.deleteDatabase("appdatabase");
        database = AppDatabase.getDatabase(getApplicationContext());

        //Test services
        database.testDao().addCustomer(new Customer("cust1", "123", "33334444", "cust1@email", "customer", "one"));
        database.testDao().addCar(new Car("cust1", "11ss33", "model", "manufacturer", "green", new Date()));
        database.serviceDao().addService(new Service("cust1", "11ss33", -33.84, 151.2093));
        //End
        /*
        //Test getting services
        List<Service> services = database.serviceDao().getNewServiceRequests();
        System.out.print("Size = " + services.size() + "\n");
        if(services.get(0).roadside_assistant_username == null)
            System.out.print("roadside is null");
        else
            System.out.print("roadside is not null");
        */
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

                            //map.addMarker(new MarkerOptions().position(currentPosition));
                            map.addCircle(new CircleOptions()
                                    .center(currentPosition)
                                    .radius(radius*1e3)
                                    .strokeColor(getResources().getColor(R.color.mapRadius))
                                    .strokeWidth(4)
                                    .fillColor(getResources().getColor(R.color.mapFill)));

                            //map.addMarker(new MarkerOptions().position(new LatLng(currentPosition.latitude - diffLatLng.latitude, currentPosition.longitude - diffLatLng.longitude)));
                            //map.addMarker(new MarkerOptions().position(new LatLng(currentPosition.latitude + diffLatLng.latitude, currentPosition.longitude + diffLatLng.longitude)));
                            //map.addMarker(new MarkerOptions().position(new LatLng(currentPosition.latitude + diffLatLng.latitude, currentPosition.longitude - diffLatLng.longitude)));
                            //map.addMarker(new MarkerOptions().position(new LatLng(currentPosition.latitude - diffLatLng.latitude, currentPosition.longitude + diffLatLng.longitude)));

                            List<Service> servicesInArea = database.serviceDao().getNewServiceRequests(
                                    currentPosition.latitude - diffLatLng.latitude,
                                    currentPosition.latitude + diffLatLng.latitude,
                                    currentPosition.longitude - diffLatLng.longitude,
                                    currentPosition.longitude + diffLatLng.longitude);

                            services = servicesInArea;

                            LinearLayout servicesListLayout = findViewById(R.id.roadsideServicesSelectList);
                            LayoutInflater inflater = LayoutInflater.from();
                            for(Service service: services) {
                                map.addMarker(new MarkerOptions().position(new LatLng(service.latitude, service.longitude)));
                                //servicesListLayout.addView(new TextView(this, ));
                            }

                            //List<Service> servicesInRadius;
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
