package com.example.roadsideassistance;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;

import java.util.List;

@Entity(inheritSuperIndices = true)
public class RoadsideAssistant extends Person{
    boolean canTow;
    float rating;

    @Ignore
    public List<Review> reviews;

    @Ignore
    public List<Service> services;

    public RoadsideAssistant(String username, String password, String phonenumber, String email, String firstName, String lastName, boolean canTow, float rating) {
        super(username, password, phonenumber, email, firstName, lastName);
        this.canTow = canTow;
        this.rating = rating;
    }

    @Ignore
    public RoadsideAssistant(Person person, boolean canTow) {
        super(person);
        this.canTow = canTow;
    }
}
