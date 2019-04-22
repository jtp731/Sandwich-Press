package com.example.roadsideassistance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.Switch;
import android.widget.ToggleButton;

public class registerRoadside extends AppCompatActivity {
    boolean canTow = false;
    Person person;
    AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_roadside);
        Intent intent = getIntent();
        person = intent.getParcelableExtra("Person");
        database = AppDatabase.getDatabase(getApplicationContext());

        Switch towable = findViewById(R.id.towToggle);
        towable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked) {
                    canTow = true;
                }
                else {
                    canTow = false;
                }
            }
        });
    }

    public void finishRegister(View view) {
        RoadsideAssistant roadsideAssistant = new RoadsideAssistant(person, canTow);
        database.roadsideAssistantDao().addRoadsideAssistant(roadsideAssistant);
        finish();
    }
}
