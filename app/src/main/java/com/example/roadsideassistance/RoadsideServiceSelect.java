package com.example.roadsideassistance;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
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
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.util.Date;
import java.util.List;
import java.util.Vector;

public class RoadsideServiceSelect extends FragmentActivity implements OnMapReadyCallback {
    RoadsideAssistant roadsideAssistant;

    public AppDatabase database;
    private GoogleMap map;
    private Location currLocation = null;
    private double radius = 10;
    private int selectedServiceIndex = 0;
    private  Vector<Service> servicesInRadius;
    private Vector<Double> serviceDistances;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roadside_service_select);

        //get Roadside assistant
        //roadsideAssistant = getIntent().getParcelableExtra("RoadsideAssistant");
        roadsideAssistant = new RoadsideAssistant("road1", "123", "55554444", "road1@email", "road", "one", true, 0);

        //delete database before use
        this.deleteDatabase("appdatabase");
        database = AppDatabase.getDatabase(getApplicationContext());

        //Test services
        /*
        database.testDao().addRoadsideAssistant(new RoadsideAssistant("", "123", "00009999", "road1@email", "road", "one", true, 0));
        database.testDao().addRoadsideAssistant(new RoadsideAssistant("road1", "123", "00009998", "road2@email", "road", "two", true, 0));
        database.testDao().addCustomer(new Customer("cust1", "123", "33334444", "cust1@email", "customer", "one"));
        database.testDao().addCar(new Car("cust1", "11ss33", "model", "manufacturer", "green", new Date()));
        database.serviceDao().addService(new Service("cust1", "11ss33", -33.84, 151.2093));
        database.serviceDao().addService(new Service("cust1", "11ss33", -33.85, 151.2090));
        database.serviceDao().addService(new Service("cust1", "11ss33", -33.81, 151.12));
        */
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

                            //move the camera to current location and zoom to city level
                            map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentPosition, 10));
                            //map.addMarker(new MarkerOptions().position(currentPosition));

                            //Create circle to show radius of search
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

                            servicesInRadius = new Vector<>();
                            serviceDistances = new Vector<>();

                            //Get all services in radius from all services in the square area
                            if(servicesInArea.size() > 0) {
                                for (int i = 0; i < servicesInArea.size(); i++) {
                                    if (LatitudeLongitude.distance(currentPosition, new LatLng(servicesInArea.get(i).latitude, servicesInArea.get(i).longitude)) <= radius)
                                        servicesInRadius.add(servicesInArea.get(i));
                                }
                            }

                            final LinearLayout servicesListLayout = findViewById(R.id.roadsideServicesSelectList);
                            if(servicesInRadius.size() > 0) {
                                for (int i = 0; i < servicesInRadius.size(); i++) {
                                    final int currIndex = i;

                                    //Add the service to the map
                                    map.addMarker(new MarkerOptions().position(new LatLng(servicesInRadius.get(i).latitude, servicesInRadius.get(i).longitude)));
                                    //Add to distance array
                                    serviceDistances.add(LatitudeLongitude.distance(currentPosition, new LatLng(servicesInRadius.get(i).latitude, servicesInRadius.get(i).longitude)));

                                    //Create text box to add to list of services
                                    final TextView addedTextView = new TextView(getContext());
                                    addedTextView.setText(servicesInRadius.get(i).toString() +
                                            " Dist = " + serviceDistances.get(i).intValue() + "km");

                                    //Set function when service is selected
                                    addedTextView.setOnClickListener(new View.OnClickListener() {
                                        @Override
                                        public void onClick(View v) {
                                            selectedServiceIndex = currIndex;
                                            for (int j = 0; j < servicesListLayout.getChildCount(); j++) {
                                                servicesListLayout.getChildAt(j).setBackgroundColor(getResources().getColor(R.color.unselectedListItem));
                                            }
                                            addedTextView.setBackgroundColor(getResources().getColor(R.color.selectedListItem));
                                        }
                                    });

                                    //Set constraints and layout
                                    addedTextView.setPadding(0, 10, 0, 10);
                                    addedTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));

                                    //Add it to the list
                                    servicesListLayout.addView(addedTextView);
                                }
                            }
                            //Show no services when there aren't any in the area
                            else {
                                final TextView noServiceTextView = new TextView(getContext());
                                noServiceTextView.setText("No Services in Area");
                                noServiceTextView.setPadding(0, 10, 0, 10);
                                noServiceTextView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                                servicesListLayout.addView(noServiceTextView);

                                //Hide button so it can't be clicked
                                findViewById(R.id.roadSideSelectServiceButton).setVisibility(View.GONE);
                            }
                        }
                    }
                }
            });
        }
        else
            Toast.makeText(this, "Not Permitted to use GPS", Toast.LENGTH_LONG).show();
    }

    public void onClick(View view) {
        //Toast.makeText(this, "Selected service = " + servicesInRadius.get(selectedServiceIndex).toString(), Toast.LENGTH_LONG).show();
        Intent intent = new Intent(this, RoadsideSelectServiceCost.class);
        intent.putExtra("Service", servicesInRadius.get(selectedServiceIndex));
        intent.putExtra("RoadsideAssistant", roadsideAssistant);
        intent.putExtra("Distance", serviceDistances.get(selectedServiceIndex));
        startActivityForResult(intent,1 );
    }

    //Used to get context while in callback
    public Context getContext() {
        return (Context)this;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == 1) {
            roadsideAssistant = data.getParcelableExtra("Roadside");
        }
    }
}
