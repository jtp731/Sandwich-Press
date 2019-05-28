package com.example.roadsideassistance;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;

public class CustomerEditCar extends AppCompatActivity {

    AppDatabase database;
    Customer customer;
    Car car;
    EditText make, model, plateNum, colour;
    TextView sub;
    int carNum;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_edit_car);

        database = AppDatabase.getDatabase(getApplicationContext());
        findViewById(R.id.makeError).setVisibility(View.GONE);
        findViewById(R.id.modelError).setVisibility(View.GONE);
        findViewById(R.id.plateError).setVisibility(View.GONE);
        findViewById(R.id.colourError).setVisibility(View.GONE);

        customer = getIntent().getParcelableExtra("Customer");
        car = getIntent().getParcelableExtra("Car");
        carNum = getIntent().getIntExtra("Position", -1);
        if (carNum == -1){
            Toast.makeText(this, "Error selecting car", Toast.LENGTH_LONG).show();
        }

        make = findViewById(R.id.carMake);
        model = findViewById(R.id.carModel);
        plateNum = findViewById(R.id.payType);
        colour = findViewById(R.id.carColour);
        sub = findViewById(R.id.subscription);

        make.setText(car.manufacturer);
        model.setText(car.model);
        plateNum.setText(car.plateNum);
        colour.setText(car.colour);

        String dateOutput = "";
        Date today = new Date();
        if (car.renewalDate.before(today)){
            dateOutput = "Not subscribed";
        } else if (car.renewalDate.after(today)){
            dateOutput = DateFormat.format("dd-MM-yyyy", car.renewalDate).toString();
        }
        sub.setText(dateOutput);
    }

    public void EditCar(View view) {
        String username = customer.username;
        String oldPlateNum = car.plateNum;
        make = findViewById(R.id.carMake);
        model = findViewById(R.id.carModel);
        plateNum = findViewById(R.id.payType);
        colour = findViewById(R.id.carColour);
        sub = findViewById(R.id.subscription);

        String carMake = make.getText().toString().trim();
        String carModel = model.getText().toString().trim();
        String carPlate = plateNum.getText().toString().trim();
        String carColour = colour.getText().toString().trim();

        if (carMake.isEmpty()) {
            findViewById(R.id.makeError).setVisibility(View.VISIBLE);
        } else if (carModel.isEmpty()) {
            findViewById(R.id.modelError).setVisibility(View.VISIBLE);
        } else if (carPlate.isEmpty()) {
            findViewById(R.id.plateError).setVisibility(View.VISIBLE);
        } else if (carColour.isEmpty()) {
            findViewById(R.id.colourError).setVisibility(View.VISIBLE);
        } else if (carMake != car.manufacturer || carModel != car.model || carPlate != car.plateNum || carColour != car.colour){
            customer.cars.remove(carNum);
            database.carDao().editCar(carMake, carModel, carPlate, carColour, car.renewalDate, oldPlateNum);
            car = database.carDao().getCar(username, carPlate);
            customer.cars.add(car);
            Toast.makeText(CustomerEditCar.this, "Success", Toast.LENGTH_LONG).show();

            finish();
        }
    }

    public void RemoveCar(View view) {
        if (customer.cars.size() == 1){
            new AlertDialog.Builder(this).setTitle("Cannot Remove Car")
                    .setMessage("You cannot remove your only car.")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        } else {
            new AlertDialog.Builder(this).setTitle("Remove Car")
                    .setMessage("Are you sure you want to remove this car?")
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            customer.cars.remove(carNum);
                            database.carDao().deleteCar(car);
                            Toast.makeText(CustomerEditCar.this, "Successfully removed car", Toast.LENGTH_LONG).show();

                            finish();
                        }
                    })
                    .setNegativeButton(android.R.string.no, null)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .show();
        }
    }

    @Override
    public void finish(){
        Intent data = new Intent();
        data.putExtra("Customer", customer);
        setResult(RESULT_OK, data);
        super.finish();
    }
}