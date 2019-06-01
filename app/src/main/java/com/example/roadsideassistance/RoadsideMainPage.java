package com.example.roadsideassistance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class RoadsideMainPage extends AppCompatActivity {
    RoadsideAssistant roadsideAssistant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roadside_main_page);

        roadsideAssistant = getIntent().getParcelableExtra("Roadside");

        //Toast.makeText(this, "" + roadsideAssistant.rating, Toast.LENGTH_SHORT).show();
    }

    public void toNewServices(View view) {
        Intent intent = new Intent(this, RoadsideServiceSelect.class);
        intent.putExtra("Roadside", roadsideAssistant);
        startActivityForResult(intent, 1);
    }

    public void toServiceOffers(View view) {
        Intent intent = new Intent(this, RoadsideSelectOffer.class);
        intent.putExtra("Roadside", roadsideAssistant);
        startActivityForResult(intent, 1);
    }

    public void toAcceptedServices(View view) {
        Intent intent = new Intent(this, RoadsideSelectActiveService.class);
        intent.putExtra("Roadside", roadsideAssistant);
        startActivityForResult(intent, 1);
    }

    public void toMyAccount(View view) {
        Intent intent = new Intent(this, RoadsideAccountSettings.class);
        intent.putExtra("Roadside", roadsideAssistant);
        startActivityForResult(intent, 1);
    }

    public void toPastServices(View view) {
        Intent intent = new Intent(this, RoadsidePastServices.class);
        intent.putExtra("Roadside", roadsideAssistant);
        startActivityForResult(intent, 1);
    }

    public void logout(View view){
        Intent intent = new Intent(this, SignIn.class);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == 1) {
            roadsideAssistant = data.getParcelableExtra("Roadside");
        }
    }
}
