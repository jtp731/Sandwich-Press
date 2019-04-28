package com.example.roadsideassistance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class registerBankAccount extends AppCompatActivity {
    static final int CUSTOMER = 0;
    static final int ROADSIDE_ASSISTANT = 1;
    static int currentPersonType = 0;

    Person person;
    AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_bank_account);
        Intent intent = getIntent();
        person = intent.getParcelableExtra("Person");
        database = AppDatabase.getDatabase(getApplicationContext());

        findViewById(R.id.errorCardNum).setVisibility(View.GONE);
        findViewById(R.id.errorExpiryDate).setVisibility(View.GONE);
    }

    public void onClick(View view) {
        if(verify()) {
            if (currentPersonType == CUSTOMER) {
                database.personDao().addPerson(person);
                //return to login
                finish();
            }

            if (currentPersonType == ROADSIDE_ASSISTANT) {
                //go to roadside page
                Intent roadsideIntent = new Intent(registerBankAccount.this, registerRoadside.class);
                roadsideIntent.putExtra("Person", person);
                startActivity(roadsideIntent);
            }
        }
    }

    public void setToCustomer(View view) {
        currentPersonType = CUSTOMER;
        Button button = findViewById(R.id.bankNextButton);
        button.setText("Finish");
    }

    public void setToRoadsideAssistant(View view) {
        currentPersonType = ROADSIDE_ASSISTANT;
        Button button = findViewById(R.id.bankNextButton);
        button.setText("Next");
    }

    private boolean verify() {
        boolean good = true;

        TextView errorCardNum = findViewById(R.id.errorCardNum);
        TextView errorExpiryDate = findViewById(R.id.errorExpiryDate);

        errorCardNum.setVisibility(View.GONE);
        errorExpiryDate.setVisibility(View.GONE);

        String bankNumPattern = "^[0-9]{16}$";
        String expiryDatePattern = "^[0-9]{1,2}/[0-9]{1,2}$";

        EditText input = findViewById(R.id.newBankNum);
        String bankNum = input.getText().toString().trim();

        input = findViewById(R.id.newExpiryDate);
        String expiryDate = input.getText().toString().trim();

        Pattern pattern = Pattern.compile(bankNumPattern);
        Matcher matcher = pattern.matcher(bankNum);
        if (!matcher.matches()) {
            good = false;
            errorCardNum.setText("Invalid Card Number");
            errorCardNum.setVisibility(View.VISIBLE);
        }

        pattern = Pattern.compile(expiryDatePattern);
        matcher = pattern.matcher(expiryDate);
        if (!matcher.matches()) {
            good = false;
            errorExpiryDate.setText("Invalid Date");
            errorExpiryDate.setVisibility(View.VISIBLE);
        }

        return good;
    }
}