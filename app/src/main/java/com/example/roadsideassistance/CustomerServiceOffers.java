package com.example.roadsideassistance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class CustomerServiceOffers extends AppCompatActivity {
    AppDatabase database;
    Customer customer;
    int selectedServiceIndex = -1;
    List<Service> serviceOffers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_service_offers);

        customer = getIntent().getParcelableExtra("Customer");

        View time = findViewById(R.id.time);
        time.post(new Runnable() {
            @Override
            public void run() {
                createList();
            }
        });
    }

    public void selectServiceButton(View view) {
        if(selectedServiceIndex >= 0) {
            Intent intent = new Intent(this, CustomerServiceAcceptOrCancel.class);
            intent.putExtra("Customer", customer);
            intent.putExtra("Service", serviceOffers.get(selectedServiceIndex));
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

    private void createList() {
        serviceOffers = customer.getServiceRequests();

        final LinearLayout servicesLayout = findViewById(R.id.customerServiceOffersLayout);
        servicesLayout.removeViews(0, servicesLayout.getChildCount());
        if (serviceOffers.size() > 0) {
            for (int i = 0; i < serviceOffers.size(); i++) {
                final int currIndex = i;


                final LinearLayout offerLayout = new LinearLayout(this);
                offerLayout.setOrientation(LinearLayout.HORIZONTAL);
                offerLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                TextView plateNumText = new TextView(this);
                plateNumText.setPadding(5,5,5,5);
                plateNumText.setWidth(findViewById(R.id.plateNum).getWidth());
                plateNumText.setText(serviceOffers.get(i).car_plateNum);
                plateNumText.setBackground(getResources().getDrawable(R.drawable.border_sharp));
                plateNumText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
                offerLayout.addView(plateNumText);

                TextView dateText = new TextView(this);
                dateText.setPadding(5,5,5,5);
                dateText.setWidth(findViewById(R.id.time).getWidth());
                dateText.setText(String.format("%d/%d %d:%d",
                        serviceOffers.get(i).time.getDate(),
                        serviceOffers.get(i).time.getMonth(),
                        serviceOffers.get(i).time.getHours(),
                        serviceOffers.get(i).time.getMinutes()));
                dateText.setBackground(getResources().getDrawable(R.drawable.border_sharp));
                dateText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
                offerLayout.addView(dateText);

                offerLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedServiceIndex = currIndex;
                        for(int j = 0; j < servicesLayout.getChildCount(); j++) {
                            servicesLayout.getChildAt(j).setBackgroundColor(getResources().getColor(R.color.unselectedListItem));
                        }
                        offerLayout.setBackgroundColor(getResources().getColor(R.color.selectedListItem));
                    }
                });
                servicesLayout.addView(offerLayout);

                /*
                final TextView serviceText = new TextView(this);
                Car car = customer.getCar(serviceOffers.get(i).car_plateNum);
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
            noServicesText.setText("NO SERVICE REQUESTS");
            servicesLayout.addView(noServicesText);
            findViewById(R.id.customerSelectActiveServiceButton).setVisibility(View.GONE);
        }
    }
}
