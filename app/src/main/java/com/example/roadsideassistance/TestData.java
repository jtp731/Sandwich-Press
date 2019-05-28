package com.example.roadsideassistance;

import android.content.Context;
import android.widget.Toast;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class TestData {
    static int numOfRoadside;
    static int numOfCustomers;
    static int numOfCarsPer;

    static double lat;
    static double lng;

    static int numberOfRequestsMade;
    static int numberOfOffersMade;

    static AppDatabase database;

    static Context context;

    public static void createTestData(Context createContext, int numberOfRoadsideassistants, int numberOfCustomers, int numberOfCarsPerCustomer) {
        context = createContext;
        numOfRoadside = numberOfRoadsideassistants;
        numOfCustomers = numberOfCustomers;
        numOfCarsPer = numberOfCarsPerCustomer;

        lat = -33.8688;
        lng = 151.2093;

        database = AppDatabase.getDatabase(context);
        for(int i = 0; i < numberOfRoadsideassistants; i++) {
            database.userDao().addRoadsideAssistant(new RoadsideAssistant(
                    "road" + (i + 1),
                    "123",
                    "" + (int)(Math.random()*1e8),
                    "road" + (i + 1) + "@email",
                    "road" + (i + 1) + "FirstName",
                    "road" + (i + 1) + "LastName",
                    "MVTC" + (int)(Math.random()*1e5),
                    "Company" + (i + 1),
                    (long)(Math.random()*1e11),
                    (Math.random() > 0.5 ? true : false),
                    0.0f));
        }

        String [] states = new String[]{"NSW", "QLD", "VIC", "ACT", "NT", "SA", "WA", "TAS"};

        for(int i = 0; i < numberOfCustomers; i++) {
            Customer customer = new Customer(
                    "cust" + (i + 1),
                    "123",
                    "" + (int)(Math.random()*1e8),
                    "cust" + (i + 1) + "@email",
                    "cust" + (i + 1) + "FirstName",
                    "cust" + (i + 1) + "LastName");

            Integer randYear = (int)(2018 + Math.random()*(2022 - 2018) - 1900);
            Integer randMonth = (int)(Math.random()*11);
            Date bankDate = new Date(randYear, randMonth, 1);
            customer.bankAccount = new BankAccount((long)(Math.random()*1e16), bankDate);

            customer.address = new Address(i+1, "Street Name Rd", "City" + (i+1), states[i%8]);

            database.userDao().addCustomer(customer);

            for(int j = 0; j < numberOfCarsPerCustomer; j++) {
                database.carDao().addCar(new Car(
                        customer.username,
                        "" + (int)(((Math.random()*1e5) * 10) + i),
                        "Model" + (j + 1),
                        "Manufacturer" + (j + 1),
                        "Colour" + (j + 1),
                        (int)Math.random()*4,
                        new Date()
                ));
            }
        }
    }

    public static void createRequests(int numberOfServices) {
        numberOfRequestsMade = 0;
        int i = 0;
        do {
            int customerNum = (int)(Math.random()*numOfCustomers + 1);
            Customer customer = database.customerDao().getCustomer("cust" + customerNum + "@email");
            customer.cars = database.customerDao().getAllCustomerCars(customer.username);
            Date date = new Date();
            date.setTime(date.getTime() - i*10);
            Service request = new Service(
                    customer.username,
                    customer.cars.get((int)(Math.random()*numOfCarsPer)).plateNum,
                    lat + Math.random() - 0.5,
                    lng + Math.random() - 0.5,
                    date,
                    (byte)0,
                    ""
            );
            request = addRandomFilter(request);
            if(!database.serviceDao().serviceActive(request.roadside_assistant_username, request.customer_username, request.car_plateNum, request.time)) {
                database.serviceDao().addService(request);
                numberOfRequestsMade++;
            }
            i++;
        }while(i < numberOfServices);
        //Toast.makeText(context, "Num of Requests Made: " + numberOfRequestsMade, Toast.LENGTH_SHORT).show();
    }

    public static void createOffers(int numberOfServices) {
        if(numberOfRequestsMade < numberOfServices) {
            //Toast.makeText(context, "Trying to create too many offers", Toast.LENGTH_SHORT).show();
            numberOfServices = numberOfRequestsMade;
        }
        numberOfOffersMade = 0;
        int i = 0;
        List<Service> requestedServices = database.serviceDao().getNewServiceRequests();
        do {
            Service randomService = requestedServices.get((int) (Math.random() * requestedServices.size()));
            int randomRoadsideNum = (int) (Math.random() * numOfRoadside + 1);
            Service offer = new Service(
                    "road" + randomRoadsideNum,
                    randomService.customer_username,
                    randomService.car_plateNum,
                    randomService.latitude,
                    randomService.longitude,
                    randomService.time,
                    (float) (Math.random() * 100 + 50),
                    randomService.status,
                    randomService.filter,
                    randomService.description);
            if (!database.serviceDao().serviceExists(offer.roadside_assistant_username, offer.customer_username, offer.car_plateNum, offer.time)) {
                database.serviceDao().addService(offer);
                numberOfOffersMade++;
            }
            i++;
        } while (i < numberOfServices);
        //Toast.makeText(context, "Offers Made: " + numberOfOffersMade, Toast.LENGTH_SHORT).show();
    }

    public static void createActive(int numberOfServices) {
        if(numberOfOffersMade < numberOfServices) {
            Toast.makeText(context, "Trying to make too many active services", Toast.LENGTH_SHORT).show();
        }
        numberOfServices = numberOfOffersMade;
        int i = 0;
        do {
            List<Service> offers = database.serviceDao().getAllOffers();
            Service randomOffer = offers.get((int) (Math.random() * offers.size()));
            if (!database.serviceDao().serviceActive(randomOffer.roadside_assistant_username, randomOffer.customer_username, randomOffer.car_plateNum, randomOffer.time)) {
                database.serviceDao().updateServiceStatus(randomOffer.roadside_assistant_username, randomOffer.customer_username, randomOffer.car_plateNum, randomOffer.time, Service.ACCEPTED);
            }
            i++;
        } while (i < numberOfServices);
    }

    public static void createPastServices(int numberOfServices) {
        for(int i = 0; i < numberOfServices; i++){
            int customerNum = (int)(Math.random()*numOfCustomers + 1);
            Customer customer = database.customerDao().getCustomer("cust" + customerNum + "@email");
            customer.cars = database.customerDao().getAllCustomerCars(customer.username);
            Date date = new Date();
            date.setTime(date.getTime() - (long)(i*1e10));
            int randomRoadsideNum = (int) (Math.random() * numOfRoadside + 1);
            Service pastService = new Service(
                    "road" + randomRoadsideNum,
                    customer.username,
                    customer.cars.get((int)(Math.random()*numOfCarsPer)).plateNum,
                    lat + Math.random() - 0.5,
                    lng + Math.random() - 0.5,
                    date,
                    (float) (Math.random() * 100 + 50),
                    (Math.random() > 0.5f ? Service.PAYED_WITH_CARD : Service.PAYED_WITH_SUB),
                    (byte)0,
                    ""
            );
            pastService = addRandomFilter(pastService);
            database.serviceDao().addService(pastService);
        }
    }

    public static void createSubPayHistory(int numberOfSubPayments) {

    }

    private static Service addRandomFilter(Service service) {
        if(Math.random() > 0.5f)
            service.setFlag(Service.MECHANICAL_BREAKDOWN);
        if(Math.random() > 0.5f)
            service.setFlag(Service.FLAT_TYRE);
        if(Math.random() > 0.5f)
            service.setFlag(Service.FLAT_BATTERY);
        if(Math.random() > 0.5f)
            service.setFlag(Service.KEYS_IN_CAR);
        if(Math.random() > 0.5f)
            service.setFlag(Service.CAR_STUCK);
        if(Math.random() > 0.5f)
            service.setFlag(Service.OUT_OF_FUEL);
        return service;
    }
}
