package com.example.roadsideassistance;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

@Dao
public interface ManagerDao {
    @Query("select case when exists(select * from customer where email = :email) then 1 else 0 end")
    boolean managerExists(String email);

    @Query("select * from manager where email = :email")
    Manager getManager(String email);
}
