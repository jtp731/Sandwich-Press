package com.example.roadsideassistance;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Date;
import java.util.Vector;

public class CustomerServiceRequest extends AppCompatActivity {
    AppDatabase database;
    Customer customer;
    Spinner carSpinner;
    CheckBox breakdown;
    CheckBox battery, tyre, keys, fuel, stuck, other;
    EditText description;
    ArrayAdapter<String> carStrings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_service_request);
        database = AppDatabase.getDatabase(this);

        customer = getIntent().getParcelableExtra("Customer");
        carSpinner = findViewById(R.id.carSpinner);
        breakdown = findViewById(R.id.breakBox);
        battery = findViewById(R.id.batteryBox);
        tyre = findViewById(R.id.tyreBox);
        keys = findViewById(R.id.keysBox);
        fuel = findViewById(R.id.fuelBox);
        stuck = findViewById(R.id.stuckBox);
        other = findViewById(R.id.otherBox);
        description = findViewById(R.id.description);

        if(customer.cars.size() > 0) {
            Vector<String> carsAsString = new Vector<>();
            Car currCar;
            for (int i = 0; i < customer.cars.size(); i++) {
                currCar = customer.cars.get(i);
                carsAsString.add(currCar.manufacturer + " " + currCar.model + " " + currCar.plateNum);
            }
            carStrings = new ArrayAdapter<String>(this, R.layout.activity_spinner, carsAsString);
            carSpinner.setAdapter(carStrings);
        }
        else {
            //Error
            Vector<String> errorArray = new Vector<>();
            errorArray.add("No Cars Available");
            carStrings = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, errorArray);
            carSpinner.setAdapter(carStrings);
            findViewById(R.id.customerRequestServiceButton).setVisibility(View.GONE);
        }
    }

    public void requestService(View view) {
        String details = "";
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
                                        0,
                                        (byte)0,
                                        "");

                                if (!breakdown.isChecked() && !battery.isChecked() && !tyre.isChecked() && !keys.isChecked() && !fuel.isChecked() && !stuck.isChecked() && !other.isChecked()) {
                                    Toast.makeText(CustomerServiceRequest.this, "Please select a service option", Toast.LENGTH_LONG).show();
                                } else if (other.isChecked() && description.length() == 0){
                                    Toast.makeText(CustomerServiceRequest.this, "Please enter a description", Toast.LENGTH_LONG).show();
                                } else {
                                    final String details;
                                    if (description.length() > 0) {
                                        details = description.getText().toString();
                                    }
                                    //Store types of service
                                    if (breakdown.isChecked()){

                                    }
                                    if (battery.isChecked()){

                                    }
                                    if (tyre.isChecked()){

                                    }
                                    if (keys.isChecked()){

                                    }
                                    if (fuel.isChecked()){

                                    }
                                    if (stuck.isChecked()){

                                    }
                                    if (other.isChecked()){

                                    }
                                    //check if a service like this exists
                                    if (database.serviceDao().serviceActive(newService.roadside_assistant_username, newService.customer_username, newService.car_plateNum, newService.time)) {
                                        Toast.makeText(getContext(), "You already have a service for this car", Toast.LENGTH_LONG).show();
                                    } else {
                                        database.serviceDao().addService(newService);
                                        customer.services.add(newService);
                                        finish();
                                    }
                                }
                            }
                        }
                    }
                });
            } else {
                Toast.makeText(this, "GPS not permitted", Toast.LENGTH_LONG).show();
            }
        } else {
            Toast.makeText(this, "You don't have any cars", Toast.LENGTH_LONG).show();
        }
    }

    public Context getContext() {
        return this;
    }

    @Override
    public void finish() {
        Intent data = new Intent();
        data.putExtra("Customer", customer);
        setResult(RESULT_OK, data);
        super.finish();
    }

    //Alter description edittext hint string
    public void otherOption(View view) {
        other = findViewById(R.id.otherBox);
        if (other.isChecked()){
            description = findViewById(R.id.description);
            description.setHint("Required description");
        } else {
            description = findViewById(R.id.description);
            description.setHint("Optional description");
        }

    }
}
