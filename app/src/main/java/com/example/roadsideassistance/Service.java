package com.example.roadsideassistance;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.location.Location;

import java.util.Date;

@Entity(foreignKeys = {
        @ForeignKey(entity = RoadsideAssistant.class,
                    parentColumns = "username",
                    childColumns = "roadside_assistant_username",
                    onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = Customer.class,
                    parentColumns = "username",
                    childColumns = "customer_username",
                    onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = Car.class,
                    parentColumns = "plateNum",
                    childColumns = "car_plateNum",
                    onDelete = ForeignKey.CASCADE),
        },
        primaryKeys = {"customer_username", "car_plateNum", "time"},
        indices = {@Index(value = {"customer_username", "car_plateNum", "time"})}
)
public class Service {
        public float cost;
        public Date time;
        public Location location;

        public String roadside_assistant_username;
        public String cutomer_username;
        public String car_plateNum;

        public Service(RoadsideAssistant roadsideAssistant, Customer customer, Car car, float cost, Date time, Location location) {
            this.roadside_assistant_username = roadsideAssistant.username;
            this.cutomer_username = customer.username;
            this.car_plateNum = car.plateNum;
            this.cost = cost;
            this.time = time;
            this.location = location;
        }

        public Service(Customer customer, Car car, Location location) {
            this.cutomer_username = customer.username;
            this.car_plateNum = car.plateNum;
            this.location = location;
            time = new Date();
            cost = 0;
            roadside_assistant_username = null;
        }
}
