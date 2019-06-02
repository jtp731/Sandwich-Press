package com.example.roadsideassistance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class CustomerAccountSettings extends AppCompatActivity {

    Customer customer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_customer_account_settings);

        customer = getIntent().getParcelableExtra("Customer");
    }

    public void updateBilling(View view){
        Intent intent = new Intent(this, registerBankAccount.class);
        intent.putExtra("Customer", customer);
        startActivityForResult(intent, 1);
    }

    public void updateSubscription(View view){
        Intent intent = new Intent(this, CustomerUpdateSubscription.class);
        intent.putExtra("Customer", customer);
        startActivityForResult(intent, 1);
    }

    public void updateAddress(View view){
        Intent intent = new Intent(this, registerAddress.class);
        intent.putExtra("Customer", customer);
        startActivityForResult(intent, 1);
    }

    public void updateDetails(View view){
        Intent intent = new Intent(this, register.class);
        intent.putExtra("Customer", customer);
        startActivityForResult(intent, 1);
    }

    public void toManageCars(View view) {
        Intent intent = new Intent(this, CustomerManageCars.class);
        intent.putExtra("Customer", customer);
        startActivityForResult(intent, 1);
    }

    public void toPayHistory(View view) {
        Intent intent = new Intent(this, CustomerPaymentHistory.class);
        intent.putExtra("Customer", customer);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK && requestCode == 1) {
            customer = data.getParcelableExtra("Customer");
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
