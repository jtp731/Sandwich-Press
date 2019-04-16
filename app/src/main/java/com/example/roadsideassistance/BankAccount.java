package com.example.roadsideassistance;

import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;
import android.widget.Toast;

import java.text.DateFormat;
import java.util.Date;

public class BankAccount implements Parcelable {
    public int cardNum;
    public Date expiryDate;
    public float balance;

    public BankAccount(int cardNum, Date expiryDate, float balance) {
        this.cardNum = cardNum;
        this.expiryDate = expiryDate;
        this.balance = balance;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flag) {
        out.writeInt(cardNum);
        out.writeString(DateFormat.getDateInstance().format(expiryDate));
        out.writeFloat(balance);
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
        this.cardNum = in.readInt();
        try {
            this.expiryDate = DateFormat.getDateInstance().parse(in.readString());
        }
        catch (java.text.ParseException e) {

        }
        this.balance = in.readFloat();
    }
}
