package com.example.roadsideassistance;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import java.util.Date;

@Dao
public interface CarDao {
    @Insert
    void addCar(Car car);

    @Query("select * from car where customer_username = :username and plateNum = :plateNum")
    Car getCar(String username, String plateNum);

    @Query("update car set manufacturer = :manufacturer, model = :model, plateNum = :plateNum, colour = :colour, renewalDate = :subscription where plateNum = :oldPlateNum")
    void editCar(String manufacturer, String model, String plateNum, String colour, Date subscription, String oldPlateNum);

    @Query("update car set renewalDate = :newDate where customer_username = :username and plateNum = :plateNum")
    void updateSubDate(String username, String plateNum, Date newDate);

    @Delete
    void deleteCar(Car car);
}
