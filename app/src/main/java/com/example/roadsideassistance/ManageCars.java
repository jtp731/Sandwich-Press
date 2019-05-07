package com.example.roadsideassistance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Vector;

public class ManageCars extends AppCompatActivity {

    Customer customer;
    Button addCar;
    Button home;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_cars);

        customer = getIntent().getParcelableExtra("Customer");
        ArrayAdapter<String> carNames;
        addCar = findViewById(R.id.addCar);
        home = findViewById(R.id.home);

        final ListView carList = findViewById(R.id.car);
        if (customer.cars.size() > 0){
            Vector<String> carsAsString = new Vector<>();
            Car currCar;
            for (int i = 0; i < customer.cars.size(); i++) {
                currCar = customer.cars.get(i);
                carsAsString.add(currCar.manufacturer + " " + currCar.model + "  " + currCar.plateNum);
            }
            ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_list_view, carsAsString);
            carList.setAdapter(adapter);
        }

        carList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ManageCars.this, CustomerEditCar.class);
                intent.putExtra("Customer", customer);
                intent.putExtra("Car", customer.cars.get(position));
                intent.putExtra("Position", position);
                startActivity(intent);
            }
        });

        addCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageCars.this, AddCar.class);
                intent.putExtra("Customer", customer);
                startActivity(intent);
            }
        });

        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ManageCars.this, CustomerMainPage.class);
                intent.putExtra("Customer", customer);
                startActivity(intent);
            }
        });
    }
}
