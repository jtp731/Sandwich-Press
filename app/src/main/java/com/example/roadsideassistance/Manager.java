package com.example.roadsideassistance;

import android.arch.persistence.room.Entity;

@Entity(inheritSuperIndices = true)
public class Manager extends Person{
    public int accessLevel;

    public Manager(String username, String password, String phonenumber, String email, String firstName, String lastName, int accessLevel) {
        super(username, password, phonenumber, email, firstName, lastName);
        this.accessLevel = accessLevel;
    }
}
