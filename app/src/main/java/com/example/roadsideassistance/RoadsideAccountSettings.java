package com.example.roadsideassistance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class RoadsideAccountSettings extends AppCompatActivity {
    RoadsideAssistant roadsideAssistant;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_roadside_account_settings);

        roadsideAssistant = getIntent().getParcelableExtra("Roadside");
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == 1) {
            roadsideAssistant = data.getParcelableExtra("Roadside");
        }
    }

    public void updatePayment(View view){
        Intent intent = new Intent(this, registerBankAccount.class);
        intent.putExtra("Roadside", roadsideAssistant);
        startActivityForResult(intent, 1);
    }

    public void updateAddress(View view){
        Intent intent = new Intent(this, registerAddress.class);
        intent.putExtra("Roadside", roadsideAssistant);
        startActivityForResult(intent, 1);
    }

    public void updateDetails(View view){
        Intent intent = new Intent(this, register.class);
        intent.putExtra("Roadside", roadsideAssistant);
        startActivityForResult(intent, 1);
    }

    @Override
    public void finish(){
        Intent data = new Intent();
        data.putExtra("Roadside", roadsideAssistant);
        setResult(RESULT_OK, data);
        super.finish();
    }

}
