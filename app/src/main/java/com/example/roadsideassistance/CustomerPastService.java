package com.example.roadsideassistance;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class CustomerPastService extends AppCompatActivity {
    Customer customer;
    Service service;
    AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_past_service);

        database = AppDatabase.getDatabase(this);
        customer = getIntent().getParcelableExtra("Customer");
        service = getIntent().getParcelableExtra("Service");

        TextView carDescription = findViewById(R.id.carDescription);
        carDescription.setText("Car: " + customer.getCar(service.car_plateNum).toString());

        TextView roadsideUsername = findViewById(R.id.customerUsername);
        roadsideUsername.setText("Roadside Username: " + service.roadside_assistant_username);

        TextView cost = findViewById(R.id.pay);
        if(service.status == Service.PAYED_WITH_CARD)
            cost.setText(String.format("Cost: $%.2f", service.cost));
        else
            cost.setText("Cost: Payed with Subscription");

        TextView serviceDescription = findViewById(R.id.serviceDescription);
        if(service.description != null && !service.description.trim().equals(""))
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
