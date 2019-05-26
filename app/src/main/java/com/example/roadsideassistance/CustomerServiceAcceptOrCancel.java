package com.example.roadsideassistance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

public class CustomerServiceAcceptOrCancel extends AppCompatActivity {
    AppDatabase database;
    Customer customer;
    Service activeService;
    int selectedOfferIndex = -1;
    List<Service> serviceOffers;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_selected_service);

        database = AppDatabase.getDatabase(this);
        customer = getIntent().getParcelableExtra("Customer");
        activeService = getIntent().getParcelableExtra("Service");
        serviceOffers = database.serviceDao().getServiceOffers(activeService.customer_username, activeService.car_plateNum, activeService.time);
        TextView serviceDescription = findViewById(R.id.customerSelectedServiceDescription);
        String fullDescription = "Plate Number: " + activeService.car_plateNum +
                "\nDescription: " + activeService.description;
        if(activeService.hasFlag(Service.CAR_STUCK))
            fullDescription += "\nCar Stuck";
        if(activeService.hasFlag(Service.FLAT_BATTERY))
            fullDescription += "\nFlat Battery";
        if(activeService.hasFlag(Service.FLAT_TYRE))
            fullDescription += "\nFlat Tyre";
        if(activeService.hasFlag(Service.KEYS_IN_CAR))
            fullDescription += "\nKeys locked in car";
        if(activeService.hasFlag(Service.MECHANICAL_BREAKDOWN))
            fullDescription += "\nMechanical Breakdown";
        if(activeService.hasFlag(Service.OUT_OF_FUEL))
            fullDescription += "\nOut of Fuel";
        serviceDescription.setText(fullDescription);

        View rating = findViewById(R.id.rating);
        rating.post(new Runnable() {
            @Override
            public void run() {
                createOfferList();
            }
        });
    }

    private void createOfferList() {
        final LinearLayout serviceOffersLayout = findViewById(R.id.customerServiceOffersLayout);
        boolean coveredBySubscription = customer.carCoveredBySubscription(activeService.car_plateNum);
        if(serviceOffers.size() > 0) {
            for(int i = 0; i < serviceOffers.size(); i++) {
                final int currIndex = i;
                final LinearLayout offerLayout = new LinearLayout(this);
                offerLayout.setOrientation(LinearLayout.HORIZONTAL);
                offerLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                RoadsideAssistant offerer = database.roadsideAssistantDao().getRoadsideAssistantByUsername(serviceOffers.get(i).roadside_assistant_username);

                TextView usernameText = new TextView(this);
                usernameText.setPadding(5,5,5,5);
                usernameText.setWidth(findViewById(R.id.customerUsername).getWidth());
                usernameText.setBackground(getResources().getDrawable(R.drawable.border_sharp));
                usernameText.setText(offerer.username);
                offerLayout.addView(usernameText);

                TextView costText = new TextView(this);
                costText.setPadding(5,5,5,5);
                costText.setWidth(findViewById(R.id.payType).getWidth());
                costText.setBackground(getResources().getDrawable(R.drawable.border_sharp));
                if(!coveredBySubscription)
                    costText.setText(String.format("$%.2f", serviceOffers.get(i).cost));
                else
                    costText.setText("Free");
                offerLayout.addView(costText);

                LinearLayout ratingBarLayout = new LinearLayout(this);
                ratingBarLayout.setPadding(5, 5, 5, 5);
                ratingBarLayout.setLayoutParams(new LinearLayout.LayoutParams(findViewById(R.id.rating).getWidth(), ViewGroup.LayoutParams.MATCH_PARENT));
                RatingBar ratingBar = new RatingBar(this, null, android.R.attr.ratingBarStyleSmall);
                ratingBar.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
                ratingBar.setNumStars(5);
                ratingBar.setStepSize(0.5f);
                ratingBar.setIsIndicator(true);
                ratingBar.setRating(offerer.rating);
                //ratingBar.setPadding(5,5,5,5);
                //ratingBar.setScaleX(0.5f);
                //ratingBar.setScaleY(0.5f);
                ratingBarLayout.setBackground(getResources().getDrawable(R.drawable.border_sharp));
                ratingBarLayout.addView(ratingBar);
                offerLayout.addView(ratingBarLayout);

                offerLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedOfferIndex = currIndex;
                        for(int j = 0; j < serviceOffersLayout.getChildCount(); j++) {
                            serviceOffersLayout.getChildAt(j).setBackgroundColor(getResources().getColor(R.color.unselectedListItem));
                        }
                        offerLayout.setBackgroundColor(getResources().getColor(R.color.selectedListItem));
                    }
                });
                serviceOffersLayout.addView(offerLayout);
                /*
                final TextView serviceOffer = new TextView(this);
                RoadsideAssistant offerer = database.roadsideAssistantDao().getRoadsideAssistantByUsername(serviceOffers.get(i).roadside_assistant_username);
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
                */
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
        System.out.println("Before " + customer.services.size());
        customer.removeService(activeService);
        System.out.println(" After " + customer.services.size());
        database.serviceDao().deleteService(activeService.customer_username, activeService.car_plateNum, activeService.time);
        finish();
    }

    public void acceptOffer(View view) {
        if (selectedOfferIndex >= 0) {
            Service service = serviceOffers.get(selectedOfferIndex);
            //Update customer list
            customer.updateServiceToAccepted(service);
            //Update database
            database.serviceDao().updateServiceStatus(service.roadside_assistant_username, service.customer_username, service.car_plateNum, service.time, 1);
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
}
