package com.example.roadsideassistance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AssistantDetails extends AppCompatActivity {

    RoadsideAssistant roadsideAssistant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assistant_details);

        roadsideAssistant = getIntent().getParcelableExtra("RoadsideAssistant");

        Button buttonChangePassword = findViewById(R.id.buttonChangePassword);
        buttonChangePassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AssistantDetails.this, RoadsideChangePassword.class);
                intent.putExtra("Roadside", roadsideAssistant);
                startActivity(intent);
            }
        });
    }
}
