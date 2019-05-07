package com.example.roadsideassistance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AssistantHome2 extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assistant_home2);
    }
    Button availJobs = findViewById(R.id.button_avail_jobs2);


    /*Button availJobs = findViewById(R.id.button_avail_jobs2);
        availJobs.setOnClickListener(new View.OnClickListener() {
        public void onClick(View v) {
            startActivity(new Intent(AssistantHome2.this, AvailableJobs.class));
        }
    });*/



}
