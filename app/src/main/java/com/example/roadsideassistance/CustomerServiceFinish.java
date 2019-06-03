package com.example.roadsideassistance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class CustomerServiceFinish extends AppCompatActivity {
    Customer customer;
    Service selectedService;
    AppDatabase database;
    Car serviceCar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_selected_finished_service);
        database = AppDatabase.getDatabase(this);

        customer = getIntent().getParcelableExtra("Customer");
        selectedService = getIntent().getParcelableExtra("Service");

        TextView displayLine = findViewById(R.id.customerSelectedFinishedRoadsideUsername);
        displayLine.setText(selectedService.roadside_assistant_username);

        serviceCar = customer.getCar(selectedService.car_plateNum);
        displayLine = findViewById(R.id.customerSelectedFinishedCarManufacturer);
        displayLine.setText(serviceCar.manufacturer);

        displayLine = findViewById(R.id.customerSelectedFinishedCarPlate);
        displayLine.setText(selectedService.car_plateNum);

        displayLine = findViewById(R.id.customerSelectedFinishedModel);
        displayLine.setText(serviceCar.model);

        displayLine = findViewById(R.id.customerSelectedFinishedCost);
        if(customer.carCoveredBySubscription(selectedService.car_plateNum)) {
            displayLine.setText("Covered By Subscription");
            Button payButton = findViewById(R.id.customerFinishServiceButton);
            payButton.setText("Finish");
        }
    }

    public void finishService(View view) {
        if(customer.bankAccount.pay(database.roadsideAssistantDao().getRoadsideAssistantByUsername(selectedService.roadside_assistant_username), customer, selectedService.car_plateNum, selectedService.cost)) {
            customer.finishService(selectedService);
            if(customer.carCoveredBySubscription(selectedService.car_plateNum))
                database.serviceDao().updateServiceStatus(selectedService.roadside_assistant_username, customer.username, selectedService.car_plateNum, selectedService.time, Service.PAYED_WITH_SUB);
            else
                database.serviceDao().updateServiceStatus(selectedService.roadside_assistant_username, customer.username, selectedService.car_plateNum, selectedService.time, Service.PAYED_WITH_CARD);

            Intent intent = new Intent(this, CustomerLeaveReview.class);
            intent.putExtra("Customer", customer);
            intent.putExtra("Service", selectedService);
            startActivityForResult(intent, 1);
        }
        else {
            if(customer.carCoveredBySubscription(selectedService.car_plateNum))
                Toast.makeText(this, "Unable to finish service", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(this, "Payment Failed", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void finish() {
        Intent data = new Intent();
        data.putExtra("Customer", customer);
        setResult(RESULT_OK, data);
        super.finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == 1) {
            customer = data.getParcelableExtra("Customer");
            finish();
        }
    }
}
