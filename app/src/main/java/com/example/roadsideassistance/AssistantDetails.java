package com.example.roadsideassistance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;

public class AssistantDetails extends AppCompatActivity {

    RoadsideAssistant roadsideAssistant;
    EditText fNameText, lNameText, emailEmailAdd, phNoNumber, bankNameText, bankNumberText, bankBSBText, licNoText, assistantRangeNum, canTowSwitch;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_assistant_details);

        roadsideAssistant = getIntent().getParcelableExtra("Roadside");
        fNameText = findViewById(R.id.fNameText);

        fNameText.setText(roadsideAssistant.firstName);
        Switch canTowSwitch = findViewById(R.id.canTowSwitch);
        if (roadsideAssistant.canTow){
            canTowSwitch.setChecked(true);
        }

        Button buttonChangePassword = findViewById(R.id.buttonChangePassword);
        buttonChangePassword.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(AssistantDetails.this, RoadsideChangePassword.class);
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
