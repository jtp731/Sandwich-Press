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
        //Toast.makeText(this, person.username, Toast.LENGTH_LONG).show();
    }

    public void nextButton(View view) {
        EditText input = findViewById(R.id.newStreetNum);
        int streetNum = Integer.parseInt(input.getText().toString());

        input = findViewById(R.id.newStreetName);
        String street = input.getText().toString();

        input = findViewById(R.id.newCity);
        String city = input.getText().toString();

        String state = states.getSelectedItem().toString();

        person.address = new Address(streetNum, street, city, state);

        Intent bankAccountIntent = new Intent(registerAddress.this, registerBankAccount.class);
        bankAccountIntent.putExtra("Person", person);
        startActivity(bankAccountIntent);
        finish();
    }
}
