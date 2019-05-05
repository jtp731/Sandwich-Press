package com.example.roadsideassistance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class CustomerMainPage extends AppCompatActivity {

    Button accountSettings;
    Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_main_page);

        Intent intent = getIntent();
        customer = intent.getParcelableExtra("Customer");

        accountSettings = findViewById(R.id.settings);

        accountSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent settings = new Intent(CustomerMainPage.this, CustomerSettings.class);
                settings.putExtra("Customer", customer);
                startActivity(settings);
            }
        });
    }

    public void requestAssistance(View view){

    }
}
