package com.example.roadsideassistance;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.widget.Spinner;

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
        primaryKeys = {"roadside_assistant_username", "customer_username", "car_plateNum", "time"},
        indices = {@Index(value = {"customer_username", "car_plateNum", "time"}), @Index(value = {"roadside_assistant_username"}), @Index(value = "car_plateNum")}
)
public class Service implements Parcelable {
        //Filter Flags
        public static final int FLAT_TYRE = 0b1;

        public float cost;
        @NonNull
        public Date time;
        public double latitude;
        public double longitude;
        public int status;
        public int filter;
        public String description;

        @NonNull
        public String roadside_assistant_username;
        @NonNull
        public String customer_username;
        @NonNull
        public String car_plateNum;

        @Ignore
        public Service(RoadsideAssistant roadsideAssistant, Customer customer, Car car, float cost, Date time, double latitude, double longitude, int status) {
            this.roadside_assistant_username = roadsideAssistant.username;
            this.customer_username = customer.username;
            this.car_plateNum = car.plateNum;
            this.cost = cost;
            this.time = time;
            this.latitude = latitude;
            this.longitude = longitude;
            this.status = status;
        }

    @Ignore
    public Service(Customer customer, Car car, Location location) {
        this.customer_username = customer.username;
        this.car_plateNum = car.plateNum;
        latitude = location.getLatitude();
        longitude = location.getLongitude();
        time = new Date();
        cost = 0;
        roadside_assistant_username = "";
        status = 0;
    }

        @Ignore
        public Service(String roadside_assistant_username, String customer_username, String car_plateNum, double latitude, double longitude, Date time, float cost, int status) {
            this.customer_username = customer_username;
            this.car_plateNum = car_plateNum;
            this.latitude = latitude;
            this.longitude = longitude;
            this.roadside_assistant_username = roadside_assistant_username;
            this.time = time;
            this.cost = cost;
            this.status = status;
        }

        public Service(String customer_username, String car_plateNum, double latitude, double longitude) {
            this.customer_username = customer_username;
            this.car_plateNum = car_plateNum;
            this.latitude = latitude;
            this.longitude = longitude;
            time = new Date();
            cost = 0;
            roadside_assistant_username = "";
            status = 0;
        }

    @Override
    public String toString() {
            return ("User: " + customer_username + " Plate Number: " + car_plateNum);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeFloat(cost);
        out.writeString(time.toString());
        out.writeDouble(latitude);
        out.writeDouble(longitude);
        out.writeInt(status);
        out.writeString(customer_username);
        out.writeString(roadside_assistant_username);
        out.writeString(car_plateNum);
    }

    public static final Parcelable.Creator<Service> CREATOR = new Parcelable.Creator<Service>() {
        public Service createFromParcel(Parcel in) {
            return new Service(in);
        }

        public Service[] newArray(int size) {
            return new Service[size];
        }
    };

    private Service(Parcel in) {
        this.cost = in.readFloat();
        this.time = new Date(in.readString());
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
        this.status = in.readInt();
        this.customer_username = in.readString();
        this.roadside_assistant_username = in.readString();
        this.car_plateNum = in.readString();
    }
}
