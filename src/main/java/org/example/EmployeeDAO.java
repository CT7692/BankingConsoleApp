package org.example;

public interface EmployeeDAO {
    void addEmployee(Employee employee);
    Employee checkEmployeeLogin(String username, String password);
}
