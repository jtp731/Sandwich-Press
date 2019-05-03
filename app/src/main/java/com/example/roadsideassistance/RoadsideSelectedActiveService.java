package com.example.roadsideassistance;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class RoadsideSelectedActiveService extends AppCompatActivity {
    RoadsideAssistant roadsideAssistant;
    Service activeService;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roadside_selected_active);
    }
}
