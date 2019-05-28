package com.example.roadsideassistance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class CustomerPastServices extends AppCompatActivity {

    AppDatabase database;
    Customer customer;
    int selectedServiceIndex = -1;
    List<Service> pastServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_past_services);

        database = AppDatabase.getDatabase(this);
        customer = getIntent().getParcelableExtra("Customer");

        View paymentType = findViewById(R.id.payType);
        paymentType.post(new Runnable() {
            @Override
            public void run() {
                createList();
            }
        });
    }

    private void createList() {
        final LinearLayout pastServicesLayout = findViewById(R.id.pastServicesLayout);
        pastServicesLayout.removeAllViews();
        pastServices = database.serviceDao().getPastCustomerServices(customer.username);
        if(pastServices != null && pastServices.size() > 0) {
            for(int i = 0; i < pastServices.size(); i++) {
                final LinearLayout serviceLayout = new LinearLayout(this);
                serviceLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                serviceLayout.setOrientation(LinearLayout.HORIZONTAL);

                TextView plateNum = new TextView(this);
                plateNum.setText(pastServices.get(i).car_plateNum);
                plateNum.setPadding(5,5,5,5);
                plateNum.setBackground(getResources().getDrawable(R.drawable.border_sharp));
                plateNum.setWidth(findViewById(R.id.plateNum).getWidth());
                serviceLayout.addView(plateNum);

                TextView roadsideUsername = new TextView(this);
                roadsideUsername.setText(pastServices.get(i).roadside_assistant_username);
                roadsideUsername.setPadding(5,5,5,5);
                roadsideUsername.setBackground(getResources().getDrawable(R.drawable.border_sharp));
                roadsideUsername.setWidth(findViewById(R.id.customerUsername).getWidth());
                serviceLayout.addView(roadsideUsername);

                TextView date = new TextView(this);
                date.setText(String.format("%d/%d/%d",
                        pastServices.get(i).time.getDate(),
                        pastServices.get(i).time.getMonth(),
                        pastServices.get(i).time.getYear() + 1900));
                date.setPadding(5,5,5,5);
                date.setBackground(getResources().getDrawable(R.drawable.border_sharp));
                date.setWidth(findViewById(R.id.date).getWidth());
                serviceLayout.addView(date);

                TextView payType = new TextView(this);
                payType.setText(pastServices.get(i).status == Service.PAYED_WITH_CARD ? "CARD" : "SUB");
                payType.setPadding(5,5,5,5);
                payType.setBackground(getResources().getDrawable(R.drawable.border_sharp));
                payType.setWidth(findViewById(R.id.payType).getWidth());
                serviceLayout.addView(payType);

                final int currIndex = i;
                serviceLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedServiceIndex = currIndex;
                        for (int j = 0; j < pastServicesLayout.getChildCount(); j++) {
                            pastServicesLayout.getChildAt(j).setBackgroundColor(getResources().getColor(R.color.unselectedListItem));
                        }
                        serviceLayout.setBackgroundColor(getResources().getColor(R.color.selectedListItem));
                    }
                });

                pastServicesLayout.addView(serviceLayout);
            }
        }
        else {
            TextView noPastServices = new TextView(this);
            noPastServices.setText("No Past Services");
            pastServicesLayout.addView(noPastServices);
            findViewById(R.id.selectPastServiceButton).setVisibility(View.GONE);
        }
    }

    public void toSelectedPastService(View view) {
        if(selectedServiceIndex >= 0) {
            Intent intent = new Intent(this, CustomerPastService.class);
            intent.putExtra("Customer", customer);
            intent.putExtra("Service", pastServices.get(selectedServiceIndex));
            startActivity(intent);
        }
    }
}
