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
    public List<Person> getUser(String username);

}
