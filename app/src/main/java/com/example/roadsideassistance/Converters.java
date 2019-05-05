package com.example.roadsideassistance;

import android.arch.persistence.room.TypeConverter;

import java.text.DateFormat;
import java.util.Date;

public class Converters {
    @TypeConverter
    public Date dateFromString(String date) {
        return new Date(date);
    }

    @TypeConverter
    public String dateToString(Date date) {
        return date.toString();
    }
}
