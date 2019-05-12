package com.example.roadsideassistance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Layout;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class RoadsidePaymentHistory extends AppCompatActivity {
    RoadsideAssistant roadsideAssistant;
    AppDatabase database;
    Spinner months;
    Spinner years;
    ArrayAdapter<CharSequence> monthsAdapter;
    ArrayAdapter<String> yearsAdapters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_history);
        database = AppDatabase.getDatabase(this);
        roadsideAssistant = getIntent().getParcelableExtra("Roadside");

        months = findViewById(R.id.paymentHistoryMonth);
        years = findViewById(R.id.paymentHistoryYear);

        monthsAdapter = ArrayAdapter.createFromResource(this, R.array.months, android.R.layout.simple_spinner_dropdown_item);
        Date earliestDate = database.serviceDao().getEarliestFinishedServiceRoadside(roadsideAssistant.username);
        Date currDate = new Date();
        ArrayList<String> yearsInString = new ArrayList<>();
        if(earliestDate != null) {
            for (int i = currDate.getYear(); i >= earliestDate.getYear(); i--)
                yearsInString.add("" + (i + 1900));
        }
        else
            yearsInString.add("" + (currDate.getYear() + 1900));
        yearsAdapters = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, yearsInString);

        months.setAdapter(monthsAdapter);
        years.setAdapter(yearsAdapters);

        fillLayout(currDate.getMonth(), currDate.getYear());

        months.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fillLayout(position, Integer.parseInt(years.getSelectedItem().toString()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        years.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fillLayout(months.getSelectedItemPosition(), Integer.parseInt(years.getItemAtPosition(position).toString()));
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void fillLayout(int month, int year) {
        Date firstOfMonth = new Date(year, month, 1);
        Date lastOfMonth = new Date(year, month, 31);
        List<Service> servicesInMonth = database.serviceDao().getServicesInMonthRoadside(roadsideAssistant.username, firstOfMonth, lastOfMonth);

        float total = 0;

        LinearLayout serviceListLayout = findViewById(R.id.paymentHistoryLayout);
        serviceListLayout.removeViews(0, serviceListLayout.getChildCount());
        if(servicesInMonth != null && servicesInMonth.size() > 0) {
            for(int i = 0; i < servicesInMonth.size(); i++) {
                total += servicesInMonth.get(i).costToPay();
                TextView service = new TextView(this);
                service.setText(String.format("Date: %d/%d/%d     Cost: $%.2f",
                        servicesInMonth.get(i).time.getDate(),
                        servicesInMonth.get(i).time.getMonth(),
                        servicesInMonth.get(i).time.getYear() + 1900,
                        servicesInMonth.get(i).costToPay()));
                serviceListLayout.addView(service);
            }

            TextView totalText = findViewById(R.id.paymentHistoryTotal);
            totalText.setText(String.format("Total: $%.2f", total));
            totalText.setTextColor(getResources().getColor(R.color.positivePayText));
            totalText.setVisibility(View.VISIBLE);
        }
        else{
            TextView noHistoryText = new TextView(this);
            noHistoryText.setText("No Services In This Month");
            serviceListLayout.addView(noHistoryText);
            TextView totalText = findViewById(R.id.paymentHistoryTotal);
            totalText.setVisibility(View.INVISIBLE);
        }
    }
}