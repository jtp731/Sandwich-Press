package com.example.roadsideassistance;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

public class managerSelectedUserReviews extends AppCompatActivity {
    Person person;
    Customer customer;
    RoadsideAssistant roadsideAssistant;
    AppDatabase database;
    List<Review> reviews;
    boolean isCustomer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_selected_reviewer);

        person = getIntent().getParcelableExtra("Person");
        database = AppDatabase.getDatabase(this);

        TextView typeLabel = findViewById(R.id.userTypeLabel);
        if(database.customerDao().customerExists(person.email)) {
            reviews = database.customerDao().getCustomer(person.email).reviews;
            typeLabel.setText("Customer");
            isCustomer = true;
        } else {
            reviews = database.roadsideAssistantDao().getRoadsideAssistantByUsername(person.username).reviews;
            typeLabel.setText("Roadside Assistant");
            isCustomer = false;
        }

        final TextView ratingView = findViewById(R.id.ratingLabel);
        ratingView.post(new Runnable() {
            @Override
            public void run() {
                createList();
            }
        });
    }

    void createList() {
        final LinearLayout reviewsLayout = findViewById(R.id.reviewsLayout);
        reviewsLayout.removeViews(0, reviewsLayout.getChildCount());
        if(reviews != null) {
            if(reviews.size() > 0) {
                final LinearLayout outerLayout = new LinearLayout(this);
                outerLayout.setOrientation(LinearLayout.VERTICAL);
                outerLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                for(int i = 0; i < reviews.size(); i++){
                    LinearLayout reviewInfoLayout = new LinearLayout(this);
                    reviewInfoLayout.setOrientation(LinearLayout.HORIZONTAL);
                    reviewInfoLayout.setLayoutParams(new TableLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                    final int currIndex = i;
                    TextView usernameText = new TextView(this);
                    usernameText.setText(isCustomer ? reviews.get(i).roadside_assistant_username : reviews.get(i).customer_username);
                    usernameText.setPadding(5,5,5,5);
                    usernameText.setBackground(getResources().getDrawable(R.drawable.border_sharp));
                    usernameText.setWidth(findViewById(R.id.usernameLabel).getWidth());
                    reviewInfoLayout.addView(usernameText);

                    TextView ratingText = new TextView(this);
                    ratingText.setText("rating in stars here");
                    ratingText.setPadding(5,5,5,5);
                    ratingText.setBackground(getResources().getDrawable(R.drawable.border_sharp));
                    ratingText.setWidth(findViewById(R.id.ratingLabel).getWidth());
                    reviewInfoLayout.addView(ratingText);

                    outerLayout.addView(reviewInfoLayout);

                    TextView descriptionText = new TextView(this);
                    ratingText.setText(reviews.get(i).reviewText);
                    ratingText.setPadding(5,5,5,5);
                    ratingText.setBackground(getResources().getDrawable(R.drawable.border_sharp));
                    ratingText.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                    outerLayout.addView(descriptionText);

                    outerLayout.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            for (int j = 0; j < reviewsLayout.getChildCount(); j++) {
                                LinearLayout layout = (LinearLayout)reviewsLayout.getChildAt(j);
                                layout.getChildAt(2).setVisibility(View.INVISIBLE);
                            }
                            outerLayout.getChildAt(2).setVisibility(View.VISIBLE);
                        }
                    });

                    reviewsLayout.addView(outerLayout);
                }
            } else {
                TextView noReviews = new TextView(this);
                noReviews.setText("No Reviews for the user");
                reviewsLayout.addView(noReviews);
            }
        } else
            Toast.makeText(this, "Error getting reviews", Toast.LENGTH_LONG).show();
    }
}
