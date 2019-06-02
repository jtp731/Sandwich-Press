package com.example.roadsideassistance;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

public class CustomerUpdateSubscription extends AppCompatActivity {

    Customer customer;
    Car car;
    ArrayAdapter<String> carStrings;
    Spinner carSpinner;
    TextView sub;
    Date today = new Date();
    Button subButton;
    String dateOutput = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_update_subscription);

        customer = getIntent().getParcelableExtra("Customer");
        sub = findViewById(R.id.expiryDate);
        carSpinner = findViewById(R.id.carSpinner);
        subButton = findViewById(R.id.subButton);

        if(customer.cars.size() > 0) {
            Car currCar;
            Vector<String> carsAsString = new Vector<>();
            for (int i = 0; i < customer.cars.size(); i++) {
                currCar = customer.cars.get(i);
                carsAsString.add(currCar.manufacturer + " " + currCar.model + " " + currCar.plateNum);
            }
            carStrings = new ArrayAdapter<String>(this, R.layout.activity_spinner, carsAsString);
            carSpinner.setAdapter(carStrings);
        } else {
            //Error
            Vector<String> errorArray = new Vector<>();
            errorArray.add("No Cars Available");
            carStrings = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, errorArray);
            carSpinner.setAdapter(carStrings);
        }

        carSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                car = customer.cars.get(carSpinner.getSelectedItemPosition());
                if (car.subType == Car.ONE_YEAR_SUB){
                    dateOutput = DateFormat.format("dd-MM-yyyy", car.renewalDate).toString();
                    subButton.setText("Turn off auto renewal");
                } else if (car.renewalDate.after(today) && car.subType == Car.FREE_SUB){
                    dateOutput = DateFormat.format("dd-MM-yyyy", car.renewalDate).toString();
                    subButton.setText("Turn on auto renewal");
                } else if (car.subType == Car.FREE_SUB){
                    dateOutput = "Not subscribed";
                    subButton.setText("Subscribe");
                }
                sub.setText(dateOutput);
                subButton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (car.subType == Car.FREE_SUB && car.renewalDate.before(today)){
                            final Calendar c = Calendar.getInstance();
                            c.setTime(new Date());
                            c.add(Calendar.MONTH, 12);
                            final Date date = c.getTime();
                            new AlertDialog.Builder(CustomerUpdateSubscription.this).setTitle("Subscribe")
                                    .setMessage("12 Month Subscription will start today and finish on " + DateFormat.format("dd-MM-yyyy", date).toString())
                                    .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            car.subType = Car.ONE_YEAR_SUB;
                                            car.renewalDate = date;
                                            dateOutput = DateFormat.format("dd-MM-yyyy", car.renewalDate).toString();
                                            subButton.setText("Turn off auto renewal");
                                            sub.setText(dateOutput);
                                        }
                                    })
                                    .setNegativeButton(android.R.string.cancel, null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        } else if (car.subType == Car.ONE_YEAR_SUB){
                            new AlertDialog.Builder(CustomerUpdateSubscription.this).setTitle("Turn off auto renewal")
                                    .setMessage("Are you sure you want to turn off auto renewal? Your current billing period will end on " + DateFormat.format("dd-MM-yyyy", car.renewalDate).toString())
                                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            car.subType = Car.FREE_SUB;
                                            Toast.makeText(CustomerUpdateSubscription.this, "Successfully cancelled auto renewal", Toast.LENGTH_LONG).show();
                                            dateOutput = DateFormat.format("dd-MM-yyyy", car.renewalDate).toString();
                                            subButton.setText("Turn on auto renewal");
                                            sub.setText(dateOutput);
                                        }
                                    })
                                    .setNegativeButton(android.R.string.cancel, null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        } else if (car.subType == Car.FREE_SUB && car.renewalDate.after(today)){
                            new AlertDialog.Builder(CustomerUpdateSubscription.this).setTitle("Turn off auto renewal")
                                    .setMessage("Auto renewal will be turned on. Next billing period will start on " + DateFormat.format("dd-MM-yyyy", car.renewalDate).toString())
                                    .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                                        @Override
                                        public void onClick(DialogInterface dialog, int which) {
                                            car.subType = Car.ONE_YEAR_SUB;
                                            Toast.makeText(CustomerUpdateSubscription.this, "Successfully turned on auto renewal", Toast.LENGTH_LONG).show();
                                            dateOutput = DateFormat.format("dd-MM-yyyy", car.renewalDate).toString();
                                            subButton.setText("Turn off auto renewal");
                                            sub.setText(dateOutput);
                                        }
                                    })
                                    .setNegativeButton(android.R.string.cancel, null)
                                    .setIcon(android.R.drawable.ic_dialog_alert)
                                    .show();
                        }
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


    }

    @Override
    public void finish(){
        Intent data = new Intent();
        data.putExtra("Customer", customer);
        setResult(RESULT_OK, data);
        super.finish();
    }

}
