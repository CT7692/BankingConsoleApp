package org.example;

import java.sql.Date;

public class Transaction {
    private int transactionID;
    private Date transactionDate;
    private double beforeBalance;
    private double newBalance;
    private double transactionAmount;
    private int customerID;
    private String transactionType;

    public Transaction() {}

    public Transaction(Date transactionDate, double beforeBalance,
                       double newBalance, double transactionAmount,
                       int customerID, String transactionType) {

        this.transactionDate = transactionDate;
        this.beforeBalance = beforeBalance;
        this.newBalance = newBalance;
        this.transactionAmount = transactionAmount;
        this.customerID = customerID;
        this.transactionType = transactionType;
    }

    public double getNewBalance() {
        return newBalance;
    }

    public void setNewBalance(double newBalance) {
        this.newBalance = newBalance;
    }

    public double getBeforeBalance() {
        return beforeBalance;
    }

    public void setBeforeBalance(double beforeBalance) {
        this.beforeBalance = beforeBalance;
    }

    public void setTransactionID(int transactionID) {
        this.transactionID = transactionID;
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }

    public int getTransactionID() {
        return transactionID;
    }

    public Date getTransactionDate() {
        return transactionDate;
    }

    public double getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(double transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "transactionID=" + transactionID +
                ", transactionDate=" + transactionDate +
                ", beforeBalance=" + beforeBalance +
                ", newBalance=" + newBalance +
                ", transactionAmount=" + transactionAmount +
                ", customerID=" + customerID +
                ", transactionType='" + transactionType + '\'' +
                '}';
    }
}
