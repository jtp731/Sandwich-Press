package com.example.roadsideassistance;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.content.Context;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(inheritSuperIndices = true)
public class Customer extends Person{
    @Ignore
    public List<Car> cars;

    @Ignore
    public List<Review> reviews;

    @Ignore
    public List<Service> services;

    @Ignore
    public List<SubscriptionPayment> subPayments;

    public Customer(String username, String password, String phonenumber, String email, String firstName, String lastName) {
        super(username, password, phonenumber, email, firstName, lastName);
    }

    public Customer(Person person) {
        super(person);
        cars = new ArrayList<>();
        reviews = new ArrayList<>();
        services = new ArrayList<>();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel out, int flags) {
        super.writeToParcel(out, flags);
        out.writeList(services);
        out.writeList(reviews);
        out.writeList(cars);
        out.writeList(subPayments);
    }

    public static final Parcelable.Creator<Customer> CREATOR = new Parcelable.Creator<Customer>() {
        public Customer createFromParcel(Parcel in) {
            return new Customer(in);
        }

        public Customer[] newArray(int size) {
            return new Customer[size];
        }
    };

    private Customer(Parcel in) {
        super(in);
        services = new ArrayList<Service>();
        in.readList(services, Service.class.getClassLoader());
        reviews = new ArrayList<Review>();
        in.readList(reviews, Review.class.getClassLoader());
        cars = new ArrayList<Car>();
        in.readList(cars, Car.class.getClassLoader());
        subPayments = new ArrayList<SubscriptionPayment>();
        in.readList(subPayments, SubscriptionPayment.class.getClassLoader());
    }

    @Ignore
    public Car getCar(String plateNum) {
        Car car = null;
        if (cars.size() > 0) {
            for(int i = 0; i < cars.size(); i++) {
                if(cars.get(i).plateNum.equals(plateNum))
                    car = cars.get(i);
            }
        }
        return car;
    }

    @Ignore
    public ArrayList<Service> getServiceRequests() {
        ArrayList<Service> serviceRequests = new ArrayList<>();
        for(int i = 0; i < services.size(); i++) {
            if (services.get(i).roadside_assistant_username.equals("") && services.get(i).status == Service.OPEN)
                serviceRequests.add(services.get(i));
        }
        return serviceRequests;
    }

    @Ignore
    public ArrayList<Service> getActiveServices() {
        ArrayList<Service> activeServices = new ArrayList<>();
        for(int i = 0; i < services.size(); i++) {
            if(!services.get(i).roadside_assistant_username.equals("") && services.get(i).status == Service.ACCEPTED)
                activeServices.add(services.get(i));
        }
        return activeServices;
    }

    @Ignore
    public ArrayList<Service> getFinishedServices() {
        ArrayList<Service> finishedServices = new ArrayList<>();
        for(int i = 0; i < services.size(); i++) {
            if(!services.get(i).roadside_assistant_username.equals("") && services.get(i).status == Service.FINISHED)
                finishedServices.add(services.get(i));
        }
        return finishedServices;
    }

    @Ignore
    public boolean carCoveredBySubscription(String plateNumber) {
        boolean covered = false;
        if (cars.size() > 0) {
            for (int i = 0; i < cars.size(); i++) {
                if(cars.get(i).plateNum.equals(plateNumber))
                    if (cars.get(i).renewalDate != null)
                        covered = true;
            }
        }
        return covered;
    }

    @Ignore
    public void updateServiceToAccepted(Service service) {
        if(services.size() > 0) {
            for(int i = 0; i < services.size(); i++) {
                Service currService = services.get(i);
                if ((currService.roadside_assistant_username.equals("") || currService.roadside_assistant_username.equals(service.roadside_assistant_username)) &&
                    currService.car_plateNum.equals(service.car_plateNum) && currService.time.equals(service.time))
                    services.get(i).status = Service.ACCEPTED;
            }
        }
    }

    @Ignore
    public void finishService(Service service) {
        if(services.size() > 0) {
            ArrayList<Service> servicesToDelete = new ArrayList<>();
            for (int i = 0; i < services.size(); i++) {
                Service currService = services.get(i);
                if(currService.car_plateNum.equals(service.car_plateNum) && currService.time.equals(service.time)) {
                    if(currService.roadside_assistant_username.equals(service.roadside_assistant_username)) {
                        if (this.carCoveredBySubscription(service.car_plateNum))
                            services.get(i).status = Service.PAYED_WITH_SUB;
                        else
                            services.get(i).status = Service.PAYED_WITH_CARD;
                    }
                    else
                        servicesToDelete.add(currService);
                }
            }
            if (servicesToDelete.size() > 0) {
                for(int i = 0; i < servicesToDelete.size(); i++)
                    services.remove(servicesToDelete.get(i));
            }
        }
    }

    @Ignore
    public void removeService(Service service) {
        if(services.size() > 0) {
            Service currService;
            int serviceToDelete = -1;
            for (int i = 0; i < services.size(); i++) {
                currService = services.get(i);
                if(currService.car_plateNum.equals(service.car_plateNum) && currService.time.equals(service.time))
                    serviceToDelete = i;
            }
            if (serviceToDelete >= 0)
                services.remove(serviceToDelete);
        }
    }

    @Ignore
    public boolean removeCost(float cost) {
        return bankAccount.tryPay();
    }

    @Ignore
    public void addReview(Review review) {
        boolean hadPreviousReview = false;
        for (int i = 0; i < reviews.size(); i++) {
            if(reviews.get(i).roadside_assistant_username.equals(review.roadside_assistant_username)) {
                hadPreviousReview = true;
                reviews.get(i).rating = review.rating;
                reviews.get(i).reviewText = review.reviewText;
            }
        }
        if(!hadPreviousReview)
            reviews.add(review);
    }

    public void updateSubPayments(Context context){
        if(subPayments != null && cars != null && cars.size() > 0) {
            Date today = new Date();
            for(int i = 0; i < cars.size(); i++) {
                if(cars.get(i).subType != Car.FREE_SUB && cars.get(i).renewalDate.before(today)) {
                    if(cars.get(i).subType == Car.ONE_MONTH_SUB) {
                        if(this.removeCost(SubscriptionPayment.COST_ONE_MONTH)) {
                            SubscriptionPayment payment = new SubscriptionPayment(this.username, cars.get(i).plateNum, today, SubscriptionPayment.COST_ONE_MONTH);
                            subPayments.add(payment);
                            AppDatabase.getDatabase(context).customerDao().addSubPayment(payment);
                            today.setMonth(today.getMonth() + 1);//Might need to change if doesn't increment year
                            cars.get(i).renewalDate = today;
                            AppDatabase.getDatabase(context).carDao().updateSubDate(this.username, cars.get(i).plateNum, cars.get(i).renewalDate);
                        }
                        else {
                            //remove sub maybe??
                        }
                    }
                    if(cars.get(i).subType == Car.SIX_MONTH_SUB) {
                        if(this.removeCost(SubscriptionPayment.COST_SIX_MONTHS)) {
                            SubscriptionPayment payment = new SubscriptionPayment(this.username, cars.get(i).plateNum, today, SubscriptionPayment.COST_SIX_MONTHS);
                            subPayments.add(payment);
                            AppDatabase.getDatabase(context).customerDao().addSubPayment(payment);
                            today.setMonth(today.getMonth() + 6);//Might need to change if doesn't increment year
                            cars.get(i).renewalDate = today;
                            AppDatabase.getDatabase(context).carDao().updateSubDate(this.username, cars.get(i).plateNum, cars.get(i).renewalDate);
                        }
                        else {
                            //remove sub maybe??
                        }
                    }
                    if(cars.get(i).subType == Car.ONE_YEAR_SUB) {
                        if(this.removeCost(SubscriptionPayment.COST_ONE_YEAR)) {
                            SubscriptionPayment payment = new SubscriptionPayment(this.username, cars.get(i).plateNum, today, SubscriptionPayment.COST_ONE_YEAR);
                            subPayments.add(payment);
                            AppDatabase.getDatabase(context).customerDao().addSubPayment(payment);
                            today.setYear(today.getYear() + 1);
                            cars.get(i).renewalDate = today;
                            AppDatabase.getDatabase(context).carDao().updateSubDate(this.username, cars.get(i).plateNum, cars.get(i).renewalDate);
                        }
                        else {
                            //remove sub maybe??
                        }
                    }
                }
            }
        }
    }

}

