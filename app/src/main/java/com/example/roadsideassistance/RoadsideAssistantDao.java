package com.example.roadsideassistance;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;

@Dao
public interface RoadsideAssistantDao {
    @Insert
    void addRoadsideAssistant(RoadsideAssistant roadsideAssistant);
}
