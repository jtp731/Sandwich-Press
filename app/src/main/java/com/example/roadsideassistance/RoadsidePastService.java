package com.example.roadsideassistance;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class RoadsidePastService extends AppCompatActivity {
    RoadsideAssistant roadsideAssistant;
    Service service;
    AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roadside_past_service);

        database = AppDatabase.getDatabase(this);
        roadsideAssistant = getIntent().getParcelableExtra("Roadside");
        service = getIntent().getParcelableExtra("Service");

        TextView carDescription = findViewById(R.id.carDescription);
        carDescription.setText("Car: " + database.carDao().getCar(service.customer_username, service.car_plateNum).toString());

        TextView customerUsername = findViewById(R.id.customerUsername);
        customerUsername.setText("Customer Username: " + service.customer_username);

        TextView pay = findViewById(R.id.pay);
        pay.setText(String.format("Pay: $%.2f", service.costToPay()));

        TextView serviceDescription = findViewById(R.id.serviceDescription);
        if(service.description != null || service.description.equals(""))
            serviceDescription.setText("Description: " + service.description);
        else
            serviceDescription.setVisibility(View.GONE);

        if(!service.hasFlag(Service.FLAT_BATTERY))
            findViewById(R.id.flatBattery).setVisibility(View.GONE);

        if(!service.hasFlag(Service.CAR_STUCK))
            findViewById(R.id.carStuck).setVisibility(View.GONE);

        if(!service.hasFlag(Service.OUT_OF_FUEL))
            findViewById(R.id.emergencyFuel).setVisibility(View.GONE);

        if(!service.hasFlag(Service.KEYS_IN_CAR))
            findViewById(R.id.lockedKeys).setVisibility(View.GONE);

        if(!service.hasFlag(Service.FLAT_TYRE))
            findViewById(R.id.flatTyre).setVisibility(View.GONE);

        if(!service.hasFlag(Service.MECHANICAL_BREAKDOWN))
            findViewById(R.id.mechBreakdown).setVisibility(View.GONE);

    }
}
