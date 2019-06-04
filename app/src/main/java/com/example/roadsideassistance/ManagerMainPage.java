package com.example.roadsideassistance;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class ManagerMainPage extends AppCompatActivity {
    Manager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_main_page);

        manager = getIntent().getParcelableExtra("Manager");
    }

    public void toActivateDeactivateUser(View view) {
        Intent intent = new Intent(this, ManagerActivateDeactivateUser.class);
        intent.putExtra("Manager", manager);
        startActivityForResult(intent, 1);
    }

    public void toAppStats(View view) {
        Intent intent = new Intent(this, ManagerAppStats.class);
        intent.putExtra("Manager", manager);
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
            manager = data.getParcelableExtra("Manager");
        }
    }
}
