package com.example.roadsideassistance;

import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;


public class ManagerActivateDeactivateUser extends AppCompatActivity {
    Manager manager;
    AppDatabase database;
    String sfirstName, slastName;
    EditText firstName, lastName;
    Button searchButton, activateButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_activate_deactivate_users);
        database = AppDatabase.getDatabase(getApplicationContext());

        manager = getIntent().getParcelableExtra("Manager");

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);

        searchButton = findViewById(R.id.searchButton);
        activateButton  = findViewById(R.id.activateButton);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sfirstName = firstName.getText().toString();
                slastName = lastName.getText().toString();

            }
        });

        activateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }
}