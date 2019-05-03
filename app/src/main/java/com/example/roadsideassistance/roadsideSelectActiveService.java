package com.example.roadsideassistance;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class roadsideSelectActiveService extends AppCompatActivity {
    RoadsideAssistant roadsideAssistant;
    int selectedActiveServiceIndex = -1;
    ArrayList<Service> activeServices;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roadside_select_active);

        roadsideAssistant = getIntent().getParcelableExtra("Roadside");
        activeServices = roadsideAssistant.getActiveServices();

        final LinearLayout activeServicesLayout = findViewById(R.id.roadsideActiveSelectLayout);
    }

    public void roadsideSelectActiveService(View view) {

    }
}
