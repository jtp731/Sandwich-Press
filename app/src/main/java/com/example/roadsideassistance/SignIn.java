package com.example.roadsideassistance;

import android.arch.persistence.room.Database;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Date;


public class SignIn extends AppCompatActivity {
    Person person = null;
    AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        database = AppDatabase.getDatabase(getApplicationContext());

        //For deleting the database and recreating when the database schema has been changed
        this.deleteDatabase("appdatabase");
        database = AppDatabase.getDatabase(getApplicationContext());

        //Add Test roadside assistant
        database.personDao().addPerson(new Person("cust1", "123", "44443333", "cust1@email", "John", "Doe"));
        database.userDao().addRoadsideAssistant(new RoadsideAssistant("road1", "123", "44443333", "road1@email", "John", "Doe", true, 50f));

        Button signup = findViewById(R.id.newSignup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signupPage = new Intent(SignIn.this, register.class);
                startActivity(signupPage);
            }
        });
    }

    public void SignIn(View view) {
        Toast.makeText(this, "Test", Toast.LENGTH_LONG).show();
        EditText input = findViewById(R.id.email);
        if(input.getText().toString() == null || input.getText().toString().trim() == "")
            Toast.makeText(this, "Email Error", Toast.LENGTH_LONG).show();
        String email = input.getText().toString().trim();

        input = findViewById(R.id.password);
        if(input.getText().toString() == null || input.getText().toString().trim() == "")
            Toast.makeText(this, "Password Error", Toast.LENGTH_LONG).show();
        String password = input.getText().toString().trim();

        int people = database.personDao().checkUser(email, password);
        if(people != 1)
            Toast.makeText(this, "Bad password email combo", Toast.LENGTH_LONG).show();
        else {
            person = database.personDao().getUserByEmail(email);
            Intent intent = new Intent(SignIn.this, CustomerHome.class);
            intent.putExtra("Username", person.username);
            startActivity(intent);
            /*8
            if(database.roadsideAssistantDao().roadsideAssistantExists(email))
                Toast.makeText(this, "Roadside exists", Toast.LENGTH_LONG).show();
            else
                Toast.makeText(this, "Roadside does not exists", Toast.LENGTH_LONG).show();
               */
        }
    }
}