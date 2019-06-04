package com.example.roadsideassistance;


import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
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
    Button searchButton, activateButton;

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

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sfirstName = firstName.getText().toString();
                slastName = lastName.getText().toString();

            }
        });

        activateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

    }

    public class TodoCursorAdapter extends CursorAdapter {
        public TodoCursorAdapter(Context context, Cursor cursor) {
            super(context, cursor, 0);
        }

        // The newView method is used to inflate a new view and return it,
        // you don't bind any data to the view at this point.
        @Override
        public View newView(Context context, Cursor cursor, ViewGroup parent) {
            return LayoutInflater.from(context).inflate(R.layout.item_todo, parent, false);
        }

        // The bindView method is used to bind all data to a given view
        // such as setting the text on a TextView.
        @Override
        public void bindView(View view, Context context, Cursor cursor) {
            // Find fields to populate in inflated template
            TextView tvUserType = (TextView) view.findViewById(R.id.userType);
            TextView tvName = (TextView) view.findViewById(R.id.name);
            TextView tvStatus = (TextView) view.findViewById(R.id.status);
            // Extract properties from cursor
            String userType = cursor.getString(cursor.getColumnIndexOrThrow("userType"));
            String name = cursor.getString(cursor.getColumnIndexOrThrow("name"));
            String status = cursor.getString(cursor.getColumnIndexOrThrow("status"));

            // Populate fields with extracted properties
            tvUserType.setText(userType);
            tvName.setText(name);
            tvStatus.setText(status);

        }
    }


}