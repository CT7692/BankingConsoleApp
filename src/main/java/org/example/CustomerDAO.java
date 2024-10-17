package org.example;

import java.util.List;

public interface CustomerDAO {
    void addCustomer(Customer customer);
    void approveCustomerRequest(int customerID);
    Customer checkCustomerLogin(String username, String password);
    List<Customer> getAllCustomers();
    void depositMoney(Customer customer, double amount);
    void withdrawMoney(Customer customer, double amount);
    void checkBalance(Customer customer);
    void transferToAnotherAccount(Customer customer, int diffCustID, double amount);
}
