package com.example.roadsideassistance;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.arch.persistence.room.TypeConverter;
import android.arch.persistence.room.TypeConverters;
import android.content.Context;


@Database(entities = {Person.class, Service.class, RoadsideAssistant.class, Customer.class, Car.class}, version = 16, exportSchema = false)
@TypeConverters({Converters.class})

public abstract class AppDatabase extends RoomDatabase {
    private static AppDatabase INSTANCE;

    public abstract PersonDao personDao();
    public abstract ServiceDao serviceDao();
    public abstract RoadsideAssistantDao roadsideAssistantDao();
    public abstract CarDao carDao();

    //test dao
    public abstract TestDao testDao();


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
