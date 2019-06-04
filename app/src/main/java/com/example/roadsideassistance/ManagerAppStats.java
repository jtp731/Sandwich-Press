package com.example.roadsideassistance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;


public class ManagerAppStats extends AppCompatActivity {
    Manager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_app_stats);

        manager = getIntent().getParcelableExtra("Manager");
    }
}