package com.example.demo.accounts;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Account {
    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    private int customer_id;
    private double balance;
    private double available_balance;

    public Account() {}

    public String toString() {
        return String.format(
            "Account Details: \n Customer ID: %d \n Account ID: %d \n Balance: %f \n Available Balance: %f\n", this.customer_id, this.id, this.balance, this.available_balance
        );
    }

    // GETTERS
    public int getID() { return this.id; }
    public int getCustomer_id() { return this.customer_id; }
    public double getBalance() { return this.balance; }
    public double getAvailable_balance() { return this.available_balance; }

    // SETTERS
    public void setCustomerID(int customer_id) {
        this.customer_id = customer_id;
    }

    public void setAvailableBalance(double available_balance) {
        this.available_balance = available_balance;
    }
    
    public void updateBalance(double addBalance) {
        this.balance += addBalance;
        this.available_balance += addBalance;
    }
    
    
}