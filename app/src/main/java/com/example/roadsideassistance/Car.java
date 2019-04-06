package com.example.roadsideassistance;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;

@Entity(foreignKeys = {
            @ForeignKey(entity = Customer.class,
                        parentColumns = "username",
                        childColumns = "customer_username",
                        onDelete = ForeignKey.CASCADE)},
        indices = {@Index(value = "username")}
)
public class Car {
    @PrimaryKey(autoGenerate = true)
    long username;
    public long customer_username;

    public String plateNum;
    public String model;
    public String manufacturer;
    public String colour;
    public String renewalDate;

    public Car(String plateNum, String model, String manufacturer, String colour, String renewalDate) {
        this.plateNum = plateNum;
        this.model = model;
        this.manufacturer = manufacturer;
        this.colour = colour;
        this.renewalDate = renewalDate;
    }
}
