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

    @Ignore
    public boolean carCoveredBySubscription(String plateNumber) {
        boolean covered = false;
        if (cars.size() > 0) {
            for (int i = 0; i < cars.size(); i++) {
                if(cars.get(i).plateNum.equals(plateNumber))
                    if (cars.get(i).renewalDate != null)
                        covered = true;
            }
        }
        return covered;
    }

    @Ignore
    public void updateServiceToAccepted(Service service) {
        if(services.size() > 0) {
            for(int i = 0; i < services.size(); i++) {
                Service currService = services.get(i);
                if ((currService.roadside_assistant_username.equals("") || currService.roadside_assistant_username.equals(service.roadside_assistant_username)) &&
                    currService.car_plateNum.equals(service.car_plateNum) && currService.time.equals(service.time))
                    services.get(i).status = 1;
            }
        }
    }
}

