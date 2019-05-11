package com.example.roadsideassistance;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class ManagerSelectUser extends AppCompatActivity {
    Manager manager;
    AppDatabase database;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_reviews_select_user);

        manager = getIntent().getParcelableExtra("Manager");
        database = AppDatabase.getDatabase(this);


    }
}
