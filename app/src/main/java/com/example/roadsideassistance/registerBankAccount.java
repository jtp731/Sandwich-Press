package com.example.roadsideassistance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.TextView;

import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class registerBankAccount extends AppCompatActivity {
    static final int CUSTOMER = 0;
    static final int ROADSIDE_ASSISTANT = 1;
    static int currentPersonType = 0;

    Person person;
    AppDatabase database;
    Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_bank_account);
        Intent intent = getIntent();
        person = intent.getParcelableExtra("Person");
        database = AppDatabase.getDatabase(getApplicationContext());

        findViewById(R.id.errorCardNum).setVisibility(View.GONE);
        findViewById(R.id.errorExpiryDate).setVisibility(View.GONE);

        if (getIntent().getParcelableExtra("Customer") != null){
            customer = getIntent().getParcelableExtra("Customer");
            setToCustomer(null);

            EditText bankNum = findViewById(R.id.newBankNum);
            bankNum.setText(Long.toString(customer.bankAccount.cardNum));

            EditText month = findViewById(R.id.newExpiryDateMonth);
            EditText year = findViewById(R.id.newExpiryDateYear);
            Date expiry = customer.bankAccount.expiryDate;
            Integer expMonth = expiry.getMonth() + 1;
            String monthExpiry;
            if (expMonth < 10){
                monthExpiry = 0 + Integer.toString(expMonth);
            } else {
                monthExpiry = Integer.toString(expMonth);
            }
            String expYear = Integer.toString(expiry.getYear() + 1900 - 2000);
            month.setText(monthExpiry);
            year.setText(expYear);
            findViewById(R.id.radioGroup).setVisibility(View.GONE);
        }
    }

    public void onClick(View view) {
        if(verify()) {
            EditText input = findViewById(R.id.newBankNum);
            long cardNum = Long.parseLong(input.getText().toString().trim());

            input = findViewById(R.id.newExpiryDateMonth);
            int month = Integer.parseInt(input.getText().toString().trim());

            input = findViewById(R.id.newExpiryDateYear);
            int year = Integer.parseInt(input.getText().toString().trim());

            Date currDate = new Date();
            int currYear = currDate.getYear() + 1900;
            currYear /= 100;
            currYear *= 100;
            year = (currYear + year) - 1900;

            Date expiryDate = new Date(year, month-1, 1);
            if (currentPersonType == CUSTOMER) {
                if (getIntent().getParcelableExtra("Customer") != null){
                    customer.bankAccount.expiryDate = expiryDate;
                    customer.bankAccount.cardNum = cardNum;

                    finish();
                } else {
                    person.bankAccount = new BankAccount(cardNum, expiryDate);
                    customer = new Customer(person);
                    database.userDao().addCustomer(customer);
                    Intent intent = new Intent(this, CustomerAddCar.class);
                    intent.putExtra("Customer", customer);
                    startActivity(intent);
                    super.finish();
                }
            }

            if (currentPersonType == ROADSIDE_ASSISTANT) {
                //go to roadside page
                Intent roadsideIntent = new Intent(registerBankAccount.this, registerRoadside.class);
                roadsideIntent.putExtra("Person", person);
                startActivity(roadsideIntent);
                super.finish();
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
        String expiryDateMonthPattern = "^[0-1][0-9]$";
        String expiryDateYearPattern = "^[0-9]{2}$";

        EditText input = findViewById(R.id.newBankNum);
        String bankNum = input.getText().toString().trim();

        input = findViewById(R.id.newExpiryDateMonth);
        String expiryDateMonth = input.getText().toString().trim();

        input = findViewById(R.id.newExpiryDateYear);
        String expiryDateYear = input.getText().toString().trim();

        Pattern pattern = Pattern.compile(bankNumPattern);
        Matcher matcher = pattern.matcher(bankNum);
        if (!matcher.matches()) {
            good = false;
            errorCardNum.setText("Invalid Card Number");
            errorCardNum.setVisibility(View.VISIBLE);
        }

        pattern = Pattern.compile(expiryDateMonthPattern);
        matcher = pattern.matcher(expiryDateMonth);
        if (!matcher.matches()) {
            good = false;
            errorExpiryDate.setText("Invalid Date");
            errorExpiryDate.setVisibility(View.VISIBLE);
        }
        else {
            input = findViewById(R.id.newExpiryDateMonth);
            int month = Integer.parseInt(input.getText().toString().trim());

            input = findViewById(R.id.newExpiryDateYear);
            int year = Integer.parseInt(input.getText().toString().trim());

            Date currDate = new Date();
            int currYear = currDate.getYear() + 1900;
            currYear /= 100;
            currYear *= 100;
            year = (currYear + year) - 1900;

            Date expiryDate = new Date(year, month-1, 1);
            if(expiryDate.before(currDate)) {
                good = false;
                errorExpiryDate.setText("Card Already Expired");
                errorExpiryDate.setVisibility(View.VISIBLE);
            }
        }

        pattern = Pattern.compile(expiryDateYearPattern);
        matcher = pattern.matcher(expiryDateYear);
        if (!matcher.matches()) {
            good = false;
            errorExpiryDate.setText("Invalid Date");
            errorExpiryDate.setVisibility(View.VISIBLE);
        }

        return good;
    }

    @Override
    public void finish(){
        Intent data = new Intent();
        data.putExtra("Customer", customer);
        setResult(RESULT_OK, data);
        super.finish();
    }
}
