/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jessesitesmastery6;

/**
 *
 * @author jesse
 */
public abstract class Lodging {

    int idNumber;
    String name;
    String description;
    int maxOccupants;
    double basePricePerNight;
    double parkingFee;
    boolean hasFreeBreakfast;

    public Lodging() {
        idNumber = 0;
        name = "";
        description = "";
        maxOccupants = 0;
        basePricePerNight = 0;
        parkingFee = 0;
        hasFreeBreakfast = false;
    }

    public Lodging(int idNumber, String name, String description, int maxOccupants, double basePricePerNight, double parkingFee, boolean hasFreeBreakfast) {
        this.idNumber = idNumber;
        this.name = name;
        this.description = description;
        this.maxOccupants = maxOccupants;
        this.basePricePerNight = basePricePerNight;
        this.parkingFee = parkingFee;
        this.hasFreeBreakfast = hasFreeBreakfast;
    }

    public void display() {
        System.out.println("ID: " + idNumber);
        System.out.println("Name: " + name);
        System.out.println("Description: " + description);
        System.out.println("Max occupants: " + maxOccupants);
        System.out.printf("Base price per night: $%.2f%n", basePricePerNight);
        System.out.printf("Parking fee: $%.2f%n", parkingFee);
        System.out.println("Has free breakfast: " + hasFreeBreakfast);
    }
}
