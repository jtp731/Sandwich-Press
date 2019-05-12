package com.example.roadsideassistance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomerActiveServicesSelect extends AppCompatActivity {
    Customer customer;
    AppDatabase database;
    int selectedServiceIndex = -1;
    ArrayList<Service> activeServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_service_active_select);

        database = AppDatabase.getDatabase(this);

        customer = getIntent().getParcelableExtra("Customer");

        View model = findViewById(R.id.model);
        model.post(new Runnable() {
            @Override
            public void run() {
                createList();
            }
        });

    }

    private void createList() {
        activeServices = customer.getActiveServices();

        final LinearLayout servicesLayout = findViewById(R.id.customerActiveServicesLayout);
        servicesLayout.removeViews(0, servicesLayout.getChildCount());
        if (activeServices.size() > 0) {
            for (int i = 0; i < activeServices.size(); i++) {
                final int currIndex = i;
                Car car = customer.getCar(activeServices.get(i).car_plateNum);

                final LinearLayout carLayout = new LinearLayout(this);
                carLayout.setOrientation(LinearLayout.HORIZONTAL);
                carLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                //carLayout.setPadding(5,5,5,5);

                TextView plateNumberText = new TextView(this);
                plateNumberText.setWidth(findViewById(R.id.plateNum).getWidth());
                plateNumberText.setPadding(5,5,5,5);
                plateNumberText.setText(car.plateNum);
                plateNumberText.setBackground(getResources().getDrawable(R.drawable.border_sharp));
                carLayout.addView(plateNumberText);

                TextView makeText = new TextView(this);
                makeText.setWidth(findViewById(R.id.make).getWidth());
                makeText.setPadding(5,5,5,5);
                makeText.setText(car.manufacturer);
                makeText.setBackground(getResources().getDrawable(R.drawable.border_sharp));
                carLayout.addView(makeText);

                TextView modelText = new TextView(this);
                modelText.setWidth(findViewById(R.id.model).getWidth());
                modelText.setPadding(5,5,5,5);
                modelText.setText(car.model);
                modelText.setBackground(getResources().getDrawable(R.drawable.border_sharp));
                carLayout.addView(modelText);

                carLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedServiceIndex = currIndex;
                        for(int j = 0; j < servicesLayout.getChildCount(); j++) {
                            servicesLayout.getChildAt(j).setBackgroundColor(getResources().getColor(R.color.unselectedListItem));
                        }
                        carLayout.setBackgroundColor(getResources().getColor(R.color.selectedListItem));
                    }
                });

                servicesLayout.addView(carLayout);
                /*
                final TextView serviceText = new TextView(this);
                Car car = customer.getCar(activeServices.get(i).car_plateNum);
                serviceText.setText("Plate Number: " + car.plateNum + " Make: " + car.manufacturer + " Model: " + car.model);
                serviceText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedServiceIndex = currIndex;
                        for(int j = 0; j < servicesLayout.getChildCount(); j++) {
                            servicesLayout.getChildAt(j).setBackgroundColor(getResources().getColor(R.color.unselectedListItem));
                        }
                        serviceText.setBackgroundColor(getResources().getColor(R.color.selectedListItem));
                    }
                });
                servicesLayout.addView(serviceText);
                */
            }
        }
        else {
            TextView noServicesText = new TextView(this);
            noServicesText.setText("NO ACTIVE SERVICES");
            servicesLayout.addView(noServicesText);
            findViewById(R.id.customerSelectActiveServiceButton).setVisibility(View.GONE);
        }
    }

    public void selectActiveServiceButton(View view) {
        if(selectedServiceIndex >= 0) {
            Intent intent = new Intent(this, CustomerSelectedActiveService.class);
            intent.putExtra("Customer", customer);
            intent.putExtra("Service", activeServices.get(selectedServiceIndex));
            startActivityForResult(intent, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == 1) {
            customer = data.getParcelableExtra("Customer");
            createList();
            //finish();
        }
    }

    @Override
    public void finish() {
        Intent data = new Intent();
        data.putExtra("Customer", customer);
        setResult(RESULT_OK, data);
        super.finish();
    }
}
