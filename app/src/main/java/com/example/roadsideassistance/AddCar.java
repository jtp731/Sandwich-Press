package com.example.roadsideassistance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class AddCar extends AppCompatActivity {

    Customer customer;
    AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);
        database = AppDatabase.getDatabase(getApplicationContext());
        findViewById(R.id.makeError).setVisibility(View.GONE);
        findViewById(R.id.modelError).setVisibility(View.GONE);
        findViewById(R.id.plateError).setVisibility(View.GONE);
        findViewById(R.id.colourError).setVisibility(View.GONE);
        findViewById(R.id.renewalError).setVisibility(View.GONE);

        customer = getIntent().getParcelableExtra("Customer");


    }

    public void AddCar(View view){
        String username = null;
        Boolean empty = false;
        String make = null;
        String model = null;
        String colour = null;
        String plate = null;
        String renewal = null;
        Date renewalDate = new Date();
        customer = getIntent().getParcelableExtra("Customer");

        Bundle extra = getIntent().getExtras();
        if (extra != null){
            username = customer.username;
        }

        EditText input = findViewById(R.id.carMake);
        if (input != null) {
            make = input.getText().toString();
        } else {
            empty = true;
            findViewById(R.id.makeError).setVisibility(View.GONE);
        }

        input = findViewById(R.id.carModel);
        if (input != null) {
            model = input.getText().toString();
        } else {
            empty = true;
            findViewById(R.id.modelError).setVisibility(View.GONE);
        }

        input = findViewById(R.id.carColour);
        if (input != null) {
            colour = input.getText().toString();
        } else {
            empty = true;
            findViewById(R.id.colourError).setVisibility(View.GONE);
        }

        input = findViewById(R.id.plateNum);
        if (input != null) {
            plate = input.getText().toString();
        } else {
            empty = true;
            findViewById(R.id.plateError).setVisibility(View.VISIBLE);
        }

        input = findViewById(R.id.renewal);
        if (input != null) {
            renewal = input.getText().toString();
            try {
                renewalDate = new SimpleDateFormat("dd/MM/yyyy").parse(renewal);
            } catch (ParseException e) {
                e.printStackTrace();
            }
        } else {
            empty = true;
            findViewById(R.id.renewalError).setVisibility(View.GONE);
        }

        if (empty == false) {
            Car car = new Car(username, plate, model, make, colour, renewalDate);
            database.carDao().addCar(car);
            customer.cars.add(car);
            Toast.makeText(this, "Success", Toast.LENGTH_LONG).show();

            Intent intent = new Intent(AddCar.this, ManageCars.class);
            intent.putExtra("Customer", customer);
            startActivity(intent);
        }
    }
}
