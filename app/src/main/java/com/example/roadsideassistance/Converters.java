package com.example.roadsideassistance;

import android.arch.persistence.room.TypeConverter;

import java.text.DateFormat;
import java.util.Date;

public class Converters {
    @TypeConverter
    public Date dateFromString(String date) {
        try {
            return DateFormat.getDateInstance().parse(date);
        }
        catch (java.text.ParseException e){
            return new Date(0);
        }
    }

    @TypeConverter
    public String dateToString(Date date) {
        return DateFormat.getDateInstance().format(date);
    }
}
