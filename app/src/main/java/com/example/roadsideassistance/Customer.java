package com.example.roadsideassistance;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;

import java.util.List;

@Entity(inheritSuperIndices = true)
public class Customer extends Person{
    @Ignore
    public List<Car> cars;

    @Ignore
    public List<Review> reviews;

    @Ignore
    public List<Service> services;

    public Customer(String username, String password, String phonenumber, String email, String firstName, String lastName) {
        super(username, password, phonenumber, email, firstName, lastName);
    }
}

