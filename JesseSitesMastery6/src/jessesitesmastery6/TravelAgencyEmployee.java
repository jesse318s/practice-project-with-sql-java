/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jessesitesmastery6;

/**
 *
 * @author jesse
 */
public class TravelAgencyEmployee extends Person {

    public String hireDate;
    public boolean isAManager;
    public double salary;

    public TravelAgencyEmployee() {
        super();
        hireDate = "";
        isAManager = false;
        salary = 0;
    }

    public TravelAgencyEmployee(int idNumber, String name, String address, String phoneNumber,
            String hireDate, boolean isAManager, double salary, String loginName, String password) {
        super(name, idNumber, address, phoneNumber, loginName, password);
        this.hireDate = hireDate;
        this.isAManager = isAManager;
        this.salary = salary;
    }

    @Override
    public void display() {
        super.display();
        System.out.println("Hire date: " + hireDate);
        System.out.println("Is a manager: " + isAManager);
        System.out.printf("Salary: $%.2f%n", salary);
    }
}
