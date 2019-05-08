package com.example.roadsideassistance;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Insert;
import android.content.Intent;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        database = AppDatabase.getDatabase(getApplicationContext());
        if (!database.roadsideAssistantDao().baseRoadsideExists()) {
            database.userDao().addRoadsideAssistant(new RoadsideAssistant("", "", "", "", "", "", null, null, false, 0));
        }

        //For deleting the database and recreating when the database schema has been changed
        this.deleteDatabase("appdatabase");
        database = AppDatabase.getDatabase(getApplicationContext());

        //Add Test roadside assistant
    //    database.personDao().addPerson(new Person("road1", "123", "44443333", "road1@email", "John", "Doe"));
        database.userDao().addRoadsideAssistant(new RoadsideAssistant("road1", "123", "44443333", "road1@email", "John", "Doe", true, 50f));
        database.userDao().addRoadsideAssistant(new RoadsideAssistant(new Person("road2", "123", "44442222", "road2@email", "road", "two", new Address(10, "tree", "Fad", "NSW"), new BankAccount(1111222233334444L, new Date())), true));
        database.userDao().addCustomer(new Customer("cust1", "123", "22223333", "cust1@email", "cust", "one"));
        database.carDao().addCar(new Car("cust1", "11ssdd", "3", "Mazda", "Grey", new Date()));
        database.serviceDao().addService(new Service("road2", "cust1", "11ssdd", 55f, 151f, new Date(119, 11, 1), 0f, 0));

        //End Test
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
        //Toast.makeText(this, Calendar.getInstance().toString(), Toast.LENGTH_LONG).show();
        boolean canSignIn = true;
        EditText input = findViewById(R.id.email);
        if(input.getText().toString() == null || input.getText().toString().trim().equals("")) {
            Toast.makeText(this, "Email Error", Toast.LENGTH_LONG).show();
            canSignIn = false;
        }
        String email = input.getText().toString().trim();

        input = findViewById(R.id.password);
        if(input.getText().toString() == null || input.getText().toString().trim().equals("")) {
            Toast.makeText(this, "Password Error", Toast.LENGTH_LONG).show();
            canSignIn = false;
        }
        String password = input.getText().toString().trim();

        int people = database.personDao().checkUser(email, password);
        if(people != 1 || !canSignIn)
            Toast.makeText(this, "Bad password email combo", Toast.LENGTH_LONG).show();
        else {
            /*
            person = database.personDao().getUserByEmail(email);
            String userString = "User: " + person.username;
            Toast.makeText(this, userString, Toast.LENGTH_LONG).show();
            */
            if(database.roadsideAssistantDao().roadsideAssistantExists(email)) {
                //Toast.makeText(this, "Roadside exists", Toast.LENGTH_LONG).show();
                RoadsideAssistant roadsideAssistant = database.roadsideAssistantDao().getRoadsideAssistant(email);
                //System.out.print(roadsideAssistant.toString());
                //Toast.makeText(this, roadsideAssistant.toString(), Toast.LENGTH_LONG).show();
                roadsideAssistant.services = database.roadsideAssistantDao().getAllRoadsideServices(roadsideAssistant.username);
                roadsideAssistant.reviews = database.roadsideAssistantDao().getAllRoadsideReviews(roadsideAssistant.username);
                Intent intent = new Intent(this, AssistantHome.class);
                intent.putExtra("RoadsideAssistant", roadsideAssistant);
                startActivity(intent);
            }
            else if(database.customerDao().customerExists(email)) {
                Customer customer = database.customerDao().getCustomer(email);
                customer.cars = database.customerDao().getAllCustomerCars(customer.username);
                customer.services = database.customerDao().getAllCustomerServices(customer.username);
                customer.reviews = database.customerDao().getAllCustomerReviews(customer.username);
                Intent intent = new Intent(this, CustomerMainPage.class);
                intent.putExtra("Customer", customer);
                startActivity(intent);
            }
            else if(database.managerDao().managerExists(email)) {
                Manager manager = database.managerDao().getManager(email);
                Intent intent = new Intent(this, ManagerMainPage.class);
                intent.putExtra("Manager", manager);
                startActivity(intent);
            }
        }
    }
}