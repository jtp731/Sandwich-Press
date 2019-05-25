package com.example.roadsideassistance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import static java.lang.Float.parseFloat;
import static java.lang.Integer.parseInt;

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

        EditText payText = findViewById(R.id.roadsidePayText);
        payText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count > 0) {
                    float pay = Float.parseFloat(s.toString().trim());
                    TextView costText = findViewById(R.id.roadsideCustomerCostText);
                    if (!costText.getText().toString().trim().equals("")) {
                        float cost = Float.parseFloat(costText.getText().toString().trim());
                        if (cost != (pay + 10)) {
                            costText.setText("" + (pay + 10));
                        }
                    }
                    else {
                        costText.setText("" + (pay + 10));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        EditText costText = findViewById(R.id.roadsideCustomerCostText);
        costText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(count > 0) {
                    float cost = Float.parseFloat(s.toString().trim());
                    TextView payText = findViewById(R.id.roadsidePayText);
                    if (!payText.getText().toString().trim().equals("")) {
                        float pay = Float.parseFloat(payText.getText().toString().trim());
                        if(cost != (pay + 10)) {
                            payText.setText("" + (cost - 10));
                        }
                    }
                    else {
                        payText.setText("" + (cost - 10));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    public void submitPay(View view) {
        //Toast.makeText(this, "" + roadsideAssistant.username, Toast.LENGTH_LONG).show();
        TextView costText = findViewById(R.id.roadsideCustomerCostText);
        if(!costText.getText().toString().trim().equals("")) {
            float cost = Float.parseFloat(costText.getText().toString());
            if (cost < 10) {
                Toast.makeText(this, "Cost Too Low", Toast.LENGTH_LONG).show();
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
