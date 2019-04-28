package com.example.roadsideassistance;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Room;

@Dao
public interface RoadsideAssistantDao {
    @Query("select case when exists(select * from roadsideassistant where email = :email) then 1 else 0 end")
    abstract boolean roadsideAssistantExists(String email);
}
