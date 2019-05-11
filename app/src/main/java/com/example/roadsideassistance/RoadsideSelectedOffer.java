package com.example.roadsideassistance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

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
        TextView displayLine = findViewById(R.id.roadsideSelectedOfferCustomerUsername);
        displayLine.setText("Customer Username: " + offer.customer_username);

        Car serviceCar = database.carDao().getCar(offer.customer_username, offer.car_plateNum);
        displayLine = findViewById(R.id.roadsideOfferCarMake);
        displayLine.setText("Manufacturer: " + serviceCar.manufacturer);

        displayLine = findViewById(R.id.roadsideOfferCarModel);
        displayLine.setText("Model: " + serviceCar.model);

        displayLine = findViewById(R.id.roadsideOfferCarPlate);
        displayLine.setText("Plate Number: " + serviceCar.plateNum);

        displayLine = findViewById(R.id.roadsideOfferPay);
        displayLine.setText(String.format("Pay: $%.2f", offer.cost));//need to work out what the cost pay difference is
    }

    public void cancelOffer(View view) {
        roadsideAssistant.removeService(offer);
        database.serviceDao().deleteService(offer);
        finish();
    }

    @Override
    public void finish() {
        Intent data = new Intent();
        data.putExtra("Roadside", roadsideAssistant);
        setResult(RESULT_OK, data);
        super.finish();
    }
}
