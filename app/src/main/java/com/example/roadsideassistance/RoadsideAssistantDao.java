package com.example.roadsideassistance;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

@Dao
public interface RoadsideAssistantDao {
    @Insert
    void addRoadsideAssistant(RoadsideAssistant roadsideAssistant);

    @Query("select * from roadsideassistant where username = :username")
    RoadsideAssistant getRoadsideAssistant(String username);
}
