/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jessesitesmastery6;

/**
 *
 * @author jesse
 */
public class OrderDetails {

    int idNumber;
    String date;
    double totalSpending;
    double pricePercentAdjustment;

    public OrderDetails() {
        idNumber = 0;
        date = "";
        totalSpending = 0;
        pricePercentAdjustment = 0;
    }

    public OrderDetails(int idNumber, String date, double totalSpending, double pricePercentAdjustment) {
        this.idNumber = idNumber;
        this.date = date;
        this.totalSpending = totalSpending;
        this.pricePercentAdjustment = pricePercentAdjustment;
    }

    public void display() {
        System.out.println("ID: " + idNumber);
        System.out.println("Date: " + date);
        System.out.printf("Total spending: $%.2f%n", totalSpending);
        System.out.printf("Price percent adjustment: %.2f%n", pricePercentAdjustment);
    }
}
