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
    public String password;
    public String phonenumber;
    public String email;
    public String firstName;
    public String lastName;

    @Embedded
    public Address address;

    @Embedded
    public BankAccount bankAccount;

    public Person(String username, String password, String phonenumber, String email, String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.phonenumber = phonenumber;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public Person(String username, String password, String phonenumber, String email, String firstName, String lastName, Address address, BankAccount bankAccount) {
        this.username = username;
        this.password = password;
        this.phonenumber = phonenumber;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.bankAccount = bankAccount;

    }
}
