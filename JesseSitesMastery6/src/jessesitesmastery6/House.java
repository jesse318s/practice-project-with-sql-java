/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jessesitesmastery6;

/**
 *
 * @author jesse
 */
public class House extends Lodging {

    int numberOfBedrooms;
    boolean hasWasher;
    boolean hasDryer;

    public House() {
        super();
        numberOfBedrooms = 0;
        hasWasher = false;
        hasDryer = false;
    }

    public House(int idNumber, String name, String description, int maxOccupants, double basePricePerNight, double parkingFee, boolean hasFreeBreakfast,
            int numberOfBedrooms, boolean hasWasher, boolean hasDryer) {
        super(idNumber, name, description, maxOccupants, basePricePerNight, parkingFee, hasFreeBreakfast);
        this.numberOfBedrooms = numberOfBedrooms;
        this.hasWasher = hasWasher;
        this.hasDryer = hasDryer;
    }

    @Override
    public void display() {
        super.display();
        System.out.println("Number of bedrooms: " + numberOfBedrooms);
        System.out.println("Has washer: " + hasWasher);
        System.out.println("Has dryer: " + hasDryer);
    }
}
