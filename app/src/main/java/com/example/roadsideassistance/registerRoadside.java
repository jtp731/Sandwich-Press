package com.example.roadsideassistance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class registerRoadside extends AppCompatActivity {
    boolean canTow = false;
    Person person;
    AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_roadside);
        Intent intent = getIntent();
        person = intent.getParcelableExtra("Person");
        database = AppDatabase.getDatabase(getApplicationContext());

        findViewById(R.id.licenceError).setVisibility(View.GONE);
        findViewById(R.id.companyNameError).setVisibility(View.GONE);
        findViewById(R.id.ABNError).setVisibility(View.GONE);

        Switch towable = findViewById(R.id.towToggle);
        towable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    canTow = true;
                }
                else {
                    canTow = false;
                }
            }
        });
    }

    public void finishRegister(View view) {
        if(verify()) {
            TextView input = findViewById(R.id.newLicenceNumber);
            String licence = input.getText().toString().trim();

            input = findViewById(R.id.newCompanyName);
            String companyName = input.getText().toString().trim();

            input = findViewById(R.id.newABN);
            String abnStr = input.getText().toString().trim();
            long abn = Long.parseLong(abnStr);

            RoadsideAssistant roadsideAssistant = new RoadsideAssistant(person, licence, companyName, abn, canTow, 0.0f);
            database.userDao().addRoadsideAssistant(roadsideAssistant);
            finish();
        }
    }

    boolean verify() {
        boolean good = true;


        TextView licenceError = findViewById(R.id.licenceError);
        TextView companyError = findViewById(R.id.companyNameError);
        TextView abnError = findViewById(R.id.ABNError);

        licenceError.setVisibility(View.GONE);
        companyError.setVisibility(View.GONE);
        abnError.setVisibility(View.GONE);

        String licencePattern = "^MV(TC)|(RL)[0-9]{5}$";
        String abnPattern = "^[0-9]{11}$";

        TextView input = findViewById(R.id.newLicenceNumber);
        String licence = input.getText().toString().trim();

        input = findViewById(R.id.newCompanyName);
        String companyName = input.getText().toString().trim();

        input = findViewById(R.id.newABN);
        String abnStr = input.getText().toString().trim();

        Pattern pattern = Pattern.compile(licencePattern);
        Matcher matcher = pattern.matcher(licence);
        if(!matcher.matches()) {
            good = false;
            licenceError.setText("Licence number must be in MVTC12345 or MVRL12345 format");
            licenceError.setVisibility(View.VISIBLE);
        }

        if(companyName.equals("")) {
            good = false;
            companyError.setText("Company Name must be filled");
            companyError.setVisibility(View.VISIBLE);
        }

        pattern = Pattern.compile(abnPattern);
        matcher = pattern.matcher(abnStr);
        if(!matcher.matches()) {
            good = false;
            abnError.setText("ABN is 11 digits");
            abnError.setVisibility(View.VISIBLE);
        }

        return good;
    }
}
