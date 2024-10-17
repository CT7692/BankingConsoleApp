package org.example;

public class EmployeeDAOFactory {

    private static EmployeeDAO employeeDAO;

    private EmployeeDAOFactory(){}

    public static EmployeeDAO getEmployeeDAO() {
        if(employeeDAO == null)
            employeeDAO = new EmployeeDAOImpl();
        return employeeDAO;
    }
}
