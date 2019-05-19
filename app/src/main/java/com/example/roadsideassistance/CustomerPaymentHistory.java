package com.example.roadsideassistance;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CustomerPaymentHistory extends AppCompatActivity {
    Customer customer;
    AppDatabase database;
    public Spinner months;
    public Spinner years;
    ArrayAdapter<CharSequence> monthsAdapter;
    ArrayAdapter<String> yearsAdapters;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_payment_history);
        //Remove this after testing
        this.deleteDatabase("appdatabase");

        database = AppDatabase.getDatabase(this);
        //customer = getIntent().getParcelableExtra("Customer");
        //Start test data
        customer = new Customer("cust1",
                "123",
                "" + (int)(Math.random()*1e8),
                "cust1@email",
                "cust1FirstName",
                "cust1LastName");
        database.userDao().addCustomer(customer);
        database.userDao().addRoadsideAssistant(new RoadsideAssistant(
                "road1",
                "123",
                "" + (int)(Math.random()*1e8),
                "road1@email",
                "road1FirstName",
                "road1LastName",
                "MVTC" + (int)(Math.random()*1e5),
                "Comany1",
                (long)(Math.random()*1e11),
                (Math.random() > 0.5 ? true : false),
                0.0f));
        for(int j = 0; j < 3; j++) {
            database.carDao().addCar(new Car(
                    customer.username,
                    "" + (int)(Math.random()*1e6),
                    "Model" + (j + 1),
                    "Manufacturer" + (j + 1),
                    "Colour" + (j + 1),
                    (int)Math.random()*4,
                    new Date()
            ));
        }

        for(int i = 0; i < 10; i++) {
            customer.cars = database.customerDao().getAllCustomerCars(customer.username);
            Date date = new Date();
            date.setTime(date.getTime() - (long)(i*1e10));
            database.serviceDao().addService(new Service(
                    "road1",
                    customer.username,
                    customer.cars.get((int)(Math.random()*3)).plateNum,
                    -33.8688 ,//+ Math.random(),
                    151.2093 ,//+ Math.random(),
                    date,
                    (float)(50 * Math.random() + 100),
                    Service.PAYED_WITH_CARD,
                    (byte)0,
                    ""
            ));
        }

        for(int i = 0; i < 10; i++) {
            customer.cars = database.customerDao().getAllCustomerCars(customer.username);
            Date date = new Date();
            date.setTime(date.getTime() - (long)(i*1e10));
            database.customerDao().addSubPayment(new SubscriptionPayment(
                    "cust1",
                    customer.cars.get((int)(Math.random()*3)).plateNum,
                    date,
                    50));
        }
        //End Test Data

        months = findViewById(R.id.paymentHistoryMonth);
        years = findViewById(R.id.paymentHistoryYear);
        monthsAdapter = ArrayAdapter.createFromResource(this, R.array.months, android.R.layout.simple_spinner_dropdown_item);
        Date earliestServiceDate = database.serviceDao().getEarliestFinishedServiceCustomer(customer.username);
        Date earliestSubDate = database.customerDao().getEarliestSubPayment(customer.username);
        Date earliestDate = earliestServiceDate.before(earliestSubDate) ? earliestServiceDate : earliestSubDate;
        Toast.makeText(this, "" + earliestDate.toString(), Toast.LENGTH_LONG).show();
        Date currDate = new Date();
        ArrayList<String> yearsInString = new ArrayList<>();
        if(earliestDate != null && currDate.getYear() != earliestDate.getYear()) {
            yearsInString.add("" + (currDate.getYear() + 1900));
            for (int i = currDate.getYear() - 1; i >= earliestDate.getYear(); i--) {
                yearsInString.add("" + (i + 1900));
            }
        }
        else {
            yearsInString.add("" + (currDate.getYear() + 1900));
        }
        yearsAdapters = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, yearsInString);

        months.setAdapter(monthsAdapter);
        years.setAdapter(yearsAdapters);

        months.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                fillLayout(position, Integer.parseInt((String)years.getSelectedItem()));
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

        View costLabel = findViewById(R.id.costLabel);
        costLabel.post(new Runnable() {
            @Override
            public void run() {
                Date currDate = new Date();
                fillLayout(currDate.getMonth(), currDate.getYear());
            }
        });
    }

    private void fillLayout(int month, int year) {
        Date firstOfMonth = new Date(year-1900, month, 1);
        Date lastOfMonth = new Date(year-1900, month, 31);
        List<Service> servicesInMonth = database.serviceDao().getServicesInMonthCustomer(customer.username, firstOfMonth, lastOfMonth);
        List<SubscriptionPayment> subPaymentsInMonth = database.customerDao().getSubPaymentsInMonth(customer.username, firstOfMonth, lastOfMonth);

        float total = 0;

        LinearLayout serviceListLayout = findViewById(R.id.paymentHistoryLayout);
        serviceListLayout.removeViews(0, serviceListLayout.getChildCount());
        if((servicesInMonth != null && servicesInMonth.size() > 0) || (subPaymentsInMonth != null && subPaymentsInMonth.size() > 0)) {
            //Toast.makeText(this, "here", Toast.LENGTH_LONG).show();
            int servicesIndex = 0;
            int subsIndex = 0;
            for(int i = 0; i < (servicesInMonth.size() + subPaymentsInMonth.size()); i++) {
                LinearLayout outerLayout = new LinearLayout(this);
                outerLayout.setOrientation(LinearLayout.HORIZONTAL);
                outerLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));

                if(servicesIndex >= servicesInMonth.size()) {
                    //fill out rest of layout with sub payments
                    total += subPaymentsInMonth.get(subsIndex).amount;
                    TextView typeText = new TextView(this);
                    typeText.setText("Sub");
                    typeText.setWidth(findViewById(R.id.typeLabel).getWidth());
                    typeText.setBackground(getResources().getDrawable(R.drawable.border_sharp));
                    typeText.setPadding(5,5,5,5);
                    outerLayout.addView(typeText);

                    TextView dateText = new TextView(this);
                    dateText.setText(String.format("%d/%d/%d",
                            subPaymentsInMonth.get(subsIndex).time.getDate(),
                            subPaymentsInMonth.get(subsIndex).time.getMonth() + 1,
                            subPaymentsInMonth.get(subsIndex).time.getYear() + 1900));
                    dateText.setWidth(findViewById(R.id.dateLabel).getWidth());
                    dateText.setBackground(getResources().getDrawable(R.drawable.border_sharp));
                    dateText.setPadding(5,5,5,5);
                    outerLayout.addView(dateText);

                    TextView costText = new TextView(this);
                    costText.setText(String.format("$%.2f", subPaymentsInMonth.get(subsIndex).amount));
                    costText.setWidth(findViewById(R.id.costLabel).getWidth());
                    costText.setBackground(getResources().getDrawable(R.drawable.border_sharp));
                    costText.setPadding(5,5,5,5);
                    outerLayout.addView(costText);

                    /*
                    TextView sub = new TextView(this);
                    sub.setText(String.format("Sub Date: %d/%d/%d Cost: $%.2f",
                            subPaymentsInMonth.get(subsIndex).time.getDate(),
                            subPaymentsInMonth.get(subsIndex).time.getMonth(),
                            subPaymentsInMonth.get(subsIndex).time.getYear() + 1900,
                            subPaymentsInMonth.get(subsIndex).amount));
                    serviceListLayout.addView(sub);
                    */
                    subsIndex++;
                }
                else if(subsIndex >= subPaymentsInMonth.size()) {
                    //fill out res of layout with service payments
                    total += servicesInMonth.get(servicesIndex).cost;
                    TextView typeText = new TextView(this);
                    typeText.setText("Service");
                    typeText.setWidth(findViewById(R.id.typeLabel).getWidth());
                    typeText.setBackground(getResources().getDrawable(R.drawable.border_sharp));
                    typeText.setPadding(5,5,5,5);
                    outerLayout.addView(typeText);

                    TextView dateText = new TextView(this);
                    dateText.setText(String.format("%d/%d/%d",
                            servicesInMonth.get(servicesIndex).time.getDate(),
                            servicesInMonth.get(servicesIndex).time.getMonth() + 1,
                            servicesInMonth.get(servicesIndex).time.getYear() + 1900));
                    dateText.setWidth(findViewById(R.id.dateLabel).getWidth());
                    dateText.setBackground(getResources().getDrawable(R.drawable.border_sharp));
                    dateText.setPadding(5,5,5,5);
                    outerLayout.addView(dateText);

                    TextView costText = new TextView(this);
                    costText.setText(String.format("$%.2f", servicesInMonth.get(servicesIndex).cost));
                    costText.setWidth(findViewById(R.id.costLabel).getWidth());
                    costText.setBackground(getResources().getDrawable(R.drawable.border_sharp));
                    costText.setPadding(5,5,5,5);
                    outerLayout.addView(costText);

                    /*
                    TextView service = new TextView(this);
                    service.setText(String.format("Service Date: %d/%d/%d  Cost: $%.2f",
                            servicesInMonth.get(servicesIndex).time.getDate(),
                            servicesInMonth.get(servicesIndex).time.getMonth(),
                            servicesInMonth.get(servicesIndex).time.getYear() + 1900,
                            servicesInMonth.get(servicesIndex).cost));
                    serviceListLayout.addView(service);
                    */
                    servicesIndex++;
                }
                else if(servicesInMonth.get(servicesIndex).time.after(subPaymentsInMonth.get(subsIndex).time)) {
                    total += servicesInMonth.get(servicesIndex).cost;
                    TextView typeText = new TextView(this);
                    typeText.setText("Service");
                    typeText.setWidth(findViewById(R.id.typeLabel).getWidth());
                    typeText.setBackground(getResources().getDrawable(R.drawable.border_sharp));
                    typeText.setPadding(5,5,5,5);
                    outerLayout.addView(typeText);

                    TextView dateText = new TextView(this);
                    dateText.setText(String.format("%d/%d/%d",
                            servicesInMonth.get(servicesIndex).time.getDate(),
                            servicesInMonth.get(servicesIndex).time.getMonth() + 1,
                            servicesInMonth.get(servicesIndex).time.getYear() + 1900));
                    dateText.setWidth(findViewById(R.id.dateLabel).getWidth());
                    dateText.setBackground(getResources().getDrawable(R.drawable.border_sharp));
                    dateText.setPadding(5,5,5,5);
                    outerLayout.addView(dateText);

                    TextView costText = new TextView(this);
                    costText.setText(String.format("$%.2f", servicesInMonth.get(servicesIndex).cost));
                    costText.setWidth(findViewById(R.id.costLabel).getWidth());
                    costText.setBackground(getResources().getDrawable(R.drawable.border_sharp));
                    costText.setPadding(5,5,5,5);
                    outerLayout.addView(costText);

                    /*
                    TextView service = new TextView(this);
                    service.setText(String.format("Service Date: %d/%d/%d  Cost: $%.2f",
                            servicesInMonth.get(servicesIndex).time.getDate(),
                            servicesInMonth.get(servicesIndex).time.getMonth(),
                            servicesInMonth.get(servicesIndex).time.getYear() + 1900,
                            servicesInMonth.get(servicesIndex).cost));
                    serviceListLayout.addView(service);
                    */
                    servicesIndex++;
                }
                else {
                    total += subPaymentsInMonth.get(subsIndex).amount;
                    TextView typeText = new TextView(this);
                    typeText.setText("Sub");
                    typeText.setWidth(findViewById(R.id.typeLabel).getWidth());
                    typeText.setBackground(getResources().getDrawable(R.drawable.border_sharp));
                    typeText.setPadding(5,5,5,5);
                    outerLayout.addView(typeText);

                    TextView dateText = new TextView(this);
                    dateText.setText(String.format("%d/%d/%d",
                            subPaymentsInMonth.get(subsIndex).time.getDate(),
                            subPaymentsInMonth.get(subsIndex).time.getMonth() + 1,
                            subPaymentsInMonth.get(subsIndex).time.getYear() + 1900));
                    dateText.setWidth(findViewById(R.id.dateLabel).getWidth());
                    dateText.setBackground(getResources().getDrawable(R.drawable.border_sharp));
                    dateText.setPadding(5,5,5,5);
                    outerLayout.addView(dateText);

                    TextView costText = new TextView(this);
                    costText.setText(String.format("$%.2f", subPaymentsInMonth.get(subsIndex).amount));
                    costText.setWidth(findViewById(R.id.costLabel).getWidth());
                    costText.setBackground(getResources().getDrawable(R.drawable.border_sharp));
                    costText.setPadding(5,5,5,5);
                    outerLayout.addView(costText);

                    /*
                    TextView sub = new TextView(this);
                    sub.setText(String.format("Sub Date: %d/%d/%d Cost: $%.2f",
                            subPaymentsInMonth.get(subsIndex).time.getDate(),
                            subPaymentsInMonth.get(subsIndex).time.getMonth(),
                            subPaymentsInMonth.get(subsIndex).time.getYear() + 1900,
                            subPaymentsInMonth.get(subsIndex).amount));
                    serviceListLayout.addView(sub);
                    */
                    subsIndex++;
                }
                serviceListLayout.addView(outerLayout);
            }

            TextView totalText = findViewById(R.id.paymentHistoryTotal);
            totalText.setText(String.format("Total: -$%.2f", total));
            totalText.setTextColor(getResources().getColor(R.color.negativePayText));
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