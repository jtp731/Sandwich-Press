package com.example.roadsideassistance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

public class registerAddress extends AppCompatActivity {
    Person person;
    Spinner states;
    ArrayAdapter<CharSequence> adapter;
    Customer customer;
    RoadsideAssistant roadsideAssistant;
    AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_address);
        states = findViewById(R.id.newState);
        adapter = ArrayAdapter.createFromResource(this, R.array.states, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        states.setAdapter(adapter);
        Intent intent = getIntent();
        person = intent.getParcelableExtra("Person");
        EditText output;
        database = AppDatabase.getDatabase(this);

        if (getIntent().getParcelableExtra("Customer") != null){
            customer = getIntent().getParcelableExtra("Customer");
            output = findViewById(R.id.newStreetNum);
            output.setText(Integer.toString(customer.address.streetNum));

            output = findViewById(R.id.newStreetName);
            output.setText(customer.address.street);

            output = findViewById(R.id.newCity);
            output.setText(customer.address.city);

            int position = adapter.getPosition(customer.address.state);
            states.setSelection(position);
        } else if (getIntent().getParcelableExtra("Roadside") != null){
            roadsideAssistant = getIntent().getParcelableExtra("Roadside");
            output = findViewById(R.id.newStreetNum);
            output.setText(roadsideAssistant.address.streetNum);

            output = findViewById(R.id.newStreetName);
            output.setText(roadsideAssistant.address.street);

            output = findViewById(R.id.newCity);
            output.setText(roadsideAssistant.address.city);


            int position = adapter.getPosition(roadsideAssistant.address.city);
            states.setSelection(position);
        }
    }

    public void nextButton(View view) {
        EditText input = findViewById(R.id.newStreetNum);
        int streetNum = Integer.parseInt(input.getText().toString());

        input = findViewById(R.id.newStreetName);
        String street = input.getText().toString();

        input = findViewById(R.id.newCity);
        String city = input.getText().toString();

        String state = states.getSelectedItem().toString();

        if (getIntent().getParcelableExtra("Customer") != null){
            customer = getIntent().getParcelableExtra("Customer");
            customer.address.streetNum = streetNum;
            customer.address.street = street;
            customer.address.city = city;
            customer.address.state = state;

            //update customer address in database
            database.userDao().updateCustomerAddress(customer);

            finish();
        } else if (getIntent().getParcelableExtra("Roadside") != null){
            roadsideAssistant = getIntent().getParcelableExtra("Roadside");
            roadsideAssistant.address.streetNum = streetNum;
            roadsideAssistant.address.street = street;
            roadsideAssistant.address.city = city;
            roadsideAssistant.address.state = state;

            finish();
        } else {
            person.address = new Address(streetNum, street, city, state);

            Intent bankAccountIntent = new Intent(registerAddress.this, registerBankAccount.class);
            bankAccountIntent.putExtra("Person", person);
            startActivity(bankAccountIntent);
            super.finish();
        }
    }

    @Override
    public void finish(){
        Intent data = new Intent();
        if (getIntent().getParcelableExtra("Customer") != null){
            data.putExtra("Customer", customer);
        } else if (getIntent().getParcelableExtra("Roadside") != null){
            data.putExtra("Roadside", roadsideAssistant);
        }
        setResult(RESULT_OK, data);
        super.finish();
    }
}
