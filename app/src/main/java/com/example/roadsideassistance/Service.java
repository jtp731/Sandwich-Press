package com.example.roadsideassistance;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.location.Location;
import android.support.annotation.NonNull;

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
        indices = {@Index(value = {"customer_username", "car_plateNum", "time"}), @Index(value = {"roadside_assistant_username"}), @Index(value = "car_plateNum")}
)
public class Service {
        public float cost;
        @NonNull
        public Date time;
        @Ignore
        public Location location;
        public double latitude;
        public double longitude;

        public String roadside_assistant_username;
        @NonNull
        public String customer_username;
        @NonNull
        public String car_plateNum;

        @Ignore
        public Service(RoadsideAssistant roadsideAssistant, Customer customer, Car car, float cost, Date time, Location location) {
            this.roadside_assistant_username = roadsideAssistant.username;
            this.customer_username = customer.username;
            this.car_plateNum = car.plateNum;
            this.cost = cost;
            this.time = time;
            this.location = location;
        }

        @Ignore
        public Service(Customer customer, Car car, Location location) {
            this.customer_username = customer.username;
            this.car_plateNum = car.plateNum;
            this.location = location;
            latitude = location.getLatitude();
            longitude = location.getLongitude();
            time = new Date();
            cost = 0;
            roadside_assistant_username = null;
        }

        public Service(String customer_username, String car_plateNum, double latitude, double longitude) {
            this.customer_username = customer_username;
            this.car_plateNum = car_plateNum;
            this.latitude = latitude;
            this.longitude = longitude;
            location.setLatitude(latitude);
            location.setLongitude(longitude);
            time = new Date();
            cost = 0;
            roadside_assistant_username = null;
        }
}
