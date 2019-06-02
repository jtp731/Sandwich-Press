package com.example.roadsideassistance;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.Date;
import java.util.List;

@Entity(foreignKeys = {
            @ForeignKey(entity = Customer.class,
                        parentColumns = "username",
                        childColumns = "customer_username",
                        onDelete = ForeignKey.CASCADE)},
        indices = {@Index(value = "customer_username")}
)
public class Car implements Parcelable {
    @Ignore
    public static final int FREE_SUB = 0;
    public static final int ONE_YEAR_SUB = 1;

    @NonNull
    public String customer_username;

    @PrimaryKey
    @NonNull
    public String plateNum;
    public String model;
    public String manufacturer;
    public String colour;
    public int subType;
    public Date renewalDate;

    @Ignore
    public Car(String plateNum, String model, String manufacturer, String colour, int subType, Date renewalDate) {
        this.plateNum = plateNum;
        this.model = model;
        this.manufacturer = manufacturer;
        this.colour = colour;
        this.subType = subType;
        this.renewalDate = renewalDate;
    }

    public Car(String customer_username, String plateNum, String model, String manufacturer, String colour, int subType, Date renewalDate) {
        this.customer_username = customer_username;
        this.plateNum = plateNum;
        this.model = model;
        this.manufacturer = manufacturer;
        this.colour = colour;
        this.subType = subType;
        this.renewalDate = renewalDate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(customer_username);
        out.writeString(plateNum);
        out.writeString(model);
        out.writeString(manufacturer);
        out.writeString(colour);
        out.writeInt(subType);
        out.writeString(renewalDate.toString());
    }

    public static final Parcelable.Creator<Car> CREATOR = new Parcelable.Creator<Car>() {
        public Car createFromParcel(Parcel in) {
            return new Car(in);
        }

        public Car[] newArray(int size) {
            return new Car[size];
        }
    };

    private Car(Parcel in) {
        this.customer_username = in.readString();
        this.plateNum = in.readString();
        this.model = in.readString();
        this.manufacturer = in.readString();
        this.colour = in.readString();
        this.subType = in.readInt();
        this.renewalDate = new Date(in.readString());
    }

    @Override
    @Ignore
    public String toString() {
        String car = "" + plateNum + ", " + manufacturer + ", " + model + ", " + colour;
        return car;
    }
}
