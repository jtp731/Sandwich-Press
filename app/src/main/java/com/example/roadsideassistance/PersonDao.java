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
    //This is a Query it will run the SQL query in the brackets
    //the :username is the parameter username in the function
    @Query("select * from person where username = :username")
    Person getUser(String username);

    @Query("select streetnum, street, city, state from person where username = :username")
    Address getAddress(String username);

    @Query("select cardnum, expirydate from person where username = :username")
    BankAccount getBankAccount(String username);

    //This is the delete statement it will delete the person the database.
    //Currently the database is set to cascade on delete so all info linked to the person
    //in the database will also be deleted
    @Delete
    void deleteUser(Person person);

    @Query("select * from person where email = :email")
    Person getUserByEmail(String email);

    @Query("select count(*) from person where email = :email and password = :password")
    int checkUser(String email, String password);

    @Query("select case when exists(select * from person where username = :username) then 1 else 0 end")
    boolean usernameTaken(String username);

    @Query("select case when exists(select * from person where email = :email) then 1 else 0 end")
    boolean emailTaken(String email);

    @Query("update person set phonenumber = :phonenumber, email = :email, password = :password where username = :username")
    void updatePerson(String username, String phonenumber, String email, String password);
}
