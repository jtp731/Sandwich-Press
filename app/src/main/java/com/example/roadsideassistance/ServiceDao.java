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

    @Query("select * from service where roadside_assistant_username = ('')")
    List<Service> getNewServiceRequests();

    @Query("select * from service where (roadside_assistant_username = '') AND (latitude >= :minLatitude AND latitude <= :maxLatitude) AND (longitude >= :minLongitude AND longitude <= :maxLongitude)")
    List<Service> getNewServiceRequests(double minLatitude, double maxLatitude, double minLongitude, double maxLongitude);

    @Delete
    void deleteService(Service service);
}