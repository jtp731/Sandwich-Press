package com.example.roadsideassistance;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface CarDao {
    @Insert(onConflict = OnConflictStrategy.FAIL)
    void addCar(Car car);

    @Query("select * from car where customer_username = :username")
    abstract Car getCars(String username);

    @Query("select count(*) from car where customer_username = :username")
    abstract Integer countCars(String username);

    @Delete
    abstract void deleteCar(Car car);
}
