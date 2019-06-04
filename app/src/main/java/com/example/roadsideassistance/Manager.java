package com.example.roadsideassistance;

import android.arch.persistence.room.Entity;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

@Entity(inheritSuperIndices = true)
public class Manager extends Person{
    public int accessLevel;

    public Manager(String username, String password, String phonenumber, String email, String firstName, String lastName, int accessLevel) {
        super(username, password, phonenumber, email, firstName, lastName);
        this.accessLevel = accessLevel;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeInt(accessLevel);
    }

    public static final Parcelable.Creator<Manager> CREATOR = new Parcelable.Creator<Manager>() {
        public Manager createFromParcel(Parcel in) {
            return new Manager(in);
        }

        public Manager[] newArray(int size) {
            return new Manager[size];
        }
    };

    private Manager(Parcel in) {
        super(in);
        this.accessLevel = in.readInt();
    }
}
