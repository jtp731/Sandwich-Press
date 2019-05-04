package com.example.roadsideassistance;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class RoadsideSelectedOffer extends AppCompatActivity {
    RoadsideAssistant roadsideAssistant;
    Service offer;
    AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roadside_selected_offer);
        database = AppDatabase.getDatabase(this);

        roadsideAssistant = getIntent().getParcelableExtra("Roadside");
        offer = getIntent().getParcelableExtra("Offer");

        //display offer
    }

    public void cancelOffer(View view) {
        roadsideAssistant.removeService(offer);
        database.serviceDao().deleteService(offer);
        finish();
    }
}
