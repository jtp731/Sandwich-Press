package com.example.roadsideassistance;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

@Entity(inheritSuperIndices = true)
public class RoadsideAssistant extends Person{
    boolean canTow;
    float rating;

    @Ignore
    public List<Review> reviews;

    @Ignore
    public List<Service> services;

    @Ignore
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

    @Ignore
    public boolean addPay(float pay) {
        return bankAccount.tryPay();
    }

    public RoadsideAssistant(String username, String password, String phonenumber, String email, String firstName, String lastName, Address address, BankAccount bankAccount, boolean canTow, float rating) {
        super(username, password, phonenumber, email, firstName, lastName, address, bankAccount);
        this.canTow = canTow;
        this.rating = rating;

    }

    public String toString() {
        String string = super.toString() + ", CanTow = " + canTow + ", Rating = " + rating;
        return string;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeList(services);
        out.writeList(reviews);
    }

    public static final Parcelable.Creator<RoadsideAssistant> CREATOR = new Parcelable.Creator<RoadsideAssistant>() {
        public RoadsideAssistant createFromParcel(Parcel in) {
            return new RoadsideAssistant(in);
        }

        public RoadsideAssistant[] newArray(int size) {
            return new RoadsideAssistant[size];
        }
    };

    private RoadsideAssistant(Parcel in) {
        super(in);
        services = new ArrayList<Service>();
        in.readList(services, Service.class.getClassLoader());
        reviews = new ArrayList<Review>();
        in.readList(reviews, Review.class.getClassLoader());
    }
}
