package com.example.roadsideassistance;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.Date;

/*
This Class is used to insert users into the system due to the fact that room makes separate tables for
person, customer, roadside assistant, and manager instead of one table
*/
@Dao
public abstract class InsertDao {
    void addRoadsideAssistant(RoadsideAssistant roadsideAssistant){
        insertRoadsideAssistant(roadsideAssistant);
        insertPerson(new Person(roadsideAssistant.username, roadsideAssistant.password, roadsideAssistant.phonenumber, roadsideAssistant.email, roadsideAssistant.firstName, roadsideAssistant.lastName));
    }

    void addCustomer(Customer customer) {
        insertCustomer(customer);
        insertPerson(new Person(customer.username, customer.password, customer.phonenumber, customer.email, customer.firstName, customer.lastName));
    }

    void addManager(Manager manager) {
        insertManager(manager);
        insertPerson(new Person(manager.username, manager.password, manager.phonenumber, manager.email, manager.firstName, manager.lastName));
    }

    void updateCustomerBankDetails(Customer updatedCustomer) {
        updateCustomerBankDetails(updatedCustomer.username, updatedCustomer.bankAccount.expiryDate, updatedCustomer.bankAccount.cardNum);
        updatePersonBankDetails(updatedCustomer.username, updatedCustomer.bankAccount.expiryDate, updatedCustomer.bankAccount.cardNum);
    }

    void updateCustomerAddress(Customer updatedCustomer) {
        updateCustomerAddress(updatedCustomer.username,
                updatedCustomer.address.streetNum,
                updatedCustomer.address.street,
                updatedCustomer.address.city,
                updatedCustomer.address.state);
        updatePersonAddress(updatedCustomer.username,
                updatedCustomer.address.streetNum,
                updatedCustomer.address.street,
                updatedCustomer.address.city,
                updatedCustomer.address.state);
    }

    void updateRoadsideBankDetails(RoadsideAssistant updatedRoadside){
        updateRoadsideBankDetails(updatedRoadside.username, updatedRoadside.bankAccount.expiryDate, updatedRoadside.bankAccount.cardNum);
        updatePersonBankDetails(updatedRoadside.username, updatedRoadside.bankAccount.expiryDate, updatedRoadside.bankAccount.cardNum);

    }

    void updateRoadsideAddress(RoadsideAssistant updatedRoadside) {
        updateRoadsideAddress(updatedRoadside.username,
                updatedRoadside.address.streetNum,
                updatedRoadside.address.street,
                updatedRoadside.address.city,
                updatedRoadside.address.state);
        updatePersonAddress(updatedRoadside.username,
                updatedRoadside.address.streetNum,
                updatedRoadside.address.street,
                updatedRoadside.address.city,
                updatedRoadside.address.state);
    }

    void updateCustomer(Customer updatedCustomer){
        updateCustomerDetails(updatedCustomer.username,
                updatedCustomer.phonenumber,
                updatedCustomer.email,
                updatedCustomer.password);
        updatePersonDetails(updatedCustomer.username,
                updatedCustomer.phonenumber,
                updatedCustomer.email,
                updatedCustomer.password);
    }

    void updateRoadside(RoadsideAssistant updatedRoadside){
        updateRoadsideDetails(updatedRoadside.username,
                updatedRoadside.phonenumber,
                updatedRoadside.email,
                updatedRoadside.password);
        updatePersonDetails(updatedRoadside.username,
                updatedRoadside.phonenumber,
                updatedRoadside.email,
                updatedRoadside.password);
    }

    void addPerson(Person person) {
        insertPerson(person);
    }

    @Insert(onConflict = OnConflictStrategy.FAIL)
    abstract void insertRoadsideAssistant(RoadsideAssistant roadsideAssistant);

    @Insert(onConflict = OnConflictStrategy.FAIL)
    abstract void insertCustomer(Customer customer);

    @Insert(onConflict = OnConflictStrategy.FAIL)
    abstract void insertManager(Manager manager);

    @Insert(onConflict = OnConflictStrategy.FAIL)
    abstract void insertPerson(Person person);

    @Query("update customer set expiryDate = :expiryDate, cardNum = :cardNum where username = :username")
    abstract void updateCustomerBankDetails(String username, Date expiryDate, long cardNum);

    @Query("update roadsideassistant set expiryDate = :expiryDate, cardNum = :cardNum where username = :username")
    abstract void updateRoadsideBankDetails(String username, Date expiryDate, long cardNum);

    @Query("update person set expiryDate = :expiryDate, cardNum = :cardNum where username = :username")
    abstract void updatePersonBankDetails(String username, Date expiryDate, long cardNum);

    @Query("update customer set streetNum = :streetNum, street = :street, city = :city, state = :state where username = :username")
    abstract void updateCustomerAddress(String username, int streetNum, String street, String city, String state);

    @Query("update roadsideassistant set streetNum = :streetNum, street = :street, city = :city, state = :state where username = :username")
    abstract void updateRoadsideAddress(String username, int streetNum, String street, String city, String state);

    @Query("update person set streetNum = :streetNum, street = :street, city = :city, state = :state where username = :username")
    abstract void updatePersonAddress(String username, int streetNum, String street, String city, String state);

    @Query("update customer set phonenumber = :phoneNumber, email = :email, password = :password where username = :username")
    abstract void updateCustomerDetails(String username, String phoneNumber, String email, String password);

    @Query("update roadsideassistant set phonenumber = :phoneNumber, email = :email, password = :password where username = :username")
    abstract void updateRoadsideDetails(String username, String phoneNumber, String email, String password);

    @Query("update person set phonenumber = :phoneNumber, email = :email, password = :password where username = :username")
    abstract void updatePersonDetails(String username, String phoneNumber, String email, String password);
}
