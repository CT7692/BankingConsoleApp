package org.example;

import javax.swing.plaf.nimbus.State;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class TransactionDAOImpl implements TransactionDAO {

    private final Connection connection;

    public TransactionDAOImpl() {
        this.connection = ConnectionFactory.getConnection();
    }

    @Override
    public void createTransaction(Transaction transaction) {
        try {
            String createTransaction =
                    "insert into transaction(date, before_balance, new_balance, " +
                            "amount, customer_id, transaction_type)" +
                            "values(?, ?, ?, ?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(createTransaction);
            preparedStatement.setDate(1, transaction.getTransactionDate());
            preparedStatement.setDouble(2, transaction.getBeforeBalance());
            preparedStatement.setDouble(3, transaction.getNewBalance());
            preparedStatement.setDouble(4, transaction.getTransactionAmount());
            preparedStatement.setInt(5, transaction.getCustomerID());
            preparedStatement.setString(6, transaction.getTransactionType());

            int rows = preparedStatement.executeUpdate();
            System.out.println(rows + " transaction added.");

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<Transaction> getAllTransactions() {

        ArrayList<Transaction> transactions = new ArrayList<>();

        try {
            String selectQuery = "select * from transaction";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(selectQuery);

            while(resultSet.next()) {

                Transaction transaction = new Transaction(
                        resultSet.getDate(2),
                        resultSet.getDouble(3),
                        resultSet.getDouble(4),
                        resultSet.getDouble(5),
                        resultSet.getInt(6),
                        resultSet.getString(7)
                );

                transaction.setTransactionID(resultSet.getInt(1));
                transactions.add(transaction);
            }

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        return transactions;
    }

    @Override
    public List<Transaction> viewTransactionsByCustomerID(int customerID) {

        List<Transaction> transactions = new ArrayList<>();

        try {
            String searchQuery = "select * from transaction where customer_id = " + customerID;
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(searchQuery);

            while(resultSet.next()) {

                Transaction transaction = new Transaction(
                        resultSet.getDate(2),
                        resultSet.getDouble(3),
                        resultSet.getDouble(4),
                        resultSet.getDouble(5),
                        resultSet.getInt(6),
                        resultSet.getString(7)
                );

                transaction.setTransactionID(resultSet.getInt(1));
                transactions.add(transaction);

            }

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        return transactions;
    }
}
