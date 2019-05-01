package com.example.roadsideassistance;

import android.arch.persistence.room.TypeConverter;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;

public class BankAccount implements Parcelable {
    public long cardNum;
    public Date expiryDate;

    public BankAccount(long cardNum, Date expiryDate) {
        this.cardNum = cardNum;
        this.expiryDate = expiryDate;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flag) {
        out.writeLong(cardNum);
        out.writeString(DateFormat.getDateInstance().format(expiryDate));
    }

    public static final Parcelable.Creator<BankAccount> CREATOR = new Parcelable.Creator<BankAccount>() {
        public BankAccount createFromParcel(Parcel in) {
            return new BankAccount(in);
        }

        public BankAccount[] newArray(int size) {
            return new BankAccount[size];
        }
    };

    private BankAccount(Parcel in) {
        this.cardNum = in.readLong();
        try {
            this.expiryDate = DateFormat.getDateInstance().parse(in.readString());
        }
        catch (java.text.ParseException e) {

        }
    }

    public String toString() {
        return "cardnum = " + cardNum + ", expirydate = " + expiryDate.toString();
    }
}
