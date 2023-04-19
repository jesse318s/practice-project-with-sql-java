/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jessesitesmastery6;

import java.math.BigDecimal;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.swing.JLabel;

/**
 *
 * @author jesse
 */
public class Login extends State {

    // connection status label
    static JLabel labelConnectionStatus = new JLabel();
    // loading status
    static boolean isLoading = false;

    @Override
    public void enter() {
        try {
            LoginLoader loader = new LoginLoader();
            update();
        } catch (Exception e) {
            System.out.println(e + "\n");
        }
    }

    @Override
    public void update() {
        while (true) {
            JesseSitesMastery6.loginPanel.revalidate();
            JesseSitesMastery6.loginPanel.repaint();

            if (!JesseSitesMastery6.loginPanel.isVisible()) {
                break;
            }
        }

        State.current.enter();
    }

    @Override
    public void load() throws SQLException {
        if (isLoading) {
            return;
        }

        isLoading = true;

        PreparedStatement psPeople
                = JesseSitesMastery6.con.prepareStatement("SELECT IdNumber, `Name`, "
                        + "Address, PhoneNumber, BalanceOwed, TotalSpending, "
                        + "LoginName, `Password`, NULL AS 'NULL' "
                        + "FROM Customers "
                        + "UNION ALL "
                        + "SELECT * "
                        + "FROM Employees;");
        ResultSet rsPeople = psPeople.executeQuery();
        PreparedStatement psCountCustomers
                = JesseSitesMastery6.con.prepareStatement("SELECT COUNT(*) FROM Customers;");
        ResultSet rsCountCustomers = psCountCustomers.executeQuery();
        String[] customersData;
        String customerInfo[];
        int customerCount;
        PreparedStatement psCountEmployees
                = JesseSitesMastery6.con.prepareStatement("SELECT COUNT(*) FROM Employees;");
        ResultSet rsCountEmployees = psCountEmployees.executeQuery();
        String[] employeesData;
        String employeeInfo[];
        int employeeCount;

        JesseSitesMastery6.people.clear();
        rsCountCustomers.next();
        customerCount = rsCountCustomers.getInt(1);
        rsCountEmployees.next();
        employeeCount = rsCountEmployees.getInt(1);
        customersData = new String[8];
        employeesData = new String[9];

        for (int i = 0; i < customerCount; i++) {
            rsPeople.next();

            for (int j = 0; j < 8; j++) {
                customersData[j] = rsPeople.getString(j + 1);
            }

            customerInfo = customersData;
            Customer customer = new Customer(Integer.parseInt(customerInfo[0]),
                    customerInfo[1], customerInfo[2], customerInfo[3],
                    Double.parseDouble(customerInfo[4]),
                    Double.parseDouble(customerInfo[5]), customerInfo[6],
                    decryptPassword(customerInfo[7])
            );
            JesseSitesMastery6.people.add(customer);
        }

        for (int i = 0; i < employeeCount; i++) {
            rsPeople.next();

            for (int j = 0; j < 9; j++) {
                employeesData[j] = rsPeople.getString(j + 1);
            }

            employeeInfo = employeesData;
            TravelAgencyEmployee employee = new TravelAgencyEmployee(Integer.parseInt(employeeInfo[0]),
                    employeeInfo[1], employeeInfo[2], employeeInfo[3], employeeInfo[4],
                    Double.parseDouble(employeeInfo[5]) == 1, Double.parseDouble(employeeInfo[6]),
                    employeeInfo[7], decryptPassword(employeeInfo[8]));
            JesseSitesMastery6.people.add(employee);
            ((ManagerMenu) State.managerMenu).loadTableEmployees();
        }

        isLoading = false;
    }

    @Override
    public void save() throws SQLException {
        String statementCustomers = "INSERT INTO Customers VALUES(?,?,?,?,?,?,?,?);";
        String statementEmployees = "INSERT INTO Employees VALUES(?,?,?,?,?,?,?,?,?);";
        PreparedStatement psDeleteCustomers = JesseSitesMastery6.con.prepareStatement("DELETE FROM Customers;");
        PreparedStatement psDeleteEmployees = JesseSitesMastery6.con.prepareStatement("DELETE FROM Employees;");

        psDeleteCustomers.executeUpdate();
        psDeleteEmployees.executeUpdate();

        for (int i = 0;
                i < JesseSitesMastery6.people.size();
                i++) {
            if (JesseSitesMastery6.people.get(i) instanceof Customer) {
                Customer customer = (Customer) JesseSitesMastery6.people.get(i);
                PreparedStatement ps = JesseSitesMastery6.con.prepareStatement(statementCustomers);

                ps.setInt(1, customer.idNumber);
                ps.setString(2, customer.name);
                ps.setString(3, customer.address);
                ps.setString(4, customer.phoneNumber);
                ps.setBigDecimal(5, new BigDecimal(customer.balanceOwed));
                ps.setBigDecimal(6, new BigDecimal(customer.totalSpending));
                ps.setString(7, customer.loginName);
                ps.setString(8, encryptPassword(customer.password));
                ps.executeUpdate();
            }

            if (JesseSitesMastery6.people.get(i) instanceof TravelAgencyEmployee) {
                TravelAgencyEmployee employee = (TravelAgencyEmployee) JesseSitesMastery6.people.get(i);
                PreparedStatement ps = JesseSitesMastery6.con.prepareStatement(statementEmployees);

                ps.setInt(1, employee.idNumber);
                ps.setString(2, employee.name);
                ps.setString(3, employee.address);
                ps.setString(4, employee.phoneNumber);
                ps.setString(5, employee.hireDate);
                ps.setBoolean(6, employee.isAManager);
                ps.setBigDecimal(7, new BigDecimal(employee.salary));
                ps.setString(8, employee.loginName);
                ps.setString(9, encryptPassword(employee.password));
                ps.executeUpdate();
            }
        }
    }

    public String encryptPassword(String password) {
        StringBuffer sb = new StringBuffer("");
        int passwordCharCode;

        for (int i = password.length() - 1; i > -1; i--) {
            passwordCharCode = ((int) password.charAt(i)) + 3;
            sb.append((char) passwordCharCode);
        }

        return sb.toString();
    }

    public String decryptPassword(String password) {
        StringBuffer sb = new StringBuffer("");
        int passwordCharCode;

        for (int i = 0; i < password.length(); i++) {
            passwordCharCode = ((int) password.charAt((password.length() - 1) - i)) - 3;
            sb.insert(i, (char) passwordCharCode);
        }

        return sb.toString();
    }
}
