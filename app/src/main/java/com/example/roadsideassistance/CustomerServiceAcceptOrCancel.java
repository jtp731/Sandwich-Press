package com.example.roadsideassistance;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class CustomerServiceAcceptOrCancel extends AppCompatActivity {
    AppDatabase database;
    Customer customer;
    Service activeService;
    int selectedOfferIndex = -1;
    ArrayList<Service> serviceOffers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_selected_service);

        database = AppDatabase.getDatabase(this);
        customer = getIntent().getParcelableExtra("Customer");
        activeService = getIntent().getParcelableExtra("Service");
        serviceOffers = database.serviceDao().getServiceOffers(activeService.customer_username, activeService.car_plateNum, activeService.time);

        final LinearLayout serviceOffersLayout = findViewById(R.id.customerServiceOffersLayout);
        boolean coveredBySubscription = customer.carCoveredBySubscription(activeService.car_plateNum);
        if(serviceOffers.size() > 0) {
            for(int i = 0; i < serviceOffers.size(); i++) {
                final int currIndex = i;
                final TextView serviceOffer = new TextView(this);
                RoadsideAssistant offerer = database.roadsideAssistantDao().getRoadsideAssistant(serviceOffers.get(i).roadside_assistant_username);
                String text = "Username: " + offerer.username + " Rating: " + offerer.rating;
                if (!coveredBySubscription)
                    text += " Cost: " + serviceOffers.get(i).cost;
                serviceOffer.setText( text);
                serviceOffer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedOfferIndex = currIndex;
                        for(int j = 0; j < serviceOffersLayout.getChildCount(); j++) {
                            serviceOffersLayout.getChildAt(j).setBackgroundColor(getResources().getColor(R.color.unselectedListItem));
                        }
                        serviceOffer.setBackgroundColor(getResources().getColor(R.color.selectedListItem));
                    }
                });
                serviceOffersLayout.addView(serviceOffer);
            }
        }
        else {
            //No Offers
            TextView noOffersTextView = new TextView(this);
            noOffersTextView.setText("NO OFFERS MADE");
            serviceOffersLayout.addView(noOffersTextView);
        }
    }

    public void deleteService(View view) {
        customer.services.remove(activeService);
        database.serviceDao().deleteService(activeService.customer_username, activeService.car_plateNum, activeService.time);
        finish();
    }

    public void acceptOffer(View view) {
        if (selectedOfferIndex > 0) {
            Service service = serviceOffers.get(selectedOfferIndex);
            //Update customer list
            customer.updateServiceToAccepted(service);
            //Update database
            database.serviceDao().updateServiceStatus(service.roadside_assistant_username, service.customer_username, service.car_plateNum, service.time, 1);
            finish();
        }
    }
}
