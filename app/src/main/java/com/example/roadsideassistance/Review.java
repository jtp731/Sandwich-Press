package com.example.roadsideassistance;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.os.Parcel;
import android.os.Parcelable;
import android.support.annotation.NonNull;

@Entity(foreignKeys = {
        @ForeignKey(entity = RoadsideAssistant.class,
                parentColumns = "username",
                childColumns = "roadside_assistant_username",
                onDelete = ForeignKey.CASCADE),
        @ForeignKey(entity = Customer.class,
                parentColumns = "username",
                childColumns = "customer_username",
                onDelete = ForeignKey.CASCADE)
        },
        primaryKeys = {"roadside_assistant_username", "customer_username"})
public class Review implements Parcelable {
    @NonNull
    public String roadside_assistant_username;
    @NonNull
    public String customer_username;
    public float rating;
    public String reviewText;

    public Review(String roadside_assistant_username, String customer_username, float rating, String reviewText) {
        this.roadside_assistant_username = roadside_assistant_username;
        this.customer_username = customer_username;
        this.rating = rating;
        this.reviewText = reviewText;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    //Parcels the current object to be parceled
    public void writeToParcel(Parcel out, int flags) {
        out.writeString(roadside_assistant_username);
        out.writeString(customer_username);
        out.writeFloat(rating);
        out.writeString(reviewText);
    }

    //Used by parcelable
    public static final Parcelable.Creator<Review> CREATOR = new Parcelable.Creator<Review>() {
        public Review createFromParcel(Parcel in) {
            return new Review(in);
        }

        public Review[] newArray(int size) {
            return new Review[size];
        }
    };

    //Constructs a reviewText given a parcel
    private Review(Parcel in) {
        this.roadside_assistant_username = in.readString();
        this.customer_username = in.readString();
        this.rating = in.readFloat();
        this.reviewText = in.readString();
    }
}
