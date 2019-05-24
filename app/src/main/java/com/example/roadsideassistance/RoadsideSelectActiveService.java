package com.example.roadsideassistance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class RoadsideSelectActiveService extends AppCompatActivity {
    RoadsideAssistant roadsideAssistant;
    int selectedActiveServiceIndex = -1;
    ArrayList<Service> activeServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roadside_select_active);

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
        activeServices = roadsideAssistant.getActiveServices();

        final LinearLayout activeServicesLayout = findViewById(R.id.roadsideActiveSelectLayout);
        activeServicesLayout.removeViews(0, activeServicesLayout.getChildCount());
        //Toast.makeText(this, "" + serviceOffers.size(), Toast.LENGTH_LONG).show();
        if (activeServices != null && activeServices.size() > 0) {
            for (int i = 0; i < activeServices.size(); i++) {
                final int currIndex = i;

                final LinearLayout serviceLayout = new LinearLayout(this);
                serviceLayout.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                serviceLayout.setOrientation(LinearLayout.HORIZONTAL);

                TextView usernameText = new TextView(this);
                usernameText.setPadding(5,5,5,5);
                usernameText.setWidth(findViewById(R.id.username).getWidth());
                usernameText.setText(activeServices.get(i).customer_username);
                usernameText.setBackground(getResources().getDrawable(R.drawable.border_sharp));
                serviceLayout.addView(usernameText);

                TextView plateNumText = new TextView(this);
                plateNumText.setPadding(5,5,5,5);
                plateNumText.setWidth(findViewById(R.id.cost).getWidth());
                plateNumText.setText(activeServices.get(i).car_plateNum);
                plateNumText.setBackground(getResources().getDrawable(R.drawable.border_sharp));
                serviceLayout.addView(plateNumText);

                TextView payText = new TextView(this);
                payText.setPadding(5,5,5,5);
                payText.setWidth(findViewById(R.id.pay).getWidth());
                payText.setText(String.format("$%.2f", activeServices.get(i).costToPay()));
                payText.setBackground(getResources().getDrawable(R.drawable.border_sharp));
                serviceLayout.addView(payText);

                serviceLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedActiveServiceIndex = currIndex;
                        for (int j = 0; j < activeServicesLayout.getChildCount(); j++) {
                            activeServicesLayout.getChildAt(j).setBackgroundColor(getResources().getColor(R.color.unselectedListItem));
                        }
                        serviceLayout.setBackgroundColor(getResources().getColor(R.color.selectedListItem));
                    }
                });
                activeServicesLayout.addView(serviceLayout);

                /*
                final TextView serviceText = new TextView(this);
                serviceText.setText("Customer: " + activeServices.get(i).customer_username + " Plate: " + activeServices.get(i).car_plateNum + " Cost: " + activeServices.get(i).cost);
                serviceText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedActiveServiceIndex = currIndex;
                        for (int j = 0; j < activeServicesLayout.getChildCount(); j++) {
                            activeServicesLayout.getChildAt(j).setBackgroundColor(getResources().getColor(R.color.unselectedListItem));
                        }
                        serviceText.setBackgroundColor(getResources().getColor(R.color.selectedListItem));
                    }
                });
                activeServicesLayout.addView(serviceText);
                */
            }
        }
        else {
            TextView noActiveServices = new TextView(this);
            noActiveServices.setText("No Active Services");
            activeServicesLayout.addView(noActiveServices);
            findViewById(R.id.roadsideSelectActiveServiceButton).setVisibility(View.GONE);
        }
    }

    public void roadsideSelectActiveService(View view) {
        if (selectedActiveServiceIndex >= 0) {
            Intent intent = new Intent(this, RoadsideSelectedActiveService.class);
            intent.putExtra("Roadside", roadsideAssistant);
            intent.putExtra("Service", activeServices.get(selectedActiveServiceIndex));
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
