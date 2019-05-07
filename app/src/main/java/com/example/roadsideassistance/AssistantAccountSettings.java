package com.example.roadsideassistance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AssistantAccountSettings extends AppCompatActivity {

    RoadsideAssistant roadsideAssistant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assistant_account_settings);

        roadsideAssistant = getIntent().getParcelableExtra("RoadsideAssistant");

        Button priceSettings = findViewById(R.id.button_assistant_price_set);
        priceSettings.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AssistantAccountSettings.this, AssistantPriceSettings.class);
                intent.putExtra("Roadside", roadsideAssistant);
                startActivity(intent);
            }
        });
    }
}
