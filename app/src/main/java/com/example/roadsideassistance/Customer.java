package com.example.roadsideassistance;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.os.Parcel;
import android.os.Parcelable;

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

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeList(services);
        out.writeList(reviews);
        out.writeList(cars);
    }

    public static final Parcelable.Creator<Customer> CREATOR = new Parcelable.Creator<Customer>() {
        public Customer createFromParcel(Parcel in) {
            return new Customer(in);
        }

        public Customer[] newArray(int size) {
            return new Customer[size];
        }
    };

    private Customer(Parcel in) {
        super(in);
        services = new ArrayList<Service>();
        in.readList(services, Service.class.getClassLoader());
        reviews = new ArrayList<Review>();
        in.readList(reviews, Review.class.getClassLoader());
        cars = new ArrayList<Car>();
        in.readList(cars, Car.class.getClassLoader());
    }
}

