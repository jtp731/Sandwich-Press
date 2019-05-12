package com.example.roadsideassistance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class CustomerSelectedActiveService extends AppCompatActivity {
    Customer customer;
    Service selectedService;
    AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_selected_service_active);

        customer = getIntent().getParcelableExtra("Customer");
        selectedService = getIntent().getParcelableExtra("Service");

        database = AppDatabase.getDatabase(this);

        TextView line = findViewById(R.id.customerSelectedActiveRoadsideUsername);
        line.setText("Roadside Username: " + selectedService.roadside_assistant_username);

        line = findViewById(R.id.customerSelectedActiveRoadsidePhone);
        RoadsideAssistant roadsideAssistant = database.roadsideAssistantDao().getRoadsideAssistantByUsername(selectedService.roadside_assistant_username);
        line.setText("Roadside Phone: " + roadsideAssistant.phonenumber);

        line = findViewById(R.id.customerSelectedActiveCarPlateNum);
        line.setText("Car Plate Number: " + selectedService.car_plateNum);

        line = findViewById(R.id.customerSelectedActiveCost);
        if(customer.carCoveredBySubscription(selectedService.car_plateNum))
            line.setText("Covered By Subscription");
        else
            line.setText(String.format("Cost: $%.2f", selectedService.cost));

        line = findViewById(R.id.description);
        String description = "Description: " + selectedService.description + "\n";
        if(selectedService.hasFlag(Service.CAR_STUCK))
            description += "\nCar Stuck";
        if(selectedService.hasFlag(Service.FLAT_BATTERY))
            description += "\nFlat Battery";
        if(selectedService.hasFlag(Service.FLAT_TYRE))
            description += "\nFlat Tyre";
        if(selectedService.hasFlag(Service.KEYS_IN_CAR))
            description += "\nKeys locked in car";
        if(selectedService.hasFlag(Service.MECHANICAL_BREAKDOWN))
            description += "\nMechanical Breakdown";
        if(selectedService.hasFlag(Service.OUT_OF_FUEL))
            description += "\nOut of Fuel";
        line.setText(description);
    }

    public void cancelActiveService(View view) {
        database.serviceDao().deleteService(customer.username, selectedService.car_plateNum, selectedService.time);
        customer.removeService(selectedService);
        finish();
    }

    @Override
    public void finish() {
        Intent data = new Intent();
        data.putExtra("Customer", customer);
        setResult(RESULT_OK, data);
        super.finish();
    }
}
