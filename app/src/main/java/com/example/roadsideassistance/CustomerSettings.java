package com.example.roadsideassistance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CustomerSettings extends AppCompatActivity {

    Button manageCars;
    Button updateBilling;
    Button updateSubscription;
    Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_settings);

        Intent intent = getIntent();
        customer = intent.getParcelableExtra("Customer");

        manageCars = findViewById(R.id.manageCars);
        updateBilling = findViewById(R.id.updateBilling);
        updateSubscription = findViewById(R.id.updateSub);

        manageCars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent manage = new Intent(CustomerSettings.this, ManageCars.class);
                manage.putExtra("Customer", customer);
                startActivity(manage);
            }
        });

        updateBilling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent billing = new Intent(CustomerSettings.this, UpdateBilling.class);
                billing.putExtra("Customer", customer);
                startActivity(billing);
            }
        });

        updateSubscription.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent subscription = new Intent(CustomerSettings.this, UpdateSubscription.class);
                subscription.putExtra("Customer", customer);
                startActivity(subscription);
            }
        });
    }
}
