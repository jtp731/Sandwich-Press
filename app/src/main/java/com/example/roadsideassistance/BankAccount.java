package com.example.roadsideassistance;

import android.arch.persistence.room.TypeConverter;

import java.text.DateFormat;
import java.util.Date;

public class BankAccount {
    public int cardNum;
    public Date expiryDate;

    public BankAccount(int cardNum, Date expiryDate) {
        this.cardNum = cardNum;
        this.expiryDate = expiryDate;
    }


}
