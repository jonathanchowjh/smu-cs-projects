package com.example.demo.accounts;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

@Entity
public class Transfer {
    @Id @GeneratedValue(strategy=GenerationType.AUTO)
    private int id;

    private int from;
    private int to;
    private double amount;

    public Transfer() {}

    public String toString() {
        return String.format(
            "Transfer Details: \n Transfer ID: %d \n Amount: %f \n Sender: %d \n Receiver: %d\n", this.id, this.amount, this.from, this.to
        );
    }

    // GETTERS
    public int getId() { return this.id; }
    public double getAmount() { return this.amount; }
    public int getFrom() { return this.from; }
    public int getTo() { return this.to; }
}