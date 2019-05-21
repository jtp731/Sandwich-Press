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

import java.util.Vector;

public class CustomerManageCars extends AppCompatActivity {

    Customer customer;
    Button addCar;
    ListView carList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_manage_cars);

        customer = getIntent().getParcelableExtra("Customer");
        ArrayAdapter<String> carNames;
        addCar = findViewById(R.id.addCar);

        carList = findViewById(R.id.car);

        populateCarDisplay();

        carList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(CustomerManageCars.this, CustomerEditCar.class);
                intent.putExtra("Customer", customer);
                intent.putExtra("Car", customer.cars.get(i));
                intent.putExtra("Position", i);
                startActivityForResult(intent, 1);
            }
        });

        addCar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CustomerManageCars.this, CustomerAddCar.class);
                intent.putExtra("Customer", customer);
                startActivityForResult(intent, 1);
            }
        });
    }

    public void populateCarDisplay(){
        if (customer.cars.size() > 0) {
            Vector<String> carsAsString = new Vector<>();
            Car currCar;
            for (int i = 0; i < customer.cars.size(); i++) {
                currCar = customer.cars.get(i);
                carsAsString.add(currCar.manufacturer + " " + currCar.model + " " + currCar.plateNum);
            }
            ArrayAdapter adapter = new ArrayAdapter<String>(this, R.layout.activity_list_view, carsAsString);
            carList.setAdapter(adapter);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == 1) {
            customer = data.getParcelableExtra("Customer");
            populateCarDisplay();
        }
    }

    @Override
    public void finish(){
        Intent data = new Intent();
        data.putExtra("Customer", customer);
        setResult(RESULT_OK, data);
        super.finish();
    }
}
