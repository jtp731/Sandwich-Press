package com.example.roadsideassistance;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class RoadsideSelectServiceCost extends AppCompatActivity {
    RoadsideAssistant roadsideAssistant;
    Service service;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roadside_service_select_cost);

        service = getIntent().getParcelableExtra("Service");
        //roadsideAssistant = getIntent().getParcelableExtra("RoadsideAssistant");


        findViewById(R.id.roadsidePayText).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    TextView payView = findViewById(R.id.roadsidePayText);
                    String payString = payView.getText().toString();
                    if(!payString.trim().equals("")) {
                        float cost = Float.parseFloat(payString);
                        //Change cost to pay
                        TextView costView = findViewById(R.id.roadsideCustomerCostText);
                        costView.setText("" + cost);
                    }
                }
            }
        });


        findViewById(R.id.roadsideCustomerCostText).setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    TextView costView = findViewById(R.id.roadsideCustomerCostText);
                    String costString = costView.getText().toString();
                    if(!costString.trim().equals("")) {
                        float pay = Float.parseFloat(costString);
                        //Change pay to cost
                        TextView payView = findViewById(R.id.roadsidePayText);
                        costView.setText("" + pay);
                    }
                }
            }
        });
    }

    public void submitPay(View view) {

    }
}
