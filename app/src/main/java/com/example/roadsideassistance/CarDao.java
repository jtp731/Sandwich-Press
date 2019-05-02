package com.example.roadsideassistance;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

@Dao
public interface CarDao {
    @Insert
    void addCar(Car car);
}
