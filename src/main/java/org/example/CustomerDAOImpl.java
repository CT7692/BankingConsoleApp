package org.example;

import java.sql.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAOImpl implements CustomerDAO {

    private final Connection connection;

    public CustomerDAOImpl() {
        this.connection = ConnectionFactory.getConnection();
    }

    public void recordTransaction(Customer customer, double beforeBalance, double amount, String transactionType) {
        TransactionDAO transactionDAO = TransactionDAOFactory.getTransactionDAO();
        Transaction transaction = new Transaction(new Date(System.currentTimeMillis()),
                beforeBalance, customer.getBalance() ,amount, customer.getCustomerID(), transactionType);
        transactionDAO.createTransaction(transaction);
    }


    @Override
    public void addCustomer(Customer customer) {

        try {

            String addQuery =
                    "insert into customer(name, birthday, balance, is_approved, username, password) " +
                            "values(?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(addQuery);
            preparedStatement.setString(1, customer.getName());
            preparedStatement.setString(2, customer.getBirthday());
            preparedStatement.setDouble(3, customer.getBalance());
            preparedStatement.setBoolean(4, customer.isApproved());
            preparedStatement.setString(5, customer.getUsername());
            preparedStatement.setString(6, customer.getPassword());

            int rows = preparedStatement.executeUpdate();
            System.out.println(rows + " customer request added.");

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void approveCustomerRequest(int customerID) {
        try {
            String updatedStatus = "true";
            String updateQuery = "update customer set is_approved = ? where customer_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(updateQuery);
            preparedStatement.setBoolean(1,
                    Boolean.parseBoolean(updatedStatus.toLowerCase()));
            preparedStatement.setInt(2, customerID);

            preparedStatement.executeUpdate();
            System.out.println("Request updated.");

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<Customer> getAllCustomers() {

        ArrayList<Customer> customers = new ArrayList<>();

        try {
            String selectQuery = "select * from customer";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery);

            while(resultSet.next()) {

                Customer customer = new Customer(
                        resultSet.getString(2),
                        resultSet.getString(3),
                        resultSet.getDouble(4),
                        resultSet.getBoolean(5),
                        resultSet.getString(6),
                        resultSet.getString(7));

                customer.setCustomerID(resultSet.getInt(1));
                customers.add(customer);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        return customers;
    }

    @Override
    public Customer checkCustomerLogin(String username, String password) {

        Customer customer = null;

        try {
            String loginQuery = "select * from customer where username = ? and password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(loginQuery);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            if (resultSet.next()) {

                customer = new Customer(resultSet.getString("name"),
                        resultSet.getString("birthday"),
                        resultSet.getDouble("balance"),
                        resultSet.getBoolean("is_approved"),
                        resultSet.getString("username"),
                        resultSet.getString("password")
                );

                customer.setCustomerID(resultSet.getInt("customer_id"));

                System.out.println("Login successful.");
            }
            else
                System.out.println("Incorrect credentials.");

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        return customer;
    }

    @Override
    public void depositMoney(Customer customer, double amount) {
        double customerBalance = customer.getBalance();
        try {
            customer.setBalance(customerBalance + amount);
            String depositQuery = "update customer set balance = ? where customer_id = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(depositQuery);
            preparedStatement.setDouble(1, customer.getBalance());
            preparedStatement.setInt(2, customer.getCustomerID());
            preparedStatement.executeUpdate();
            System.out.println(customer.getUsername() + "'s balance updated.");
        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        recordTransaction(customer, customerBalance, amount, "deposit");
    }

    @Override
    public void withdrawMoney(Customer customer, double amount) {
        double customerBalance = 0;
        try {

            String checkBalanceQuery = "select balance from customer where customer_id = " + customer.getCustomerID();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(checkBalanceQuery);
            resultSet.next();
            customerBalance = resultSet.getDouble(1);

            if (amount < customerBalance) {
                customer.setBalance(customerBalance - amount);
                String withdrawalQuery = "update customer set balance = ? where customer_id = ?";
                PreparedStatement preparedStatement = connection.prepareStatement(withdrawalQuery);
                preparedStatement.setDouble(1, customer.getBalance());
                preparedStatement.setInt(2, customer.getCustomerID());
                preparedStatement.executeUpdate();
                System.out.println(customer.getUsername() + "'s balance updated.");
            }
            else
                System.out.println("Insufficient funds.");

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        recordTransaction(customer, customerBalance, amount, "withdrawal");
    }

    @Override
    public void checkBalance(Customer customer) {
        try {
            String checkBalanceQuery = "select balance from customer where customer_id = " + customer.getCustomerID();
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(checkBalanceQuery);
            resultSet.next();
            double balance = resultSet.getDouble(1);
            DecimalFormat df = new DecimalFormat("$#,##0.00");
            String customerBalance = df.format(balance);
            System.out.println(customerBalance);

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void transferToAnotherAccount(Customer customer, int diffCustID, double amount) {
        try {
            double customerBalance = customer.getBalance();
            //Retrieve the ID of the customer the logged in customer wants to send money to
            //and check if that customer exists.
            String findCustomer = "select * from customer where customer_id = " + diffCustID;
            Statement statement2 = connection.createStatement();
            ResultSet resultSet2 = statement2.executeQuery(findCustomer);

            if(resultSet2.next()) {

                Customer recipient = new Customer(
                        resultSet2.getString(2),
                        resultSet2.getString(3),
                        resultSet2.getDouble(4),
                        resultSet2.getBoolean(5),
                        resultSet2.getString(6),
                        resultSet2.getString(7)
                );

                recipient.setCustomerID(resultSet2.getInt(1));

                if(resultSet2.getBoolean(5)) {
                    if (amount < customerBalance) {
                        customer.setBalance(customerBalance - amount);
                        String withdrawalQuery = "update customer set balance = ? where customer_id = ?";
                        PreparedStatement preparedStatement1 = connection.prepareStatement(withdrawalQuery);
                        preparedStatement1.setDouble(1, customer.getBalance());
                        preparedStatement1.setInt(2, customer.getCustomerID());
                        preparedStatement1.executeUpdate();
                        System.out.println(customer.getUsername() + "'s balance updated.");
                        recordTransaction(customer, customerBalance, amount, "withdrawal");

                        double recipientBalance = recipient.getBalance();
                        recipient.setBalance(recipientBalance + amount);
                        String depositQuery = "update customer set balance = ? where customer_id = ?";
                        PreparedStatement preparedStatement2 = connection.prepareStatement(depositQuery);
                        preparedStatement2.setDouble(1, recipient.getBalance());
                        preparedStatement2.setInt(2, recipient.getCustomerID());
                        preparedStatement2.executeUpdate();
                        System.out.println("Transfer successful.");
                        recordTransaction(recipient, recipientBalance, amount, "deposit");

                    }
                    else
                        System.out.println("Insufficient funds.");
                }
                else
                    System.out.println("The account of the customer you're trying to reach has not been approved.");

            }
            else
                System.out.println("The account you're trying to reach doesn't exist.");


        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }
}
