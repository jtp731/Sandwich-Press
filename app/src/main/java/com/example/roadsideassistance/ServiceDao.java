package com.example.roadsideassistance;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Delete;
import android.location.Location;

import java.util.ArrayList;
import java.util.Date;
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

    @Query("select * from service where (roadside_assistant_username = '')  AND (status = 0)" +
            "AND (latitude >= :minLatitude AND latitude <= :maxLatitude) " +
            "AND (longitude >= :minLongitude AND longitude <= :maxLongitude)")
    List<Service> getNewServiceRequests(double minLatitude, double maxLatitude, double minLongitude, double maxLongitude);

    @Query("select case when exists(select * from service where roadside_assistant_username = :roadside_username and customer_username = :customer_username and car_plateNum = :plateNum and time = :time and status = 0) then 1 else 0 end")
    boolean madeOffer(String roadside_username, String customer_username, String plateNum, Date time);

    @Query("update service set status = 0 where roadside_assistant_username = '' and customer_username = :custUsername and car_plateNum = :plateNum and time = :time")
    void setServiceToOpen(String custUsername, String plateNum, Date time);

    @Query("update service set status = :status where roadside_assistant_username = :roadsideUsername and customer_username = :custUsername and car_plateNum = :plateNum and time = :time")
    void updateService(String roadsideUsername, String custUsername, String plateNum, Date time, int status);

    @Query("delete from service where roadside_assistant_username <> :roadsideUsername and customer_username = :custUsername and car_plateNum = :plateNum and time = :time")
    void deleteServicesNotEqual(String roadsideUsername, String custUsername, String plateNum, Date time);

    @Query("select * from service where roadside_assistant_username <> '' and customer_username = :customer_username and car_plateNum = :plateNum and time = :time")
    List<Service> getServiceOffers(String customer_username, String plateNum, Date time);

    @Query("update service set status = :status where (roadside_assistant_username = '' or roadside_assistant_username = :roadsideUsername) and customer_username = :customerUsername and car_plateNum = :plateNum and time = :time")
    void updateServiceStatus(String roadsideUsername, String customerUsername, String plateNum, Date time, int status);

    @Query("delete from service where customer_username = :customer_username and car_plateNum = :plateNum and time = :time")
    void deleteService(String customer_username, String plateNum, Date time);

    @Query("delete from service where roadside_assistant_username = :roadside_username and customer_username = :customer_username and car_plateNum = :plateNum and time = :time")
    void deleteService(String roadside_username, String customer_username, String plateNum, Date time);

    @Query("select case when exists(select * from service where roadside_assistant_username = :roadsideUsername and customer_username = :customerUsername and car_plateNum = :plateNum and time = :time) then 1 else 0 end")
    boolean serviceExists(String roadsideUsername, String customerUsername, String plateNum, Date time);

    @Query("select case when exists(select * from service where roadside_assistant_username = :roadsideUsername and customer_username = :customerUsername and car_plateNum = :plateNum " +
            "and (time = :time or status = 0 or status = 1 or status = 2)) then 1 else 0 end")
    boolean serviceActive(String roadsideUsername, String customerUsername, String plateNum, Date time);

    @Query("select MIN(time) from service where customer_username = :username and (status = 3 or status = 4)")
    Date getEarliestFinishedServiceCustomer(String username);

    @Query("select MIN(time) from service where roadside_assistant_username = :username and (status = 3 or status = 4)")
    Date getEarliestFinishedServiceRoadside(String username);

    @Query("select * from service where roadside_assistant_username = :username and time >= :firstOfMonth and time <= :lastOfMonth group by time order by time desc")
    List<Service> getServicesInMonthRoadside(String username, Date firstOfMonth, Date lastOfMonth);

    @Delete
    void deleteService(Service service);
}