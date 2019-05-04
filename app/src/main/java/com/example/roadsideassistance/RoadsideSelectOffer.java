package com.example.roadsideassistance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class RoadsideSelectOffer extends AppCompatActivity {
    RoadsideAssistant roadsideAssistant;
    int selectedOfferIndex = -1;
    ArrayList<Service> offerList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roadside_select_offer);

        roadsideAssistant = getIntent().getParcelableExtra("Roadside");
        offerList = roadsideAssistant.getCurrentOffers();

        final LinearLayout offersLayout = findViewById(R.id.roadsideOfferLayout);
        if(offerList != null && offerList.size() >0) {
            for (int i = 0; i < offerList.size(); i++) {
                final int currIndex = i;
                final TextView offerText = new TextView(this);
                offerText.setText("Customer: " + offerList.get(i).customer_username + " Plate: " + offerList.get(i).car_plateNum + " Cost: " + offerList.get(i).cost);
                offerText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedOfferIndex = currIndex;
                        for (int j = 0; j < offersLayout.getChildCount(); j++) {
                            offersLayout.getChildAt(j).setBackgroundColor(getResources().getColor(R.color.unselectedListItem));
                        }
                        offerText.setBackgroundColor(getResources().getColor(R.color.selectedListItem));
                    }
                });
                offersLayout.addView(offerText);
            }
        } else {
            TextView noOffersMadeTextView = new TextView(this);
            noOffersMadeTextView.setText("No Offers have been made");
            offersLayout.addView(noOffersMadeTextView);
        }
    }

    public void selectOffer(View view) {
        if (selectedOfferIndex >= 0) {
            Intent intent = new Intent(this, RoadsideSelectedOffer.class);
            intent.putExtra("Roadside", roadsideAssistant);
            intent.putExtra("Offer", offerList.get(selectedOfferIndex));
            startActivity(intent);
        }
    }
}
