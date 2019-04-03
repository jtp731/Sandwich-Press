package com.example.roadsideassistance;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

public class register extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Bundle extras = getIntent().getExtras();
        EditText newEmail = findViewById(R.id.email);

        String email;
        if (extras == null) {
            email = "";
        } else {
            email = extras.getString("usersEmail");
        }

        newEmail.setText(email);
    }
}
