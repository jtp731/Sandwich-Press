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

public class AssistantHome extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assistant_home);



       Button availJobs = findViewById(R.id.button_avail_jobs);
        availJobs.setOnClickListener(new View.OnClickListener() {
           public void onClick(View v) {
                startActivity(new Intent(AssistantHome.this, RoadsideSelectActiveService.class));
            }
        });
    }


}
