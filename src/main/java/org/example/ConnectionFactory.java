package org.example;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionFactory {

    private static Connection connection;

    private ConnectionFactory(){}

    public static Connection getConnection() {

        if(connection == null) {
            String url = "jdbc:mysql://localhost:3306/bank_app";
            String user = "root";
            String password = "root";

            try {
                connection = DriverManager.getConnection(url, user, password);
            } catch (SQLException e) {
                throw new RuntimeException(e.getMessage());
            }
        }

        return connection;
    }

}
