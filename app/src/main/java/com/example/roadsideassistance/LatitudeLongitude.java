package com.example.roadsideassistance;

import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

public class LatitudeLongitude {
    private final static double earthRadius = 6371;//in km
    private final static double latitudeDegreeInKm = 111;//1 degree of latitude equals 111km
    public static String test;

    public static LatLng getDifferences(double currLat, double currLong, double radius) {
        double diffLat = Math.abs(radius/latitudeDegreeInKm);
        double earthLatRadius = earthRadius * Math.cos(Math.toRadians(currLat));
        double diffLong = Math.abs(Math.toDegrees(radius/earthLatRadius));

        test = "lat diff = " + diffLat + " long diff = " + diffLong;
        return new LatLng(diffLat, diffLong);
    }
}
