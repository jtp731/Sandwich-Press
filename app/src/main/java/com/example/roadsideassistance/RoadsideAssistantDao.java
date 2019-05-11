package com.example.roadsideassistance;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Room;

import java.util.List;

@Dao
public interface RoadsideAssistantDao {
    @Query("select case when exists(select * from roadsideassistant where email = :email) then 1 else 0 end")
    boolean roadsideAssistantExists(String email);

    @Query("select * from roadsideassistant where email = :email")
    RoadsideAssistant getRoadsideAssistantByEmail(String email);

    @Query("select * from roadsideassistant where username = :username")
    RoadsideAssistant getRoadsideAssistantByUsername(String username);

    @Query("select * from service where roadside_assistant_username = :username")
    List<Service> getAllRoadsideServices(String username);

    @Query("select * from review where roadside_assistant_username = :username")
    List<Review> getAllRoadsideReviews(String username);

    @Query("select case when exists(select * from roadsideassistant where username = '') then 1 else 0 end")
    boolean baseRoadsideExists();
}
