package com.example.roadsideassistance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
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
        View pay = findViewById(R.id.pay);
        pay.post(new Runnable() {
            @Override
            public void run() {
                createList();
            }
        });
    }

    private void createList() {
        offerList = roadsideAssistant.getCurrentOffers();
        //Toast.makeText(this, "" + offerList.size(), Toast.LENGTH_LONG).show();

        final LinearLayout offersLayout = findViewById(R.id.roadsideOfferLayout);
        offersLayout.removeViews(0, offersLayout.getChildCount());
        if(offerList != null && offerList.size() >0) {
            for (int i = 0; i < offerList.size(); i++) {
                final int currIndex = i;

                final LinearLayout serviceLayout = new LinearLayout(this);
                serviceLayout.setOrientation(LinearLayout.HORIZONTAL);
                serviceLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                TextView usernameText = new TextView(this);
                usernameText.setPadding(5,5,5,5);
                usernameText.setWidth(findViewById(R.id.username).getWidth());
                usernameText.setText(offerList.get(i).customer_username);
                usernameText.setBackground(getResources().getDrawable(R.drawable.border_sharp));
                serviceLayout.addView(usernameText);

                TextView plateNumText = new TextView(this);
                plateNumText.setPadding(5,5,5,5);
                plateNumText.setWidth(findViewById(R.id.cost).getWidth());
                plateNumText.setText(offerList.get(i).car_plateNum);
                plateNumText.setBackground(getResources().getDrawable(R.drawable.border_sharp));
                serviceLayout.addView(plateNumText);

                TextView payText = new TextView(this);
                payText.setPadding(5,5,5,5);
                payText.setWidth(findViewById(R.id.pay).getWidth());
                payText.setText(String.format("$%.2f", offerList.get(i).costToPay()));
                payText.setBackground(getResources().getDrawable(R.drawable.border_sharp));
                serviceLayout.addView(payText);

                serviceLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedOfferIndex = currIndex;
                        for (int j = 0; j < offersLayout.getChildCount(); j++) {
                            offersLayout.getChildAt(j).setBackgroundColor(getResources().getColor(R.color.unselectedListItem));
                        }
                        serviceLayout.setBackgroundColor(getResources().getColor(R.color.selectedListItem));
                    }
                });
                offersLayout.addView(serviceLayout);

                /*
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
                */
            }
        } else {
            TextView noOffersMadeTextView = new TextView(this);
            noOffersMadeTextView.setText("No Offers have been made");
            offersLayout.addView(noOffersMadeTextView);
            findViewById(R.id.roadsideSelectOfferButton).setVisibility(View.GONE);
        }
    }

    public void selectOffer(View view) {
        if (selectedOfferIndex >= 0) {
            Intent intent = new Intent(this, RoadsideSelectedOffer.class);
            intent.putExtra("Roadside", roadsideAssistant);
            intent.putExtra("Offer", offerList.get(selectedOfferIndex));
            startActivityForResult(intent, 1);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == 1) {
            roadsideAssistant = data.getParcelableExtra("Roadside");
            createList();
        }
    }

    @Override
    public void finish() {
        Intent data = new Intent();
        data.putExtra("Roadside", roadsideAssistant);
        setResult(RESULT_OK, data);
        super.finish();
    }
}
