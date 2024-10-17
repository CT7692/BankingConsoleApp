package org.example;

public class CustomerDAOFactory {

    private static CustomerDAO customerDAO;

    private CustomerDAOFactory(){}

    public static CustomerDAO getCustomerDAO() {
        if(customerDAO == null)
            customerDAO = new CustomerDAOImpl();
        return customerDAO;
    }
}