package com.example.roadsideassistance;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;

public class TestData {

    public static void createTestData(Context context, int numberOfRoadsideassistants, int numberOfCustomers, int numberOfCarsPerCustomer, int numberOfServices) {
        AppDatabase database = AppDatabase.getDatabase(context);
        for(int i = 0; i < numberOfRoadsideassistants; i++) {
            database.userDao().addRoadsideAssistant(new RoadsideAssistant(
                    "road" + (i + 1),
                    "123",
                    "" + (int)(Math.random()*1e8),
                    "road" + (i + 1) + "@email",
                    "road" + (i + 1) + "FirstName",
                    "road" + (i + 1) + "LastName",
                    "MVTC" + (int)(Math.random()*1e5),
                    "Comany" + (i + 1),
                    (long)(Math.random()*1e11),
                    (Math.random() > 0.5 ? true : false),
                    0.0f));
        }

        for(int i = 0; i < numberOfCustomers; i++) {
            Customer customer = new Customer(
                    "cust" + (i + 1),
                    "123",
                    "" + (int)(Math.random()*1e8),
                    "cust" + (i + 1) + "@email",
                    "cust" + (i + 1) + "FirstName",
                    "cust" + (i + 1) + "LastName");

            database.userDao().addCustomer(customer);

            for(int j = 0; j < numberOfCarsPerCustomer; j++) {
                database.carDao().addCar(new Car(
                        customer.username,
                        "" + (int)(Math.random()*1e6),
                        "Model" + (j + 1),
                        "Manufacturer" + (j + 1),
                        "Colour" + (j + 1),
                        new Date(0)
                ));
            }
        }

        for(int i = 0; i < numberOfServices; i++) {
            int customerNum = (int)(Math.random()*numberOfCustomers + 1);
            Customer customer = database.customerDao().getCustomer("cust" + customerNum + "@email");
            customer.cars = database.customerDao().getAllCustomerCars(customer.username);
            Date date = new Date();
            date.setTime(date.getTime() + (long)(i*1e6));
            database.serviceDao().addService(new Service(
                    customer.username,
                    customer.cars.get((int)(Math.random()*numberOfCarsPerCustomer)).plateNum,
                    -33.8688 ,//+ Math.random(),
                    151.2093 ,//+ Math.random(),
                    date,
                    (byte)0,
                    ""
            ));
        }
    }
}
