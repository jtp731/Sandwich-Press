package com.example.roadsideassistance;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

@Entity(inheritSuperIndices = true)
public class RoadsideAssistant extends Person{
    String licence;
    String companyName;
    long abn;
    boolean canTow;
    float rating;

    @Ignore
    public List<Review> reviews;

    @Ignore
    public List<Service> services;

    @Ignore
    public RoadsideAssistant(String username, String password, String phonenumber, String email, String firstName, String lastName, String licence, String companyName, long abn, boolean canTow, float rating) {
        super(username, password, phonenumber, email, firstName, lastName);
        this.licence = licence;
        this.companyName = companyName;
        this.abn = abn;
        this.canTow = canTow;
        this.rating = rating;
        reviews = new ArrayList<>();
        services = new ArrayList<>();
    }

    @Ignore
    public RoadsideAssistant(Person person, String licence, String companyName, long abn, boolean canTow, float rating) {
        super(person);
        this.canTow = canTow;
        this.licence = licence;
        this.companyName = companyName;
        this.abn = abn;
        this.canTow = canTow;
        this.rating = rating;
    }

    @Ignore
    public boolean addPay(float pay) {
        return bankAccount.tryPay();
    }

    public RoadsideAssistant(String username, String password, String phonenumber, String email, String firstName, String lastName, Address address, BankAccount bankAccount, String licence, String companyName, long abn, boolean canTow, float rating) {
        super(username, password, phonenumber, email, firstName, lastName, address, bankAccount);
        this.licence = licence;
        this.companyName = companyName;
        this.abn = abn;
        this.canTow = canTow;
        this.rating = rating;

    }

    public String toString() {
        String string = super.toString() + ", Licence = " + licence + ", Company Name = " + companyName + ", ABN = " + abn + ", CanTow = " + canTow + ", Rating = " + rating;
        return string;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeString(licence);
        out.writeString(companyName);
        out.writeLong(abn);
        out.writeInt(canTow ? 1:0);
        out.writeFloat(rating);
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
        this.licence = in.readString();
        this.companyName = in.readString();
        this.abn = in.readLong();
        this.canTow = in.readInt() == 1;
        this.rating = in.readFloat();
        services = new ArrayList<>();
        in.readList(services, Service.class.getClassLoader());
        reviews = new ArrayList<>();
        in.readList(reviews, Review.class.getClassLoader());
    }

    public ArrayList<Service> getActiveServices() {
        ArrayList<Service> activeServices = new ArrayList<>();
        if(services.size() > 0) {
            for(int i = 0; i < services.size(); i++) {
                if(services.get(i).status == 1)
                    activeServices.add(services.get(i));
            }
        }
        return activeServices;
    }

    public void removeService(Service service) {
        if(services != null && services.size() > 0) {
            for(int i = 0; i < services.size(); i++) {
                if(services.get(i).equals(service))
                    services.remove(i);
            }
        }
    }

    public void updateService(Service service, int status) {
        if(services != null && services.size() > 0) {
            for(int i = 0; i < services.size(); i++) {
                if(services.get(i).equals(service))
                    services.get(i).status = status;
            }
        }
    }

    public ArrayList<Service> getCurrentOffers() {
        ArrayList<Service> offers = new ArrayList<>();
        if(services != null && services.size() > 0) {
            for(int i = 0; i < services.size(); i++) {
                if(services.get(i).status == 0)
                    offers.add(services.get(i));
            }
        }
        return offers;
    }
}
