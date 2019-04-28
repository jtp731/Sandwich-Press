package com.example.roadsideassistance;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;

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
}
