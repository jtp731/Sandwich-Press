package com.example.roadsideassistance;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

@Dao
public interface CarDao {
    @Insert
    void addCar(Car car);

    @Query("select * from car where customer_username = :username and plateNum = :plateNum")
    Car getCar(String username, String plateNum);
}
