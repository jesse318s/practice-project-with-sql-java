/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jessesitesmastery6;

/**
 *
 * @author jesse
 */
public class Customer extends Person {

    double balanceOwed;
    double totalSpending;

    public Customer() {
        super();
        balanceOwed = 0;
        totalSpending = 0;
    }

    public Customer(int idNumber, String name, String address, String phoneNumber,
            double balanceOwed, double totalSpending, String loginName, String password) {
        super(name, idNumber, address, phoneNumber, loginName, password);
        this.balanceOwed = balanceOwed;
        this.totalSpending = totalSpending;
    }

    @Override
    public void display() {
        super.display();
        System.out.printf("Balance owed: $%.2f%n", balanceOwed);
        System.out.printf("Total spending: $%.2f%n", totalSpending);
    }
}
