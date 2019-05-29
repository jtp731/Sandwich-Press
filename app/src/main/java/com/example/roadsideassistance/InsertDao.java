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

    @Query("update person set expiryDate = :expiryDate, cardNum = :cardNum where username = :username")
    abstract void updatePersonBankDetails(String username, Date expiryDate, long cardNum);

    @Query("update customer set streetNum = :streetNum, street = :street, city = :city, state = :state where username = :username")
    abstract void updateCustomerAddress(String username, int streetNum, String street, String city, String state);

    @Query("update person set streetNum = :streetNum, street = :street, city = :city, state = :state where username = :username")
    abstract void updatePersonAddress(String username, int streetNum, String street, String city, String state);
}
