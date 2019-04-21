package com.example.roadsideassistance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.RadioGroup;

public class registerBankAccount extends AppCompatActivity {
    static final int CUSTOMER = 0;
    static final int ROADSIDE_ASSISTANT = 1;
    static final int MANAGER = 2;
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
    }

    public void onClick(View view) {
        if(currentPersonType == CUSTOMER) {
            database.personDao().addPerson(person);
            //return to login
            finish();
        }

        if(currentPersonType == ROADSIDE_ASSISTANT) {
            //go to roadside page
            Intent roadsideIntent = new Intent(registerBankAccount.this, registerRoadside.class);
            roadsideIntent.putExtra("Person", person);
            startActivity(roadsideIntent);
        }

        if(currentPersonType == MANAGER) {
            //go to manager page
            Intent managerIntent = new Intent( registerBankAccount.this, registerManager.class);
            managerIntent.putExtra("Person", person);
            startActivity(managerIntent);
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

    public void setToManager(View view) {
        currentPersonType = MANAGER;
        Button button = findViewById(R.id.bankNextButton);
        button.setText("Next");
    }
}
