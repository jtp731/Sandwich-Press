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

        Bundle extra = getIntent().getExtras();
        if (extra != null){
            username = extra.getString("Username");
        }

        EditText input = findViewById(R.id.carMake);
        final String make = input.getText().toString();

        input = findViewById(R.id.carModel);
        final String model = input.getText().toString();

        input = findViewById(R.id.carColour);
        final String colour = input.getText().toString();

        input = findViewById(R.id.plateNum);
        final String plate = input.getText().toString();

        input = findViewById(R.id.renewal);
        final Date renewal = (Date) input.getText();

        Car car = new Car(username, make, model, colour, plate, renewal);
        database.carDao().addCar(car);

        Intent intent = new Intent(AddCar.this, ManageCars.class);
        intent.putExtra("Username", username);
        startActivity(intent);
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