package com.example.roadsideassistance;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Date;
import java.util.Vector;

public class CustomerMainPage extends AppCompatActivity {

    AppDatabase database;
    Button accountSettings;
    Customer customer;
    Spinner carSpinner;
    ArrayAdapter<String> carNames;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_main_page);
        database = AppDatabase.getDatabase(this);

        customer = getIntent().getParcelableExtra("Customer");

        accountSettings = findViewById(R.id.settings);
        carSpinner = findViewById(R.id.carSpinner);

        if(customer.cars.size() > 0) {
            Vector<String> carsAsString = new Vector<>();
            Car currCar;
            for (int i = 0; i < customer.cars.size(); i++) {
                currCar = customer.cars.get(i);
                carsAsString.add("Car: " + currCar.manufacturer + " " + currCar.model + " Plate:" + currCar.plateNum);
            }
            carNames = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, carsAsString);
            carSpinner.setAdapter(carNames);
        }
        else {
            //Error
            Vector<String> errorArray = new Vector<>();
            errorArray.add("No Cars Available");
            carNames = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, errorArray);
            carSpinner.setAdapter(carNames);
        }

        accountSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settings = new Intent(CustomerMainPage.this, CustomerSettings.class);
                settings.putExtra("Customer", customer);
                startActivity(settings);
            }
        });
    }

    public void requestService(View view) {
        if (customer.cars.size() > 0) {
            if (ContextCompat.checkSelfPermission(this.getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Task<Location> locationResult = LocationServices.getFusedLocationProviderClient(this).getLastLocation();
                locationResult.addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            Location currLocation = task.getResult();
                            if(currLocation != null) {
                                Service newService = new Service("",
                                        customer.username,
                                        customer.cars.get(carSpinner.getSelectedItemPosition()).plateNum,
                                        currLocation.getLatitude(),
                                        currLocation.getLongitude(),
                                        new Date(),
                                        0f,
                                        0);
                                if(database.serviceDao().serviceExists(newService.roadside_assistant_username, newService.customer_username, newService.car_plateNum, newService.time)){
                                    Toast.makeText(getContext(), "You already have a service for this car", Toast.LENGTH_LONG).show();
                                }
                                else {
                                    database.serviceDao().addService(newService);
                                    customer.services.add(newService);
                                    finish();
                                }
                            }
                        }
                    }
                });
            }
            else {
                Toast.makeText(this, "GPS not permitted", Toast.LENGTH_LONG).show();
            }
        }
        else {
            Toast.makeText(this, "You don't have any cars", Toast.LENGTH_LONG).show();
        }
    }

    public Context getContext() {
        return this;
    }
}
