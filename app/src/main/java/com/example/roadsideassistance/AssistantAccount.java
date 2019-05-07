package com.example.roadsideassistance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AssistantAccount extends AppCompatActivity {

    RoadsideAssistant roadsideAssistant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assistant_account);

        roadsideAssistant = getIntent().getParcelableExtra("RoadsideAssistant");

        Button assistDetails = findViewById(R.id.button_assistant_details);
        assistDetails.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AssistantAccount.this, AssistantDetails.class);
                intent.putExtra("Roadside", roadsideAssistant);
                startActivity(intent);
            }
        });

        Button assistPayHist = findViewById(R.id.button_assistant_pay_history);
        assistPayHist.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AssistantAccount.this, AssistantPaymentHistory.class);
                intent.putExtra("Roadside", roadsideAssistant);
                startActivity(intent);
            }
        });

        Button assistSettings = findViewById(R.id.button_assistant_settings);
        assistSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AssistantAccount.this, AssistantAccountSettings.class);
                intent.putExtra("Roadside", roadsideAssistant);
                startActivity(intent);
            }
        });
    }
}
