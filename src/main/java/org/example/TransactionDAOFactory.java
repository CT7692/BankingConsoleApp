package org.example;

public class TransactionDAOFactory {

    private static TransactionDAO transactionDAO;

    private TransactionDAOFactory(){}

    public static TransactionDAO getTransactionDAO() {
        if(transactionDAO == null)
            transactionDAO = new TransactionDAOImpl();
        return transactionDAO;
    }
}
