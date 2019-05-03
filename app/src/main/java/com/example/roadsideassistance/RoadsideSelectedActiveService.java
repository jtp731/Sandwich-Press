package com.example.roadsideassistance;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class RoadsideSelectedActiveService extends AppCompatActivity {
    RoadsideAssistant roadsideAssistant;
    Service activeService;
    Car car;
    AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roadside_selected_active);
        database = AppDatabase.getDatabase(this);

        roadsideAssistant = getIntent().getParcelableExtra("Roadside");
        activeService = getIntent().getParcelableExtra("Service");

        car = database.carDao().getCar(activeService.customer_username, activeService.car_plateNum);

        TextView customerTextView = findViewById(R.id.roadsideSelectedActiveServiceCutomer);
        TextView carTextView = findViewById(R.id.roadsideSelectedActiveServiceCar);
        TextView costTextView = findViewById(R.id.roadsideSelectedActiveServiceCost);

        customerTextView.setText(activeService.customer_username);
        carTextView.setText(activeService.car_plateNum);
        costTextView.setText("" + activeService.cost);
    }

    public void cancelService(View view) {
        roadsideAssistant.removeService(activeService);
        database.serviceDao().setServiceToOpen(activeService.customer_username, activeService.car_plateNum, activeService.time);
        database.serviceDao().deleteService(activeService);
        finish();
    }

    public void finishService(View view) {
        if (car.renewalDate != null) {
            roadsideAssistant.updateService(activeService, 3);
            database.serviceDao().updateService(roadsideAssistant.username, activeService.customer_username, activeService.car_plateNum, activeService.time, 3);
            database.serviceDao().deleteServicesNotEqual(roadsideAssistant.username, activeService.customer_username, activeService.car_plateNum, activeService.time);
        } else {
            roadsideAssistant.updateService(activeService, 2);
            database.serviceDao().updateService(roadsideAssistant.username, activeService.customer_username, activeService.car_plateNum, activeService.time, 3);
            database.serviceDao().deleteServicesNotEqual(roadsideAssistant.username, activeService.customer_username, activeService.car_plateNum, activeService.time);
        }
    }
}
