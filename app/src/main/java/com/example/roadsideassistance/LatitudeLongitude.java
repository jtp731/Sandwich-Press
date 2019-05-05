package com.example.roadsideassistance;

import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

public class LatitudeLongitude {
    private final static double earthRadius = 6371;//in km
    private final static double latitudeDegreeInKm = 111;//1 degree of latitude equals 111km
    //public static String test;

    public static LatLng getDifferences(double currLat, double currLong, double radius) {
        double diffLat = Math.abs(radius/latitudeDegreeInKm);
        double earthLatRadius = earthRadius * Math.cos(Math.toRadians(currLat));
        double diffLong = Math.abs(Math.toDegrees(radius/earthLatRadius));

        //test = "lat diff = " + diffLat + " long diff = " + diffLong;
        return new LatLng(diffLat, diffLong);
    }

    public static double distance(LatLng fromPoint, LatLng toPoint) {
        double latDiff = Math.toRadians(fromPoint.latitude - toPoint.latitude);
        double lngDiff = Math.toRadians(fromPoint.longitude - toPoint.longitude);
        double a = Math.sin(latDiff/2) * Math.sin(latDiff/2) +
                Math.cos(Math.toRadians(fromPoint.latitude)) * Math.cos(Math.toRadians(toPoint.latitude)) *
                Math.sin(lngDiff/2) * Math.sin(lngDiff/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        System.out.print("Distance = " + (earthRadius * c));
        return (earthRadius * c);
    }
}
