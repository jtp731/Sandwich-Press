package com.example.roadsideassistance;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class register extends AppCompatActivity {
    private AppDatabase database;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        database = AppDatabase.getDatabase(getApplicationContext());
        findViewById(R.id.newUsernameError).setVisibility(View.GONE);
        findViewById(R.id.newEmailError).setVisibility(View.GONE);
        findViewById(R.id.newPhoneError).setVisibility(View.GONE);
        findViewById(R.id.newPasswordError).setVisibility(View.GONE);
    }

    void createUser(View view) {
        EditText input = findViewById(R.id.newUsername);
        String username = input.getText().toString();

        input = findViewById(R.id.newFName);
        String firstName = input.getText().toString();

        input = findViewById(R.id.newLName);
        String lastName = input.getText().toString();

        input = findViewById(R.id.newEmail);
        String email = input.getText().toString();

        input = findViewById(R.id.newPhone);
        String phonenumber = input.getText().toString();

        input = findViewById(R.id.newPassword);
        String password = input.getText().toString();

        input = findViewById(R.id.newPConfirm);
        String passwordConfirm = input.getText().toString();

        boolean canCreateUser = true;
        TextView error;

        error = findViewById(R.id.newPasswordError);
        if(!password.equals(passwordConfirm)) {
            //Notify user passwords aren't the same
            error.setText("Passwords not the same");
            error.setVisibility(View.VISIBLE);

            //Toast.makeText(this, "passwords not the same", Toast.LENGTH_LONG).show();
            canCreateUser = false;
        }
        else {
            error.setVisibility(View.GONE);
        }

        error = findViewById(R.id.newUsernameError);
        if(userExists(username)) {
            //notify user that username is taken
            error.setText("User name taken");
            error.setVisibility(View.VISIBLE);

            //Toast.makeText(this, "username taken", Toast.LENGTH_LONG).show();
            canCreateUser = false;
        }
        else {
            error.setVisibility(View.GONE);
        }

        error = findViewById(R.id.newEmailError);
        if(!validEmail(email)) {
            //notify user email is bad
            error.setText("invalid email");
            error.setVisibility(View.VISIBLE);

            //Toast.makeText(this, "email invalid", Toast.LENGTH_LONG).show();
            canCreateUser = false;
        }
        else if(emailTaken(email)) {
            error.setText("Email Already Used");
            error.setVisibility(View.VISIBLE);

            canCreateUser = false;
        }
        else {
                error.setVisibility(View.GONE);
        }

        error = findViewById(R.id.newPhoneError);
        if(!validPhoneNumber(phonenumber)) {
            //notify user phonenumber is bad
            error.setText("invalid phone number");
            error.setVisibility(View.VISIBLE);

            //Toast.makeText(this, "phone number invald", Toast.LENGTH_LONG).show();
            canCreateUser = false;
        }
        else {
            error.setVisibility(View.GONE);
        }

        if(canCreateUser) {
            //database.personDao().addPerson(new Person(username, password, phonenumber, email, firstName, lastName));
            Toast.makeText(this, "Creating new user", Toast.LENGTH_LONG).show();
   
            Intent signupAddress = new Intent(register.this, registerAddress.class);
            signupAddress.putExtra("Person", new Person(username, password, phonenumber, email, firstName, lastName));
            startActivity(signupAddress);
            finish();
        }
    }

    boolean validEmail(String email) {
        String patternString = "^.+@.+$";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }

    boolean emailTaken(String email) {
        return database.personDao().emailTaken(email);
    }

    boolean validPhoneNumber(String phonenumber) {
        String patternString = "^[0-9]{8}$";
        Pattern pattern = Pattern.compile(patternString);
        Matcher matcher = pattern.matcher(phonenumber);
        return matcher.matches();
    }

    boolean userExists(String username) {
        return database.personDao().usernameTaken(username);
    }
}
