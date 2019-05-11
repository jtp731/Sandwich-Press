package com.example.roadsideassistance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class CustomerMainPage extends AppCompatActivity {
    Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_main_page);

        customer = getIntent().getParcelableExtra("Customer");

        //Toast.makeText(this, "" + roadsideAssistant.services.size(), Toast.LENGTH_SHORT).show();
    }

    public void toNewService(View view) {
        Intent intent = new Intent(this, CustomerServiceRequest.class);
        intent.putExtra("Customer", customer);
        startActivityForResult(intent, 1);

        Toast.makeText(this, "" + customer.services.size(), Toast.LENGTH_LONG).show();
    }

    public void toCustServiceOffers(View view) {
        Intent intent = new Intent(this, CustomerServiceOffers.class);
        intent.putExtra("Customer", customer);
        startActivityForResult(intent, 1);
    }

    public void toActiveServices(View view) {
        Intent intent = new Intent(this, CustomerActiveServicesSelect.class);
        intent.putExtra("Customer", customer);
        startActivityForResult(intent, 1);
    }

    public void toFinishedServices(View view) {
        Intent intent = new Intent(this, CustomerServiceFinishedSelect.class);
        intent.putExtra("Customer", customer);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == 1) {
                customer = data.getParcelableExtra("Customer");
        }
    }
}
