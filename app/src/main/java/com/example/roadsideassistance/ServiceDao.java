package com.example.roadsideassistance;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Delete;
import android.location.Location;

import java.util.List;

@Dao
public interface ServiceDao {
    @Insert(onConflict = OnConflictStrategy.FAIL)
    void addService(Service service);

    @Query("select * from service where customer_username = :username")
    List<Service> getServicesForCustomer(String username);

    @Query("select * from service where roadside_assistant_username = :username")
    List<Service> getServicesFromRoadsideAssistant(String username);

    @Query("select * from service where roadside_assistant_username = NULL")
    List<Service> getNewServiceRequest();

    //Might need to change this to get locations in a radius better
    @Query("select * from service where roadside_assistant_username = null AND (latitude >= :minLatitude AND latitude <= :maxLatitude)")
    List<Service> getNewServiceRequest(double minLatitude, double maxLatitude);

    @Delete
    void deleteService(Service service);
}