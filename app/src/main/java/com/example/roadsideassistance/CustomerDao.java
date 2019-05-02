package com.example.roadsideassistance;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface CustomerDao {
    @Query("select case when exists(select * from customer where email = :email) then 1 else 0 end")
    boolean customerExists(String email);

    @Query("select * from customer where email = :email")
    Customer getCustomer(String email);

    @Query("select * from service where customer_username = :username")
    List<Service> getAllCustomerServices(String username);

    @Query("select * from car where customer_username = :username")
    List<Car> getAllCustomerCars(String username);

    @Query("select * from review where customer_username = :username")
    List<Review> getAllCustomerReviews(String username);
}
