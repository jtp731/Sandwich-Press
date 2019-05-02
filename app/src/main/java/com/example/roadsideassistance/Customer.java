package com.example.roadsideassistance;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.content.Context;

import java.util.ArrayList;
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

    @Ignore
    public Car getCar(String plateNum) {
        Car car = null;
        if (cars.size() > 0) {
            for(int i = 0; i < cars.size(); i++) {
                if(cars.get(i).plateNum.equals(plateNum))
                    car = cars.get(i);
            }
        }
        return car;
    }

    @Ignore
    public ArrayList<Service> getActiveServices() {
        ArrayList<Service> activeServices = new ArrayList<>();
        for(int i = 0; i < services.size(); i++) {
            if (services.get(i).roadside_assistant_username.equals("") && services.get(i).status == 0)
                activeServices.add(services.get(i));
        }
        return activeServices;
    }
}

