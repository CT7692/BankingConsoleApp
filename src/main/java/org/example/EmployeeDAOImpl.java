package org.example;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class EmployeeDAOImpl implements EmployeeDAO{

    private final Connection connection;

    public EmployeeDAOImpl() {
        this.connection = ConnectionFactory.getConnection();
    }

    @Override
    public void addEmployee(Employee employee) {
        try {
            String addEmpQuery = "insert into employee(name, username, password)" +
                    "values(?, ?, ?)";

            PreparedStatement preparedStatement = connection.prepareStatement(addEmpQuery);
            preparedStatement.setString(1, employee.getName());
            preparedStatement.setString(2, employee.getUsername());
            preparedStatement.setString(3, employee.getPassword());

            int rows = preparedStatement.executeUpdate();
            System.out.println(rows + " employee added.");

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public Employee checkEmployeeLogin(String username, String password) {

        Employee employee = null;

        try {
            String loginQuery = "select * from employee where username = ? and password = ?";
            PreparedStatement preparedStatement = connection.prepareStatement(loginQuery);
            preparedStatement.setString(1, username);
            preparedStatement.setString(2, password);
            ResultSet resultSet = preparedStatement.executeQuery();

            if(resultSet.next()) {
                employee = new Employee(resultSet.getString("name"),
                        resultSet.getString("username"),
                        resultSet.getString("password"));

                System.out.println("Login successful.");
            }
            else
                System.out.println("Incorrect credentials.");

        } catch (SQLException e) {
            throw new RuntimeException(e.getMessage());
        }

        return employee;
    }
}
