package com.example.roadsideassistance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;
import java.util.Vector;

public class CustomerUpdateSubscription extends AppCompatActivity {

    Customer customer;
    Car car;
    ArrayAdapter<String> carStrings;
    Spinner carSpinner;
    TextView sub;
    RadioButton one, six, twelve;
    Date today = new Date();
    Button updateSub;
    String dateOutput = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_subscription);

        customer = getIntent().getParcelableExtra("Customer");
        sub = findViewById(R.id.expiryDate);
        carSpinner = findViewById(R.id.carSpinner);
        one = findViewById(R.id.radioOne);
        six = findViewById(R.id.radioSix);
        twelve = findViewById(R.id.radioTwelve);
        updateSub = findViewById(R.id.update);

        if(customer.cars.size() > 0) {
            Car currCar;
            Vector<String> carsAsString = new Vector<>();
            for (int i = 0; i < customer.cars.size(); i++) {
                currCar = customer.cars.get(i);
                carsAsString.add(currCar.manufacturer + " " + currCar.model + " " + currCar.plateNum);
            }
            carStrings = new ArrayAdapter<String>(this, R.layout.activity_spinner, carsAsString);
            carSpinner.setAdapter(carStrings);
        } else {
            //Error
            Vector<String> errorArray = new Vector<>();
            errorArray.add("No Cars Available");
            carStrings = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, errorArray);
            carSpinner.setAdapter(carStrings);
        }

        carSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                car = customer.cars.get(carSpinner.getSelectedItemPosition());
                if (car.renewalDate.before(today)){
                    dateOutput = "Not subscribed";
                } else if (car.renewalDate.after(today)){
                    dateOutput = DateFormat.format("dd-MM-yyyy", car.renewalDate).toString();
                }
                sub.setText(dateOutput);

                updateSub.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (car.renewalDate.before(today)){
                            Date renewalDate;
                            Calendar calendar = Calendar.getInstance();
                            if (one.isChecked()){
                                calendar.add(Calendar.MONTH, 1);
                                renewalDate = calendar.getTime();
                                car.renewalDate = renewalDate;
                            } else if (six.isChecked()) {
                                calendar.add(Calendar.MONTH, 6);
                                renewalDate = calendar.getTime();
                                car.renewalDate = renewalDate;
                            } else if (twelve.isChecked()){
                                calendar.add(Calendar.MONTH, 12);
                                renewalDate = calendar.getTime();
                                car.renewalDate = renewalDate;
                            }
                            dateOutput = DateFormat.format("dd-MM-yyyy", car.renewalDate).toString();
                            sub.setText(dateOutput);
                            finish();
                        }
                    }
                });
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void finish(){
        Intent data = new Intent();
        data.putExtra("Customer", customer);
        setResult(RESULT_OK, data);
        super.finish();
    }
}
