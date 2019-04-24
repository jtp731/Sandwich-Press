package com.example.roadsideassistance;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface PersonDao {
    @Insert(onConflict = OnConflictStrategy.FAIL)
    void addPerson(Person person);

    @Query("select * from person where username = :username")
    Person getUser(String username);

    @Query("select streetnum, street, city, state from person where username = :username")
    Address getAddress(String username);

    @Query("select cardnum, expirydate, balance from person where username = :username")
    BankAccount getBankAccount(String username);

    @Delete
    void deleteUser(Person person);

    @Query("select * from person where email = :email")
    Person getUserByEmail(String email);

    @Query("select count(*) from person where email = :email and password = :password")
    int checkUser(String email, String password);
}
