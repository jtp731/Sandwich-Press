package com.example.roadsideassistance;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.Index;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

import java.util.Date;

@Entity(foreignKeys = {
            @ForeignKey(entity = Customer.class,
                parentColumns = "username",
                childColumns = "customer_username",
                onDelete = ForeignKey.CASCADE),
            @ForeignKey(entity = Car.class,
                parentColumns = "plateNum",
                childColumns = "plate_num",
                onDelete = ForeignKey.CASCADE)
        },
        primaryKeys = {"customer_username", "plate_num", "time"},
        indices = {@Index(value = "customer_username"), @Index(value = "plate_num")})
public class SubscriptionPayment implements Parcelable {
        @Ignore
        public static final float COST_ONE_MONTH = 14.00f;
        @Ignore
        public static final float COST_SIX_MONTHS = 72.00f;
        @Ignore
        public static final float COST_ONE_YEAR = 120.00f;

        @NonNull
        public String customer_username;
        @NonNull
        public String plate_num;
        @NonNull
        public Date time;
        public float amount;

        public SubscriptionPayment(String customer_username, String plate_num, Date time, float amount) {
                this.customer_username = customer_username;
                this.plate_num = plate_num;
                this.time = time;
                this.amount = amount;
        }

        @Override
        public int describeContents() {
                return 0;
        }

        @Override
        public void writeToParcel(Parcel out, int flags) {
                out.writeString(customer_username);
                out.writeString(plate_num);
                out.writeString(time.toString());
                out.writeFloat(amount);
        }

        public static final Parcelable.Creator<SubscriptionPayment> CREATOR = new Parcelable.Creator<SubscriptionPayment>() {
                public SubscriptionPayment createFromParcel(Parcel in) {
                        return new SubscriptionPayment(in);
                }

                public SubscriptionPayment[] newArray(int size) {
                        return new SubscriptionPayment[size];
                }
        };

        @Ignore
        public SubscriptionPayment(Parcel in) {
                this.customer_username = in.readString();
                this.plate_num = in.readString();
                this.time = new Date(in.readString());
                this.amount = in.readFloat();
        }
}
