package com.example.roadsideassistance;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

/*
This is a Dao this is the interface used by the database to access the info
 */

@Dao
public interface PersonDao {
    @Insert(onConflict = OnConflictStrategy.FAIL)
    void addPerson(Person person);

    //This is a Query it will run the SQL query in the brackets
    //the :username is the parameter username in the function
    @Query("select * from person where username = :username")
    abstract Person getUser(String username);

    @Query("select streetnum, street, city, state from person where username = :username")
    abstract Address getAddress(String username);

    @Query("select cardnum, expirydate, balance from person where username = :username")
    abstract BankAccount getBankAccount(String username);

    //This is the delete statement it will delete the person the database.
    //Currently the database is set to cascade on delete so all info linked to the person
    //in the database will also be deleted
    @Delete
    abstract void deleteUser(Person person);

    @Query("select * from person where email = :email")
    abstract Person getUserByEmail(String email);

    @Query("select count(*) from person where email = :email and password = :password")
    abstract int checkUser(String email, String password);
}
