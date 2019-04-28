package com.example.roadsideassistance;

import android.arch.persistence.room.Database;
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
import android.widget.Toast;

public class ManageCars extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    AppDatabase database;
    Car car = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_cars);

        Button addCar = findViewById(R.id.newCar);
        EditText carDatabase = findViewById(R.id.dataCar);
        database = AppDatabase.getDatabase(getApplicationContext());

        String username = null;
        Bundle extra = getIntent().getExtras();
        if (extra != null){
            username = extra.getString("Username");
        }

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        drawerLayout = findViewById(R.id.drawer_layout);

        car = database.carDao().getCars(username);
        if (car != null){
            carDatabase.setText(car.manufacturer);
        }

        final String finalUsername = username;
        addCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageCars.this, AddCar.class);
                intent.putExtra("Username", finalUsername);
                startActivity(intent);
            }
        });

        NavigationView navigationView = findViewById(R.id.navigation);
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        int id = menuItem.getItemId();

                        if (id == R.id.home) {
                            Intent intent = new Intent(ManageCars.this, CustomerHome.class);
                            startActivity(intent);
                        } else if (id == R.id.userAccount) {
                            Intent intent = new Intent(ManageCars.this, CustomerAccount.class);
                            startActivity(intent);
                        }

                        return true;
                    }
                });

        navigationView.getMenu().getItem(1).setChecked(true);
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