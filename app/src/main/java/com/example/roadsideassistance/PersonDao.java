package com.example.roadsideassistance;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface PersonDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void addPerson(Person person);

    @Query("select * from person where username = :username")
    Person getUser(String username);

    @Query("select * from person where email = :email")
    Person getUserByEmail(String email);

    @Query("select count(*) from person where email = :email and password = :password")
    int checkUser(String email, String password);
}
