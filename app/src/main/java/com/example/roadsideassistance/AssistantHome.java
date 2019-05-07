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

    RoadsideAssistant roadsideAssistant;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assistant_home);

        roadsideAssistant = getIntent().getParcelableExtra("RoadsideAssistant");



       Button availJobs = findViewById(R.id.button_avail_jobs);
        availJobs.setOnClickListener(new View.OnClickListener() {
           public void onClick(View v) {
               Intent intent = new Intent(AssistantHome.this, RoadsideSelectActiveService.class);
                intent.putExtra("Roadside", roadsideAssistant);
                startActivity(intent);
            }
        });

        Button currJob = findViewById(R.id.button_current_job);
        currJob.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AssistantHome.this, CurrentJob.class);
                intent.putExtra("Roadside", roadsideAssistant);
                startActivity(intent);
            }
        });

        Button assistAccount = findViewById(R.id.button_assistant_account);
        assistAccount.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AssistantHome.this, AssistantAccount.class);
                intent.putExtra("Roadside", roadsideAssistant);
                startActivity(intent);
            }
        });
    }


}
