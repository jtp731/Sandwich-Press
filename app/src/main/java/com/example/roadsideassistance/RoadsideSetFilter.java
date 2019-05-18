package com.example.roadsideassistance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.CheckBox;

public class RoadsideSetFilter extends AppCompatActivity {
    byte filter;
    CheckBox battery, tyre, keys, fuel, stuck, breakdown;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roadside_set_filter);

        filter = getIntent().getByteExtra("Filter", (byte)0);

        breakdown = findViewById(R.id.breakBox);
        battery = findViewById(R.id.batteryBox);
        tyre = findViewById(R.id.tyreBox);
        keys = findViewById(R.id.keysBox);
        fuel = findViewById(R.id.fuelBox);
        stuck = findViewById(R.id.stuckBox);

        if((filter & Service.MECHANICAL_BREAKDOWN) != 0)
            breakdown.setChecked(true);

        if((filter & Service.FLAT_BATTERY) != 0)
            battery.setChecked(true);

        if((filter & Service.FLAT_TYRE) != 0)
            tyre.setChecked(true);

        if((filter & Service.KEYS_IN_CAR) != 0)
            keys.setChecked(true);

        if((filter & Service.OUT_OF_FUEL) != 0)
            fuel.setChecked(true);

        if((filter & Service.CAR_STUCK) != 0)
            stuck.setChecked(true);
    }

    public void setFilter(View view) {
        if (breakdown.isChecked())
            filter = (byte)(filter | Service.MECHANICAL_BREAKDOWN);
        else
            filter = (byte)(filter & ~Service.MECHANICAL_BREAKDOWN);

        if (battery.isChecked())
            filter = (byte)(filter | Service.FLAT_BATTERY);
        else
            filter = (byte)(filter & ~Service.FLAT_BATTERY);

        if (tyre.isChecked())
            filter = (byte)(filter | Service.FLAT_TYRE);
        else
            filter = (byte)(filter & ~Service.FLAT_TYRE);

        if (keys.isChecked())
            filter = (byte)(filter | Service.KEYS_IN_CAR);
        else
            filter = (byte)(filter & ~Service.KEYS_IN_CAR);

        if (fuel.isChecked())
            filter = (byte)(filter | Service.OUT_OF_FUEL);
        else
            filter = (byte)(filter & ~Service.OUT_OF_FUEL);

        if (stuck.isChecked())
            filter = (byte)(filter | Service.CAR_STUCK);
        else
            filter = (byte)(filter & ~Service.CAR_STUCK);

        finish();
    }

    @Override
    public void finish() {
        Intent data = new Intent();
        data.putExtra("Filter", filter);
        setResult(RESULT_OK, data);
        super.finish();
    }

}
