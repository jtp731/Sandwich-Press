package com.example.roadsideassistance;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.arch.persistence.room.PrimaryKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

@Entity(indices = {@Index(value = "email", unique = true)})
public class Person implements Parcelable {
    @PrimaryKey
    @NonNull
    public final String username;
    public String password;
    public String phonenumber;
    public String email;
    public String firstName;
    public String lastName;

    @Embedded
    public Address address;

    @Embedded
    public BankAccount bankAccount;

    public Person(String username, String password, String phonenumber, String email, String firstName, String lastName) {
        this.username = username;
        this.password = password;
        this.phonenumber = phonenumber;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    @Ignore
    public Person(String username, String password, String phonenumber, String email, String firstName, String lastName, Address address, BankAccount bankAccount) {
        this.username = username;
        this.password = password;
        this.phonenumber = phonenumber;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.bankAccount = bankAccount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        out.writeString(username);
        out.writeString(password);
        out.writeString(phonenumber);
        out.writeString(email);
        out.writeString(firstName);
        out.writeString(lastName);
        out.writeParcelable(address, 0);
        out.writeParcelable(bankAccount, 0);
    }

    public static final Parcelable.Creator<Person> CREATOR = new Parcelable.Creator<Person>() {
        public Person createFromParcel(Parcel in) {
            return new Person(in);
        }

        public Person[] newArray(int size) {
            return new Person[size];
        }
    };

    private Person(Parcel in) {
        this.username = in.readString();
        this.password = in.readString();
        this.phonenumber = in.readString();
        this.email = in.readString();
        this.firstName = in.readString();
        this.lastName = in.readString();
        this.address = in.readParcelable(Address.class.getClassLoader());
        this.bankAccount = in.readParcelable(BankAccount.class.getClassLoader());
    }

    @Ignore
    public Person(Person person) {
        this.username = person.username;
        this.password = person.password;
        this.phonenumber = person.phonenumber;
        this.email = person.email;
        this.firstName = person.firstName;
        this.lastName = person.lastName;
        this.address = person.address;
        this.bankAccount = person.bankAccount;
    }
}
