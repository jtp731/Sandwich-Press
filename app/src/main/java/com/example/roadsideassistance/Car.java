package com.example.roadsideassistance;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Date;

@Entity(foreignKeys = {
            @ForeignKey(entity = Customer.class,
                        parentColumns = "username",
                        childColumns = "customer_username",
                        onDelete = ForeignKey.CASCADE)},
        indices = {@Index(value = "customer_username")}
)
public class Car {
    @NonNull
    public String customer_username;

    @PrimaryKey
    @NonNull
    public String plateNum;
    public String model;
    public String manufacturer;
    public String colour;
    public Date renewalDate;

    @Ignore
    public Car(String plateNum, String model, String manufacturer, String colour, Date renewalDate) {
        this.plateNum = plateNum;
        this.model = model;
        this.manufacturer = manufacturer;
        this.colour = colour;
        this.renewalDate = renewalDate;
    }

    public Car(String customer_username, String plateNum, String model, String manufacturer, String colour, Date renewalDate) {
        this.customer_username = customer_username;
        this.model = model;
        this.manufacturer = manufacturer;
        this.colour = colour;
        this.renewalDate = renewalDate;
    }
}
