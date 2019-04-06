package com.example.roadsideassistance;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

@Entity
public class Person {
    @PrimaryKey
    @NonNull
    public final String username;
    public String email;
    public String firstName;
    public String lastName;

    @Embedded
    public Address address;

    @Embedded
    public BankAccount bankAccount;

    public Person(String username, String email, String firstName, String lastName) {
        this.username = username;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }
}
