package com.example.roadsideassistance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

public class ManagerSelectUserForReviews extends AppCompatActivity {
    Manager manager;
    int selectedIndex = -1;
    List<Person> people;
    AppDatabase database;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_reviews_select_user);

        //manager = getIntent().getParcelableExtra("Manager");
        //this.deleteDatabase("appdatabase");
        database = AppDatabase.getDatabase(this);
        //TestData.createTestData(this, 10, 10, 3, 0);

        RadioButton customersOnlyRadioButton = findViewById(R.id.managerCustomersOnlyRadio);
        RadioButton roadsideOnlyRadioButton = findViewById(R.id.managerRoadsideOnlyRadio);
        RadioButton bothRadioButton = findViewById(R.id.managerBothRadio);

        customersOnlyRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setList(0);
            }
        });

        roadsideOnlyRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setList(1);
            }
        });

        bothRadioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setList(2);
            }
        });

        final View avgRatingView = findViewById(R.id.AvgReviews);
        avgRatingView.post(new Runnable() {
            @Override
            public void run() {
                setList(2);
            }
        });
        //setList(2);
    }

    private void setList(int type) {
        if(type == 0) {
            people = database.managerDao().getAllCustomers();
        }
        if(type == 1) {
            people = database.managerDao().getAllRoadsideAssistants();
        }
        if(type == 2) {
            people = database.managerDao().getAllCustomersAndRoadside();
        }

        final LinearLayout peopleLayout = findViewById(R.id.managerSelectUserLayout);
        if(peopleLayout.getChildCount() > 0)
            peopleLayout.removeViews(0, peopleLayout.getChildCount());
        findViewById(R.id.managerSelectUserButton).setVisibility(View.VISIBLE);
        if(people.size() > 0) {
            //Toast.makeText(this, "Greater than 0", Toast.LENGTH_LONG).show();
            for (int i = 0; i < people.size(); i++) {
                final int currIndex = i;
                final LinearLayout personLayout = new LinearLayout(this);
                personLayout.setOrientation(LinearLayout.HORIZONTAL);
                personLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                personLayout.setPadding(0, 0, 0, 0);

                Person person = people.get(i);

                TextView typeText = new TextView(this);
                //Toast.makeText(this, "Width: " + findViewById(R.id.userType).getWidth(), Toast.LENGTH_LONG).show();
                if(findViewById(R.id.userType).getWidth() == 0)
                    typeText.setWidth(170);
                else
                    typeText.setWidth(findViewById(R.id.userType).getWidth());
                typeText.setPadding(0,0,0,0);
                if(database.customerDao().customerExists(person.email))
                    typeText.setText("Customer");
                else
                    typeText.setText("Roadside");
                typeText.setBackground(getResources().getDrawable(R.drawable.border_sharp));
                personLayout.addView(typeText);

                TextView usernameText = new TextView(this);
                if(findViewById(R.id.Username).getWidth() == 0)
                    usernameText.setWidth(250);
                else
                    usernameText.setWidth(findViewById(R.id.Username).getWidth());
                usernameText.setPadding(0,0,0,0);
                usernameText.setText(person.username);
                usernameText.setBackground(getResources().getDrawable(R.drawable.border_sharp));
                personLayout.addView(usernameText);

                TextView numOfReviewText = new TextView(this);
                if(findViewById(R.id.NoOfReviews).getWidth() == 0)
                    numOfReviewText.setWidth(300);
                else
                    numOfReviewText.setWidth(findViewById(R.id.NoOfReviews).getWidth());
                numOfReviewText.setPadding(3,0,0,0);
                numOfReviewText.setText("" + database.reviewDao().getReviewCount(person.username));
                numOfReviewText.setBackground(getResources().getDrawable(R.drawable.border_sharp));
                personLayout.addView(numOfReviewText);

                TextView avgRatingText = new TextView(this);
                if(findViewById(R.id.AvgReviews).getWidth() == 0)
                    avgRatingText.setWidth(170);
                else
                    avgRatingText.setWidth(findViewById(R.id.AvgReviews).getWidth());
                avgRatingText.setPadding(3,0,0,0);
                avgRatingText.setText("" + database.reviewDao().getAvgRating(person.username));
                avgRatingText.setBackground(getResources().getDrawable(R.drawable.border_sharp));
                personLayout.addView(avgRatingText);

                personLayout.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedIndex = currIndex;
                        for(int j = 0; j < peopleLayout.getChildCount(); j++) {
                            peopleLayout.getChildAt(j).setBackgroundColor(getResources().getColor(R.color.unselectedListItem));
                        }
                        personLayout.setBackgroundColor(getResources().getColor(R.color.selectedListItem));
                    }
                });

                peopleLayout.addView(personLayout);
                //Toast.makeText(this, "Added view", Toast.LENGTH_LONG).show();
                /*
                final TextView personText = new TextView(this);
                Person person = people.get(i);
                if(database.customerDao().customerExists(person.email))
                    personText.setText("Customer| ");
                else
                    personText.setText("Roadside| ");
                personText.setText(personText.getText().toString()
                        + "Username: " + person.username
                        + " | No. of Reviews: " + database.reviewDao().getReviewCount(person.username)
                        + " | Avg Rating: " + database.reviewDao().getAvgRating(person.username));
                personText.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectedIndex = currIndex;
                        for(int j = 0; j < peopleLayout.getChildCount(); j++) {
                            peopleLayout.getChildAt(j).setBackgroundColor(getResources().getColor(R.color.unselectedListItem));
                        }
                        personText.setBackgroundColor(getResources().getColor(R.color.selectedListItem));
                    }
                });
                peopleLayout.addView(personText);
                */
            }
        }
        else {
            TextView noUsersText = new TextView(this);
            noUsersText.setText("No Users Exist");
            peopleLayout.addView(noUsersText);
            findViewById(R.id.managerSelectUserButton).setVisibility(View.GONE);
        }
    }

    public void toSelectedUser() {
        if(selectedIndex >= 0) {
            Intent intent = new Intent(this, managerSelectedUserReviews.class);
            intent.putExtra("Manager", manager);
            intent.putExtra("Person", people.get(selectedIndex));
            startActivity(intent);
        }
        else
            Toast.makeText(this, "No User Selected", Toast.LENGTH_LONG).show();
    }
}
