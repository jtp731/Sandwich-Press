package com.example.roadsideassistance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class CustomerServiceFinishedSelect extends AppCompatActivity {
    Customer customer;
    int selectedFinishedServiceIndex = -1;
    List<Service> finishedServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_service_finished_select);
        customer = getIntent().getParcelableExtra("Customer");

        createList();
    }

    public void selectFinishedServiceButton(View view) {
        if(selectedFinishedServiceIndex >= 0) {
            Intent intent = new Intent(this, CustomerServiceFinish.class);
            intent.putExtra("Customer", customer);
            intent.putExtra("Service", finishedServices.get(selectedFinishedServiceIndex));
            startActivityForResult(intent, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == 1) {
            customer = data.getParcelableExtra("Customer");
            finish();
        }
    }

    @Override
    public void finish() {
        Intent data = new Intent();
        data.putExtra("Customer", customer);
        setResult(RESULT_OK, data);
        super.finish();
    }

    private void createList() {
        finishedServices = customer.getFinishedServices();

        final LinearLayout servicesLayout = findViewById(R.id.customerFinishedServicesLayout);
        if (finishedServices.size() > 0) {
            for (int i = 0; i < finishedServices.size(); i++) {
                final int currIndex = i;
                final TextView serviceText = new TextView(this);
                Car car = customer.getCar(finishedServices.get(i).car_plateNum);
                serviceText.setText("Plate Number: " + car.plateNum + " Make: " + car.manufacturer + " Model: " + car.model);
                serviceText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedFinishedServiceIndex = currIndex;
                        for (int j = 0; j < servicesLayout.getChildCount(); j++) {
                            servicesLayout.getChildAt(j).setBackgroundColor(getResources().getColor(R.color.unselectedListItem));
                        }
                        serviceText.setBackgroundColor(getResources().getColor(R.color.selectedListItem));
                    }
                });
                servicesLayout.addView(serviceText);
            }
        } else {
            TextView noServicesText = new TextView(this);
            noServicesText.setText("NO FINISHED SERVICES");
            servicesLayout.addView(noServicesText);
            findViewById(R.id.customerSelectFinishedServiceButton).setVisibility(View.GONE);
        }
    }
}
