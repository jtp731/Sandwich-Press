package com.example.roadsideassistance;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.database.Cursor;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.CursorAdapter;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteDatabase;



public class ManagerActivateDeactivateUser extends AppCompatActivity {
    Manager manager;
    AppDatabase database;
    String sfirstName, slastName;
    EditText firstName, lastName;
    Button searchButton, activateButton, deactivateButton;
    TextView textView;
    //Boolean check = true;

    LinearLayout userListLayout,userListLayout2,userListLayout3,userListLayout4,userListLayout5,userListLayout6, userListLayout7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_activate_deactivate_users);
        database = AppDatabase.getDatabase(getApplicationContext());

        // TodoDatabaseHandler is a SQLiteOpenHelper class connecting to SQLite


        manager = getIntent().getParcelableExtra("Manager");

        firstName = findViewById(R.id.firstName);
        lastName = findViewById(R.id.lastName);


        searchButton = findViewById(R.id.searchButton);
        activateButton  = findViewById(R.id.activateButton);
        deactivateButton = findViewById(R.id.deactivateButton);
        userListLayout = findViewById(R.id.userListLayout);
        userListLayout2 = findViewById(R.id.userListLayout2);
        userListLayout5 = findViewById(R.id.userListLayout5);
        //userListLayout3 = findViewById(R.id.userListLayout3);
        //userListLayout4 = findViewById(R.id.userListLayout4);

        //userListLayout6 = findViewById(R.id.userListLayout6);
        //userListLayout7 = findViewById(R.id.userListLayout7);

        searchButton.setOnClickListener
                (
                        new View.OnClickListener()
                        {
                            public void onClick(View v)
                            {
                                if (userListLayout.getVisibility() == View.VISIBLE)
                                {
                                    userListLayout.setVisibility(View.INVISIBLE);

                                    if(firstName.getText().toString().contains("cust35")){
                                        userListLayout2.setVisibility(View.VISIBLE);
                                        activateButton.setOnClickListener
                                                (
                                                        new View.OnClickListener()
                                                        {
                                                            public void onClick(View v)
                                                            {
                                                                userListLayout2.setVisibility(View.VISIBLE);
                                                                userListLayout5.setVisibility(View.INVISIBLE);

                                                            }
                                                        }
                                                );
                                        deactivateButton.setOnClickListener
                                                (
                                                        new View.OnClickListener()
                                                        {
                                                            public void onClick(View v)
                                                            {
                                                                userListLayout2.setVisibility(View.INVISIBLE);
                                                                userListLayout5.setVisibility(View.VISIBLE);

                                                            }
                                                        }
                                                );
                                        Log.d("Cus", "Is working");
                                    }

                                }
                                else
                                {
                                    userListLayout.setVisibility(View.VISIBLE);
                                    userListLayout2.setVisibility(View.INVISIBLE);
                                    //userListLayout3.setVisibility(View.INVISIBLE);
                                    //userListLayout4.setVisibility(View.INVISIBLE);
                                    userListLayout5.setVisibility(View.INVISIBLE);
                                    //userListLayout6.setVisibility(View.INVISIBLE);
                                    //userListLayout7.setVisibility(View.INVISIBLE);
                                }
                            }
                        }
                );



    }






}