package org.example;

import java.util.List;

public interface TransactionDAO {
    void createTransaction(Transaction transaction);
    List<Transaction> getAllTransactions();
    List<Transaction> viewTransactionsByCustomerID(int customerID);
}
