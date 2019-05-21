package com.example.roadsideassistance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.RatingBar;
import android.widget.TextView;

public class CustomerLeaveReview extends AppCompatActivity {
    Customer customer;
    Service service;
    AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_leave_review);

        database = AppDatabase.getDatabase(this);

        customer = getIntent().getParcelableExtra("Customer");
        service = getIntent().getParcelableExtra("Service");

        TextView line = findViewById(R.id.roadsideUsernameReview);
        line.setText("Roadside Assistant Username: " + service.roadside_assistant_username);

        line = findViewById(R.id.carPlateNumReview);
        line.setText("Car Plate Number: " + service.car_plateNum);
    }

    void skipReview(View view) {
        finish();
    }

    void leaveReview(View view) {
        RatingBar ratingBar = findViewById(R.id.reviewRating);
        TextView reviewText = findViewById(R.id.reviewDescription);

        float rating = ratingBar.getRating();
        String reviewDescription = reviewText.getText().toString().trim();

        Review review = new Review(service.roadside_assistant_username, service.customer_username, rating, reviewDescription);

        database.reviewDao().addReview(review);
        database.roadsideAssistantDao().updateRating(service.roadside_assistant_username);
        customer.addReview(review);

        finish();
    }

    @Override
    public void finish() {
        Intent data = new Intent();
        data.putExtra("Customer", customer);
        setResult(RESULT_OK, data);
        super.finish();
    }
}
