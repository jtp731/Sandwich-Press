package com.example.roadsideassistance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

public class RoadsideSelectServiceCost extends AppCompatActivity {
    AppDatabase database;
    RoadsideAssistant roadsideAssistant;
    Service service;
    Double distance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roadside_service_select_cost);

        database = AppDatabase.getDatabase(this);

        service = getIntent().getParcelableExtra("Service");
        roadsideAssistant = getIntent().getParcelableExtra("RoadsideAssistant");
        distance = getIntent().getDoubleExtra("Distance", 0);

        TextView custUsername = findViewById(R.id.roadsideServiceCostUsername);
        custUsername.setText("Username: " + service.customer_username);

        TextView plateNum = findViewById(R.id.roadsideServiceCostPlateNum);
        plateNum.setText("Plate Number: " + service.car_plateNum);

        Car carToService = database.carDao().getCar(service.customer_username, service.car_plateNum);
        TextView make = findViewById(R.id.roadsideServiceCostMake);
        make.setText("Make: " + carToService.manufacturer);

        TextView model = findViewById(R.id.roadsideServiceCostModel);
        model.setText("Model: " + carToService.model);

        TextView distanceTextView = findViewById(R.id.roadsideServiceCostDistance);
        distanceTextView.setText("Distance: " + String.format("%.2f Km", distance));

        findViewById(R.id.roadsidePayText).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    TextView payView = findViewById(R.id.roadsidePayText);
                    String payString = payView.getText().toString();
                    if(!payString.trim().equals("")) {
                        float cost = Float.parseFloat(payString);
                        //Change cost to pay
                        cost += 10;
                        TextView costView = findViewById(R.id.roadsideCustomerCostText);
                        costView.setText("" + cost);
                    }
                }
            }
        });


        findViewById(R.id.roadsideCustomerCostText).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    TextView costView = findViewById(R.id.roadsideCustomerCostText);
                    String costString = costView.getText().toString();
                    if(!costString.trim().equals("")) {
                        float pay = Float.parseFloat(costString);
                        //Change pay to cost
                        pay -= 10;
                        TextView payView = findViewById(R.id.roadsidePayText);
                        payView.setText("" + pay);
                    }
                }
            }
        });
    }

    public void submitPay(View view) {
        //Toast.makeText(this, "" + roadsideAssistant.username, Toast.LENGTH_LONG).show();
        TextView costText = findViewById(R.id.roadsideCustomerCostText);
        if(!costText.getText().toString().trim().equals("")) {
            float cost = Float.parseFloat(costText.getText().toString());
            if (cost < 10) {
                //Error
            } else {
                database.serviceDao().addService(new Service(roadsideAssistant.username, service.customer_username, service.car_plateNum, service.latitude, service.longitude, service.time, cost, service.status, (byte)0, ""));
                roadsideAssistant.services.add(new Service(roadsideAssistant.username, service.customer_username, service.car_plateNum, service.latitude, service.longitude, service.time, cost, service.status, (byte)0, ""));
                finish();
            }
        }
    }

    @Override
    public void finish() {
        Intent data = new Intent();
        data.putExtra("Roadside", roadsideAssistant);
        setResult(RESULT_OK, data);
        super.finish();
    }
}
