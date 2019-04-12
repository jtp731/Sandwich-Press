package com.example.roadsideassistance;

import java.util.Date;

public class BankAccount {
    public int cardNum;
    public Date expiryDate;

    public BankAccount(int cardNum, Date expiryDate) {
        this.cardNum = cardNum;
        this.expiryDate = expiryDate;
    }
}
