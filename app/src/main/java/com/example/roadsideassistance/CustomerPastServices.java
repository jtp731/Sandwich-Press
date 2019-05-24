package com.example.roadsideassistance;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class CustomerPastServices extends AppCompatActivity {

    AppDatabase database;
    Customer customer;
    Service service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_past_services);

        database = AppDatabase.getDatabase(this);
        customer = getIntent().getParcelableExtra("Customer");


    }


}
