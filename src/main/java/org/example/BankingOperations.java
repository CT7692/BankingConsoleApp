package org.example;

import java.sql.Connection;
import java.util.List;
import java.util.Scanner;

public class BankingOperations {

    CustomerDAO customerDAO;
    TransactionDAO transactionDAO;
    EmployeeDAO employeeDAO;
    Scanner sc = new Scanner(System.in);
    Connection connection = ConnectionFactory.getConnection();

    public BankingOperations() {

        customerDAO = CustomerDAOFactory.getCustomerDAO();
        transactionDAO = TransactionDAOFactory.getTransactionDAO();
        employeeDAO = EmployeeDAOFactory.getEmployeeDAO();


        String firstOption;

        do {

            openingMenu();
            System.out.print("Enter Option: ");

            firstOption = sc.nextLine().trim();

           if (InputValidation.isValidOption(firstOption, 1, 5)) {
                switch(firstOption) {
                    case "1": {
                        Customer customer = registerCustomer();
                        customerDAO.addCustomer(customer);
                        break;
                    }

                    case "2": {
                        Employee employee = registerEmployee();
                        employeeDAO.addEmployee(employee);
                        break;
                    }

                    case "3": {
                        customerLogin();
                        break;
                    }

                    case "4": {
                        employeeLogin();
                        break;
                    }
                }
            }
        } while (!firstOption.equals("5"));

    }

    public void openingMenu() {
        System.out.println("##################### WELCOME ####################");
        System.out.println("1: Register Customer");
        System.out.println("2: Register Employee");
        System.out.println("3: Customer Login");
        System.out.println("4: Employee Login");
        System.out.println("5: Exit");
    }

    public void customerMenu(Customer customer) {
        System.out.println("##################### WELCOME BACK, " + customer.getUsername() + " #####################");
        System.out.println("1: Deposit");
        System.out.println("2: Withdrawal");
        System.out.println("3: View Balance");
        System.out.println("4: Send Money to Another Account");
        System.out.println("5: Logout");
    }

    public void employeeMenu(Employee employee) {
        System.out.println("##################### " + employee.getUsername() + "'s EMPLOYEE OPERATIONS ####################");
        System.out.println("1: View All Customers");
        System.out.println("2: Approve Customer");
        System.out.println("3: View Transactions");
        System.out.println("4: View Transactions by Customer ID");
        System.out.println("5: Exit");
    }

    public Customer registerCustomer() {

        System.out.println("##################### REGISTER CUSTOMER ####################");
        System.out.print("Enter Name: ");
        String name = sc.next();
        name += sc.nextLine();

        System.out.print("Enter Date of Birth: ");
        String birthday = sc.next();
        birthday += sc.nextLine();

        System.out.print("Enter Starting Balance: ");
        String strBalance = sc.nextLine().trim();

        System.out.print("Enter Username: ");
        String username = sc.next();
        username += sc.nextLine();

        System.out.print("Enter Password: ");
        String password = sc.next();
        password += sc.nextLine();

        return new Customer(name, birthday, Double.parseDouble(strBalance), false, username, password);
    }

    public Employee registerEmployee() {
        System.out.println("##################### REGISTER EMPLOYEE ####################");
        System.out.print("Enter Name: ");
        String name = sc.next();
        name += sc.nextLine();

        System.out.print("Enter Username: ");
        String username = sc.next();
        username += sc.nextLine();

        System.out.print("Enter Password: ");
        String password = sc.next();
        password += sc.nextLine();

        return new Employee(name, username, password);
    }

    public void customerLogin() {
        System.out.println("##################### CUSTOMER LOGIN ####################");
        System.out.print("Enter Username: ");
        String username = sc.next();
        username += sc.nextLine();

        System.out.print("Enter Password: ");
        String password = sc.next();
        password += sc.nextLine();

        Customer customer = customerDAO.checkCustomerLogin(username, password);
        boolean loginSuccessful = customer != null;
        customerOperations(loginSuccessful, customer);
    }

    public void customerOperations(boolean loginSuccessful, Customer customer) {

        String accountNotApproved = "This account has not been approved to make transactions. " +
                "Please wait for approval by an employee.";
        if(loginSuccessful) {
            String loginOption;

            do {

                customerMenu(customer);
                System.out.print("Enter option: ");
                loginOption = sc.nextLine().trim();

                if(InputValidation.isValidOption(loginOption, 1,5)) {
                    switch (loginOption) {

                        case "1": {
                            if(customer.isApproved()) {
                                double amount = getDeposit();
                                if(amount <= 0)
                                    System.out.println("Please enter a valid deposit amount.");
                                else
                                    customerDAO.depositMoney(customer, amount);
                            }
                            else
                                System.out.println(accountNotApproved);
                            break;
                        }

                        case "2": {
                            if(customer.isApproved()) {
                                double amount = getWithdrawal();
                                if(amount <= 0)
                                    System.out.println("Please enter a valid withdrawal amount.");
                                else
                                    customerDAO.withdrawMoney(customer, amount);
                            }
                            else
                                System.out.println(accountNotApproved);

                            break;
                        }

                        case "3": {
                            customerDAO.checkBalance(customer);
                            break;
                        }

                        case "4": {
                            if(customer.isApproved())
                                depositToAnotherAccount(customer);
                            else
                                System.out.println(accountNotApproved);
                            break;
                        }
                    }
                }
            } while(!loginOption.equals("5"));
        }
    }

    public double getDeposit() {
        System.out.print("Enter Amount to Deposit: ");
        String depositAmount = sc.nextLine().trim();
        return Double.parseDouble(depositAmount);
    }

    public double getWithdrawal() {
        System.out.print("Enter Amount to Withdraw: ");
        String withdrawalAmount = sc.nextLine().trim();
        return Double.parseDouble(withdrawalAmount);
    }

    public void employeeOperations(boolean loginSuccessful, Employee employee) {
        if(loginSuccessful) {
            String employeeOption;
            do {
                employeeMenu(employee);
                System.out.print("Enter Option: ");
                employeeOption = sc.nextLine().trim();

                if(InputValidation.isValidOption(employeeOption, 1, 6)) {
                    switch (employeeOption) {
                        case "1": {
                            viewAllCustomers();
                            break;
                        }

                        case "2": {
                            approveCustomer();
                            break;
                        }

                        case "3": {
                            viewAllTransactions();
                            break;
                        }

                        case "4": {
                            viewTransactionsByCustID();
                            break;
                        }
                    }
                }
            } while(!employeeOption.equals("5"));
        }
    }

    public void approveCustomer() {
        System.out.println("##################### APPROVE A CUSTOMER REQUEST ####################");
        System.out.print("Enter Customer ID For Approval: ");
        String strID = sc.nextLine().trim();
        if(InputValidation.isPosInt(strID))
            customerDAO.approveCustomerRequest(Integer.parseInt(strID));
    }

    public void employeeLogin() {
        System.out.println("##################### EMPLOYEE LOGIN ####################");
        System.out.print("Enter Username: ");
        String username = sc.next();
        username += sc.nextLine();

        System.out.print("Enter Password: ");
        String password = sc.next();
        password += sc.nextLine();

        Employee employee = employeeDAO.checkEmployeeLogin(username, password);
        boolean loginSuccessful = employee != null;
        employeeOperations(loginSuccessful, employee);
    }

    public void depositToAnotherAccount(Customer customer) {
        System.out.println("##################### SEND MONEY TO ANOTHER ACCOUNT ####################");
        System.out.print("Enter ID of Whom to Send Money: ");
        String strID = sc.nextLine().trim();
        if(InputValidation.isPosInt(strID)) {
            System.out.print("Enter Amount to Send: ");
            String amountToSend = sc.nextLine().trim();
            double amount = Double.parseDouble(amountToSend);
            customerDAO.transferToAnotherAccount(customer, Integer.parseInt(strID), amount);
        }
    }

    public void viewAllTransactions() {
        System.out.println("##################### ALL TRANSACTIONS ####################");
        List<Transaction> transactions = transactionDAO.getAllTransactions();
        if(transactions.isEmpty())
            System.out.println("No transactions yet.");
        else {
            for(Transaction transaction: transactions) System.out.println(transaction);
        }
    }

    public void viewTransactionsByCustID() {
        System.out.println("##################### VIEW TRANSACTIONS BY CUSTOMER ID ####################");
        System.out.print("Enter Customer ID: ");
        String strID = sc.nextLine().trim();
        if(InputValidation.isPosInt(strID)) {
            List<Transaction> transactions = transactionDAO.viewTransactionsByCustomerID(Integer.parseInt(strID));
            if(transactions.isEmpty())
                System.out.println("No transactions yet.");
            else {
                for(Transaction transaction: transactions) System.out.println(transaction);
            }
        }
    }

    public void viewAllCustomers() {
        System.out.println("##################### ALL CUSTOMERS ####################");
        List<Customer> customers = customerDAO.getAllCustomers();
        if(customers.isEmpty())
            System.out.println("No customers yet.");
        else
            for (Customer customer : customers) System.out.println(customer);
    }

}
