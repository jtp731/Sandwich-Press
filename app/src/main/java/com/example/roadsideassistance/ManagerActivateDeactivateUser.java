package com.example.roadsideassistance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class ManagerActivateDeactivateUser extends AppCompatActivity {
    Manager manager;
    AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_activate_deactivate_users);
        database = AppDatabase.getDatabase(getApplicationContext());

        manager = getIntent().getParcelableExtra("Manager");
    }
}