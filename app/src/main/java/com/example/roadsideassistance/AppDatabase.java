package com.example.roadsideassistance;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;

/*
This is the database main code. To use the database in an activity you need to get the database using
    AppDatabase database = AppDatabase.getDatabase(getApplicationContext());
Then using the database is done by
    database.classDao.function(parameters);
eg to get a person is done by
    database.PersonDao.getUser("username");
this will give you a person object using the SQL query
    select * from person where username = "username"
 */

//The below is where the classes are added to make them part of the database
@Database(entities = {Person.class, Service.class, RoadsideAssistant.class, Customer.class, Car.class}, version = 16, exportSchema = false)
//This is the type converter class that currently changes date to a string so that is can be stored in the database
@TypeConverters({Converters.class})

public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;

    //This is where the classDao are declared so they can be used
    public abstract PersonDao personDao();
    public abstract ServiceDao serviceDao();
    public abstract RoadsideAssistantDao roadsideAssistantDao();
    public abstract InsertDao userDao();


    public static AppDatabase getDatabase(Context context) {
        if(INSTANCE == null) {
            INSTANCE = Room.databaseBuilder(context, AppDatabase.class, "appdatabase")
                    .allowMainThreadQueries()
                    .fallbackToDestructiveMigration()
                    .build();
        }
        return INSTANCE;
    }

    public static void destroyInstance() {
        INSTANCE = null;
    }
}
