package com.example.roadsideassistance;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Vector;

public class CustomerServiceRequest extends AppCompatActivity {
    AppDatabase database;
    Customer customer;
    Spinner carSpinner;
    ArrayAdapter<String> carStrings;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_service_request);

        customer = new Customer("cust1", "123", "33334444", "cust1@email", "jane", "doe");
        List<Car> cars = new ArrayList<>();
        cars.add(new Car("11ss33", "Mazda", "3", "green", new Date()));
        cars.add(new Car("22ss33", "Mitsu", "lancer", "green", new Date()));
        cars.add(new Car("33ss33", "BMW", "m3", "green", new Date()));
        cars.add(new Car("44ss33", "Jaguar", "thing", "green", new Date()));
        customer.cars = cars;
        carSpinner = findViewById(R.id.carSpinner);

        if(customer.cars.size() > 0) {
            Vector<String> carsAsString = new Vector<>();
            Car currCar;
            for (int i = 0; i < customer.cars.size(); i++) {
                currCar = customer.cars.get(i);
                carsAsString.add("Plate: " + currCar.plateNum + " Make: " + currCar.manufacturer + " Model: " + currCar.model);
            }
            carStrings = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, carsAsString);
            carSpinner.setAdapter(carStrings);
        }
        else {
            //Error
        }
    }

    public void requestService(View view) {
        if (customer.cars.size() > 0) {

        }
        else {
            Toast.makeText(this, "You don't have any cars", Toast.LENGTH_LONG).show();
        }
    }
}
