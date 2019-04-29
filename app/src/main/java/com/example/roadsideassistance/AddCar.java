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

import java.util.Date;

public class AddCar extends AppCompatActivity {

    private AppDatabase database;
    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_car);
        database = AppDatabase.getDatabase(getApplicationContext());
        findViewById(R.id.makeError).setVisibility(View.GONE);
        findViewById(R.id.modelError).setVisibility(View.GONE);
        findViewById(R.id.plateError).setVisibility(View.GONE);
        findViewById(R.id.colourError).setVisibility(View.GONE);
        findViewById(R.id.renewalError).setVisibility(View.GONE);

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
    }

    public void AddCar(View view){
        String username = null;
        Boolean empty = false;
        String make = null;
        String model = null;
        String colour = null;
        String plate = null;
        String renewal = null;


        Bundle extra = getIntent().getExtras();
        if (extra != null){
            username = extra.getString("Username");
        }

        EditText input = findViewById(R.id.carMake);
        if (input != null) {
            make = input.getText().toString();
        } else {
            empty = true;
            findViewById(R.id.makeError).setVisibility(View.GONE);
        }

        input = findViewById(R.id.carModel);
        if (input != null) {
            model = input.getText().toString();
        } else {
            empty = true;
            findViewById(R.id.modelError).setVisibility(View.GONE);
        }

        input = findViewById(R.id.carColour);
        if (input != null) {
            colour = input.getText().toString();
        } else {
            empty = true;
            findViewById(R.id.colourError).setVisibility(View.GONE);
        }

        input = findViewById(R.id.plateNum);
        if (input != null) {
            plate = input.getText().toString();
        } else {
            empty = true;
            findViewById(R.id.plateError).setVisibility(View.GONE);
        }

        input = findViewById(R.id.renewal);
        if (input != null) {
            renewal = input.getText().toString();
        } else {
            empty = true;
            findViewById(R.id.renewalError).setVisibility(View.GONE);
        }

        if (empty == false) {
            Car car = new Car(username, plate, model, make, colour, renewal);
            database.carDao().addCar(car);
        }
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