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

    @Query("update car set manufacturer = :manufacturer, model = :model, plateNum = :plateNum, colour = :colour, renewalDate = :subscription where customer_username = :username")
    void editCar(String username, String manufacturer, String model,String plateNum, String colour, Date subscription);

    @Delete
    void deleteCar(Car car);
}
