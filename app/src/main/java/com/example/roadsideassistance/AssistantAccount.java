package com.example.roadsideassistance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class AssistantAccount extends AppCompatActivity {

    RoadsideAssistant roadsideAssistant;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assistant_account);

        roadsideAssistant = getIntent().getParcelableExtra("Roadside");

        Button assistDetails = findViewById(R.id.button_assistant_details);
        assistDetails.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AssistantAccount.this, AssistantDetails.class);
                intent.putExtra("Roadside", roadsideAssistant);
                startActivity(intent);
            }
        });

        Button assistPayHist = findViewById(R.id.button_assistant_pay_history);
        assistPayHist.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AssistantAccount.this, AssistantPaymentHistory.class);
                intent.putExtra("Roadside", roadsideAssistant);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == 1) {
            roadsideAssistant = data.getParcelableExtra("Roadside");
        }
    }
    @Override
    public void finish(){
        Intent data = new Intent();
        data.putExtra("Roadside", roadsideAssistant);
        setResult(RESULT_OK, data);
        super.finish();
    }


}
