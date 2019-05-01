package com.example.roadsideassistance;

import android.os.Parcel;
import android.os.Parcelable;

public class Address implements Parcelable {
    public int streetNum;
    public String street;
    public String city;
    public String state;

    public Address(int streetNum, String street, String city, String state) {
        this.streetNum = streetNum;
        this.street = street;
        this.city = city;
        this.state = state;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeInt(streetNum);
        out.writeString(street);
        out.writeString(city);
        out.writeString(state);
    }

    public static final Parcelable.Creator<Address> CREATOR = new Parcelable.Creator<Address>() {
        public Address createFromParcel(Parcel in) {
            return new Address(in);
        }

        public Address[] newArray(int size) {
            return new Address[size];
        }
    };

    private Address(Parcel in) {
        this.streetNum = in.readInt();
        this.street = in.readString();
        this.city = in.readString();
        this.state = in.readString();
    }

    public String toString() {
        return "StreetNum = " + streetNum + ", Street = " + street + ", city = " + city + ", state = " + state;
    }
}
