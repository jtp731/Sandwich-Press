package com.example.roadsideassistance;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class register extends AppCompatActivity {
    private AppDatabase database;

    Customer customer;
    RoadsideAssistant roadsideAssistant;
    Manager manager;
    EditText usernameText, fNameText, lNameText, phoneNumberText, emailText, password1Text, password2Text, accessLevel;
    TextView heading;
    Switch canTowSwitch;
    Button next;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        database = AppDatabase.getDatabase(getApplicationContext());
        findViewById(R.id.newUsernameError).setVisibility(View.GONE);
        findViewById(R.id.newEmailError).setVisibility(View.GONE);
        findViewById(R.id.newPhoneError).setVisibility(View.GONE);
        findViewById(R.id.newPasswordError).setVisibility(View.GONE);
        findViewById(R.id.accessLevelError).setVisibility(View.GONE);
        findViewById(R.id.accessLevel).setVisibility(View.GONE);
        findViewById(R.id.accessLevelLabel).setVisibility(View.GONE);
        findViewById(R.id.firstNameError).setVisibility(View.GONE);
        findViewById(R.id.lastNameError).setVisibility(View.GONE);

        heading = findViewById(R.id.heading);
        usernameText = findViewById(R.id.newUsername);
        fNameText = findViewById(R.id.newFName);
        lNameText = findViewById(R.id.newLName);
        phoneNumberText = findViewById(R.id.newPhone);
        emailText = findViewById(R.id.newEmail);
        password1Text = findViewById(R.id.newPassword);
        password2Text = findViewById(R.id.newPConfirm);
        accessLevel = findViewById(R.id.accessLevel);
        canTowSwitch = findViewById(R.id.canTowSwitch);
        canTowSwitch.setVisibility(View.GONE);
        next = findViewById(R.id.newSignup);

        if (getIntent().getParcelableExtra("Customer") != null){
            Button btn = (Button) findViewById(R.id.newSignup);
            btn.setText("Save");
            customer = getIntent().getParcelableExtra("Customer");
            heading.setText("Edit Details");
            usernameText.setEnabled(false);
            fNameText.setEnabled(false);
            lNameText.setEnabled(false);
            usernameText.setText(customer.username);
            fNameText.setText(customer.firstName);
            lNameText.setText(customer.lastName);
            phoneNumberText.setText(customer.phonenumber);
            emailText.setText(customer.email);

            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Boolean success = true;
                    String phonenumber = phoneNumberText.getText().toString();
                    String email = emailText.getText().toString();
                    String password = password1Text.getText().toString();
                    String confirmPass = password2Text.getText().toString();
                    TextView errorMsg;

                    if (!validPhoneNumber(phonenumber) && phonenumber != customer.phonenumber){
                        errorMsg = findViewById(R.id.newPhoneError);
                        errorMsg.setText("Invalid phonenumber");
                        errorMsg.setVisibility(View.VISIBLE);
                        success = false;
                    }
                    if (!email.equals(customer.email) && emailTaken(email)){
                        errorMsg = findViewById(R.id.newEmailError);
                        errorMsg.setText("Email already in use");
                        errorMsg.setVisibility(View.VISIBLE);
                        success = false;
                    } else if (!validEmail(email)){
                        errorMsg = findViewById(R.id.newEmailError);
                        errorMsg.setText("Invalid mail");
                        errorMsg.setVisibility(View.VISIBLE);
                        success = false;
                    }
                    if (!password.equals(confirmPass)){
                        errorMsg = findViewById(R.id.newPasswordError);
                        errorMsg.setText("Passwords do not match");
                        errorMsg.setVisibility(View.VISIBLE);
                        success = false;
                    }

                    if (success){
                        if (password.matches("")){
                            password = customer.password;

                        }

                        customer.phonenumber = phonenumber;
                        customer.email = email;
                        customer.password = password;

                        database.userDao().updateCustomer(customer);


                        finish();
                    }
                }
            });

        } else if (getIntent().getParcelableExtra("Roadside") != null){
            Button btn = (Button) findViewById(R.id.newSignup);
            btn.setText("Save");
            roadsideAssistant = getIntent().getParcelableExtra("Roadside");
            canTowSwitch.setVisibility(View.VISIBLE);
            if (roadsideAssistant.canTow == true){
                canTowSwitch.setChecked(true);
            }else if (roadsideAssistant.canTow == false){
                canTowSwitch.setChecked(false);
            }
            //setTitle("Edit Details");//Maybe this instead
            heading.setText("Edit Details");
            usernameText.setEnabled(false);
            fNameText.setEnabled(false);
            lNameText.setEnabled(false);
            usernameText.setText(roadsideAssistant.username);
            fNameText.setText(roadsideAssistant.firstName);
            lNameText.setText(roadsideAssistant.lastName);
            phoneNumberText.setText(roadsideAssistant.phonenumber);
            emailText.setText(roadsideAssistant.email);

            next.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Boolean success = true;
                    Boolean canTow = false;
                    if (canTowSwitch.isChecked()){
                        canTow = true;
                    }else if(!canTowSwitch.isChecked()){
                        canTow = false;
                    }
                    String phonenumber = phoneNumberText.getText().toString();
                    String email = emailText.getText().toString();
                    String password = password1Text.getText().toString();
                    String confirmPass = password2Text.getText().toString();
                    TextView errorMsg;

                    if (!validPhoneNumber(phonenumber) && phonenumber != roadsideAssistant.phonenumber){
                        errorMsg = findViewById(R.id.newPhoneError);
                        errorMsg.setText("Invalid phonenumber");
                        errorMsg.setVisibility(View.VISIBLE);
                        success = false;
                    }
                    if (!email.equals(roadsideAssistant.email) && emailTaken(email)){
                        errorMsg = findViewById(R.id.newEmailError);
                        errorMsg.setText("Email already in use");
                        errorMsg.setVisibility(View.VISIBLE);
                        success = false;
                    } else if (!validEmail(email)){
                        errorMsg = findViewById(R.id.newEmailError);
                        errorMsg.setText("Invalid mail");
                        errorMsg.setVisibility(View.VISIBLE);
                        success = false;
                    }
                    if (!password.equals(confirmPass)){
                        errorMsg = findViewById(R.id.newPasswordError);
                        errorMsg.setText("Passwords do not match");
                        errorMsg.setVisibility(View.VISIBLE);
                        success = false;
                    }

                    if (success){
                        if (password.matches("")){
                            password = roadsideAssistant.password;

                        }

                        roadsideAssistant.phonenumber = phonenumber;
                        roadsideAssistant.email = email;
                        roadsideAssistant.password = password;
                        roadsideAssistant.canTow = canTow;

                        database.userDao().updateRoadside(roadsideAssistant);

                        finish();
                    }
                }


            });


        }
        else if (getIntent().getParcelableExtra("Manager") != null) {
            manager = getIntent().getParcelableExtra("Manager");
            heading.setText("Create A Manager");
            findViewById(R.id.accessLevelLabel).setVisibility(View.VISIBLE);
            accessLevel.setVisibility(View.VISIBLE);
            Button create = findViewById(R.id.newSignup);
            create.setText("Create Manager");
        }
    }

    public void createUser(View view) {
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
        if(password.trim().length() < 8 || password.trim().length() > 10) {
            error.setText("Password must be between 8 and 10 characters");
            error.setVisibility(View.VISIBLE);

            canCreateUser = false;
        }
        else {
            error.setVisibility(View.GONE);
        }

        error = findViewById(R.id.firstNameError);
        if(firstName.trim().length() <= 0) {
            error.setText("First Name cannot be empty");
            error.setVisibility(View.VISIBLE);

            canCreateUser = false;
        }
        else {
            error.setVisibility(View.GONE);
        }

        error = findViewById(R.id.lastNameError);
        if(lastName.trim().length() <= 0) {
            error.setText("Last Name cannot be empty");
            error.setVisibility(View.VISIBLE);

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
        if(username.trim().length() < 4 || username.trim().length() > 10) {
            error.setText("Username must be between 4 and 10 characters");
            error.setVisibility(View.VISIBLE);

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

        if(canCreateUser && manager == null) {
            //database.personDao().addPerson(new Person(username, password, phonenumber, email, firstName, lastName));
            //Toast.makeText(this, "Creating new user", Toast.LENGTH_LONG).show();
   
            Intent signupAddress = new Intent(register.this, registerAddress.class);
            signupAddress.putExtra("Person", new Person(username, password, phonenumber, email, firstName, lastName));
            startActivity(signupAddress);
            super.finish();
        }
        else if(manager != null){
            int accLvl = 0;
            if(accessLevel.getText().toString().trim().length() <= 0) {
                TextView accessLevelError = findViewById(R.id.accessLevelError);
                accessLevelError.setText("Access Level cannot be empty");
                accessLevelError.setVisibility(View.VISIBLE);
                canCreateUser = false;
            }
            else {
                accLvl = Integer.parseInt(accessLevel.getText().toString().trim());
                if (!validAccessLevel(accLvl)) {
                    TextView accessLevelError = findViewById(R.id.accessLevelError);
                    accessLevelError.setText("Access Level needs to be less than " + manager.accessLevel);
                    accessLevelError.setVisibility(View.VISIBLE);
                    canCreateUser = false;
                }
            }

            if(canCreateUser) {
                database.userDao().addManager(new Manager(username, password, phonenumber, email, firstName, lastName, accLvl));
                finish();
            }
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
        String patternStringHome = "^[0-9]{8}$";
        String patternStringMobile = "^[0-9]{10}$";
        Pattern patternHome = Pattern.compile(patternStringHome);
        Pattern patternMobile = Pattern.compile(patternStringMobile);
        Matcher matcher = patternHome.matcher(phonenumber);
        if (!matcher.matches()){
            matcher = patternMobile.matcher(phonenumber);
        }
        return matcher.matches();
    }

    boolean userExists(String username) {
        return database.personDao().usernameTaken(username);
    }

    boolean validAccessLevel(int accessLevel) {
        if(manager.accessLevel > accessLevel)
            return true;
        else
            return false;
    }

    @Override
    public void finish(){
        Intent data = new Intent();
        if (getIntent().getParcelableExtra("Customer") !=null){
            data.putExtra("Customer", customer);
        } else if (getIntent().getParcelableExtra("Roadside") !=null){
            data.putExtra("Roadside", roadsideAssistant);
        }
        else if (getIntent().getParcelableExtra("Manager") != null) {
            data.putExtra("Manager", manager);
        }
        setResult(RESULT_OK, data);
        super.finish();
    }
}
