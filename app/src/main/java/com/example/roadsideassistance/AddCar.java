package com.example.roadsideassistance;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class AddCar extends AppCompatActivity {

    private AppDatabase database;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_cars);
        database = AppDatabase.getDatabase(getApplicationContext());

        Button addCar = findViewById(R.id.newCar);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        drawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        int id = menuItem.getItemId();

                        if (id == R.id.home) {
                            Intent intent = new Intent(AddCar.this, CustomerHome.class);
                            startActivity(intent);
                        } else if (id == R.id.userAccount) {
                            Intent intent = new Intent(AddCar.this, CustomerAccount.class);
                            startActivity(intent);
                        }

                        return true;
                    }
                });

        navigationView.getMenu().getItem(1).setChecked(true);

        EditText input = findViewById(R.id.carMake);
        String make = input.getText().toString();

        input = findViewById(R.id.carModel);
        String model = input.getText().toString();

        input = findViewById(R.id.carColour);
        String colour = input.getText().toString();

        input = findViewById(R.id.plateNum);
        String plate = input.getText().toString();


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                drawerLayout.openDrawer(GravityCompat.START);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}