package org.example;

public class Customer extends User{
    private int customerID;
    private String birthday;
    private double balance;
    private boolean isApproved;

    public Customer() {}

    public Customer(String name, String birthday, double balance, boolean isApproved, String username, String password) {
        this.setName(name);
        this.birthday = birthday;
        this.balance = balance;
        this.isApproved = isApproved;
        this.setUsername(username);
        this.setPassword(password);
    }

    public int getCustomerID() {
        return customerID;
    }

    public void setCustomerID(int customerID) {
        this.customerID = customerID;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        this.isApproved = approved;
    }

    @Override
    public String toString() {
        return "Customer{" +
                "customerID=" + customerID +
                ", name=" + this.getName() +
                ", birthday='" + birthday + '\'' +
                ", balance=" + balance +
                ", isApproved=" + isApproved +
                ", username=" + this.getUsername() +
                ", password=" + this.getPassword() +
                '}';
    }
}
