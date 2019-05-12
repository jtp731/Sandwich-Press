package com.example.roadsideassistance;

import android.arch.persistence.room.TypeConverter;

import java.text.DateFormat;
import java.util.Date;

public class Converters {
    @TypeConverter
    public Date dateFromLong(long date) {
        return new Date(date);
    }

    @TypeConverter
    public long dateToLong(Date date) {
        return date.getTime();
    }
}
