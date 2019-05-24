package com.example.roadsideassistance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class CustomerAddCar extends AppCompatActivity {

    Customer customer;
    AppDatabase database;
    RadioButton free;
    RadioButton oneMonth;
    RadioButton sixMonths;
    RadioButton twelveMonths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_add_car);
        database = AppDatabase.getDatabase(getApplicationContext());
        findViewById(R.id.makeError).setVisibility(View.GONE);
        findViewById(R.id.modelError).setVisibility(View.GONE);
        findViewById(R.id.plateError).setVisibility(View.GONE);
        findViewById(R.id.colourError).setVisibility(View.GONE);

        customer = getIntent().getParcelableExtra("Customer");


    }

    public void AddCar(View view){
        String username = null;
        Boolean empty = false;
        String make = null;
        String model = null;
        String colour = null;
        String plate = null;
        Date renewalDate = new Date();
        customer = getIntent().getParcelableExtra("Customer");

        Bundle extra = getIntent().getExtras();
        if (extra != null){
            username = customer.username;
        }

        EditText input = findViewById(R.id.carMake);
        if (!input.getText().toString().trim().isEmpty()) {
            make = input.getText().toString();
            findViewById(R.id.makeError).setVisibility(View.GONE);
        } else {
            empty = true;
            findViewById(R.id.makeError).setVisibility(View.VISIBLE);
        }

        input = findViewById(R.id.carModel);
        if (!input.getText().toString().trim().isEmpty()) {
            model = input.getText().toString();
            findViewById(R.id.modelError).setVisibility(View.GONE);
        } else {
            empty = true;
            findViewById(R.id.modelError).setVisibility(View.VISIBLE);
        }

        input = findViewById(R.id.carColour);
        if (!input.getText().toString().trim().isEmpty()) {
            colour = input.getText().toString();
            findViewById(R.id.colourError).setVisibility(View.GONE);
        } else {
            empty = true;
            findViewById(R.id.colourError).setVisibility(View.VISIBLE);
        }

        input = findViewById(R.id.plateNum);
        if (!input.getText().toString().trim().isEmpty()) {
            plate = input.getText().toString();
            findViewById(R.id.plateError).setVisibility(View.GONE);
        } else {
            empty = true;
            findViewById(R.id.plateError).setVisibility(View.VISIBLE);
        }

        free = findViewById(R.id.radioFree);
 //       oneMonth = findViewById(R.id.radioOne);
 //       sixMonths = findViewById(R.id.radioSix);
        twelveMonths = findViewById(R.id.radioTwelve);
        Calendar calendar = Calendar.getInstance();
        int subType = Car.FREE_SUB;

        if (free.isChecked()){
            renewalDate = new Date(0);
/*        } else if (oneMonth.isChecked()){
            calendar.setTime(new Date());
            calendar.add(Calendar.MONTH, 1);
            renewalDate = calendar.getTime();
            subType = Car.ONE_MONTH_SUB;
        } else if (sixMonths.isChecked()){
            calendar.setTime(new Date());
            calendar.add(Calendar.MONTH, 6);
            renewalDate = calendar.getTime();
            subType = Car.SIX_MONTH_SUB;
*/        } else if (twelveMonths.isChecked()){
            calendar.setTime(new Date());
            calendar.add(Calendar.MONTH, 12);
            renewalDate = calendar.getTime();
            subType = Car.ONE_YEAR_SUB;
        }

        if (empty == false) {
            Car car = new Car(username, plate, model, make, colour, subType, renewalDate);
            database.carDao().addCar(car);
            customer.cars.add(car);
            Toast.makeText(this, "Successfully added car", Toast.LENGTH_LONG).show();

            finish();
        }
    }

    @Override
    public void finish() {
        Intent data = new Intent();
        data.putExtra("Customer", customer);
        setResult(RESULT_OK, data);
        super.finish();
    }
}