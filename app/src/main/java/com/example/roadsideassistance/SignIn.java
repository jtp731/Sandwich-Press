package com.example.roadsideassistance;

import android.Manifest;
import android.arch.persistence.room.Database;
import android.arch.persistence.room.Insert;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Calendar;
import java.util.Date;


public class SignIn extends AppCompatActivity {
    Person person = null;
    AppDatabase database;
    boolean permission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        permission = requestPermission();

        this.deleteDatabase("appdatabase");
        database = AppDatabase.getDatabase(getApplicationContext());
        if (!database.roadsideAssistantDao().baseRoadsideExists()) {
            database.userDao().addRoadsideAssistant(new RoadsideAssistant("", "", "", "", "", "", null, null, "", "", -1, false, 0));
            TestData.createTestData(this, 10, 10, 3);
            TestData.createRequests(50);
            TestData.createOffers(20);
            TestData.createActive(10);
            TestData.createPastServices(100);
            //TestData.createSubPayHistory(10);
        }

        Button signup = findViewById(R.id.newSignup);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (permission == false) {
                    permission = requestPermission();
                } else {
                    Intent signupPage = new Intent(SignIn.this, register.class);
                    startActivity(signupPage);
                }

            }
        });
    }

    public void SignIn(View view) {
        //Toast.makeText(this, Calendar.getInstance().toString(), Toast.LENGTH_LONG).show();
        if (permission == false) {
            permission = requestPermission();
        } else {
            boolean canSignIn = true;
            EditText input = findViewById(R.id.email);
            if (input.getText().toString() == null || input.getText().toString().trim().equals("")) {
                Toast.makeText(this, "Email Error", Toast.LENGTH_LONG).show();
                canSignIn = false;
            }
            String email = input.getText().toString().trim();

            input = findViewById(R.id.password);
            if (input.getText().toString() == null || input.getText().toString().trim().equals("")) {
                Toast.makeText(this, "Password Error", Toast.LENGTH_LONG).show();
                canSignIn = false;
            }
            String password = input.getText().toString().trim();

            int people = database.personDao().checkUser(email, password);
            if (people != 1 || !canSignIn)
                Toast.makeText(this, "Bad password email combo", Toast.LENGTH_LONG).show();
            else {
            /*
            person = database.personDao().getUserByEmail(email);
            String userString = "User: " + person.username;
            Toast.makeText(this, userString, Toast.LENGTH_LONG).show();
            */
                if (database.roadsideAssistantDao().roadsideAssistantExists(email)) {
                    //Toast.makeText(this, "Roadside exists", Toast.LENGTH_LONG).show();
                    RoadsideAssistant roadsideAssistant = database.roadsideAssistantDao().getRoadsideAssistantByEmail(email);
                    //System.out.print(roadsideAssistant.toString());
                    //Toast.makeText(this, roadsideAssistant.toString(), Toast.LENGTH_LONG).show();
                    roadsideAssistant.services = database.roadsideAssistantDao().getAllRoadsideServices(roadsideAssistant.username);
                    roadsideAssistant.reviews = database.roadsideAssistantDao().getAllRoadsideReviews(roadsideAssistant.username);
                    Intent intent = new Intent(this, RoadsideMainPage.class);
                    intent.putExtra("RoadsideAssistant", roadsideAssistant);
                    startActivity(intent);
                } else if (database.customerDao().customerExists(email)) {
                    Customer customer = database.customerDao().getCustomer(email);
                    customer.cars = database.customerDao().getAllCustomerCars(customer.username);
                    customer.services = database.customerDao().getAllCustomerServices(customer.username);
                    customer.reviews = database.customerDao().getAllCustomerReviews(customer.username);
                    customer.subPayments = database.customerDao().getAllCustomerSubPayments(customer.username);
                    customer.updateSubPayments(this);
                    Intent intent = new Intent(this, CustomerMainPage.class);
                    intent.putExtra("Customer", customer);
                    startActivity(intent);
                } else if (database.managerDao().managerExists(email)) {
                    Manager manager = database.managerDao().getManager(email);
                    Intent intent = new Intent(this, ManagerMainPage.class);
                    intent.putExtra("Manager", manager);
                    startActivity(intent);
                }
            }
        }
    }

    public boolean requestPermission() {
        final int MY_PERMISSIONS_REQUEST_LOCATION = 0;
        if (ContextCompat.checkSelfPermission(SignIn.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(SignIn.this, Manifest.permission.ACCESS_FINE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setTitle("Allow Location Services")
                        .setMessage("Location services are required to share your location for roadside assistance. Please accept to continue.")
                        .setPositiveButton("Continue", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                ActivityCompat.requestPermissions(SignIn.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
                            }
                        })
                        .create()
                        .show();
            } else {
                ActivityCompat.requestPermissions(SignIn.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_PERMISSIONS_REQUEST_LOCATION);
            }
            return false;
        } else {
            return true;
        }
    }
}