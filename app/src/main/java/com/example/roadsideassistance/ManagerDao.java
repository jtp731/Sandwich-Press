package com.example.roadsideassistance;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.RoomWarnings;

import java.util.List;

@Dao
public interface ManagerDao {
    @Query("select case when exists(select * from customer where email = :email) then 1 else 0 end")
    boolean managerExists(String email);

    @Query("select * from manager where email = :email")
    Manager getManager(String email);

    @Query("select * from customer")
    List<Person> getAllCustomers();

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("select * from roadsideassistant")
    List<Person> getAllRoadsideAssistants();

    @SuppressWarnings(RoomWarnings.CURSOR_MISMATCH)
    @Query("select * from person where exists(select * from customer) or exists(select * from roadsideassistant)")
    List<Person> getAllCustomersAndRoadside();
}
