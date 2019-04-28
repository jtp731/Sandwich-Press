package com.example.roadsideassistance;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;

public class CustomerAccount extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_account);

        String username = null;

        Bundle extra = getIntent().getExtras();
        if (extra != null){
            username = extra.getString("Username");
        }

        Button manageCars = findViewById(R.id.manageCars);
        Button updateBilling = findViewById(R.id.updateBilling);
        Button updateSub = findViewById(R.id.updateSub);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ActionBar actionBar = getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setHomeAsUpIndicator(R.drawable.ic_menu);

        drawerLayout = findViewById(R.id.drawer_layout);

        NavigationView navigationView = findViewById(R.id.navigation);
        final String finalUsername = username;
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
                    @Override
                    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                        menuItem.setChecked(true);
                        drawerLayout.closeDrawers();
                        int id = menuItem.getItemId();

                        if (id == R.id.home) {
                            Intent intent = new Intent(CustomerAccount.this, CustomerHome.class);
                            intent.putExtra("Username", finalUsername);
                            startActivity(intent);
                        }

                        return true;
                    }
                });

        navigationView.getMenu().getItem(1).setChecked(true);

        manageCars.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerAccount.this, ManageCars.class);
                intent.putExtra("Username", finalUsername);
                startActivity(intent);
            }
        });

        updateBilling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerAccount.this, UpdateBilling.class);
                intent.putExtra("Username", finalUsername);
                startActivity(intent);
            }
        });

        updateSub.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerAccount.this, UpdateSubscription.class);
                intent.putExtra("Username", finalUsername);
                startActivity(intent);
            }
        });
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