/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jessesitesmastery6;

/**
 *
 * @author jesse
 */
public class Hotel extends Lodging {

    int vacancies;
    int numberOfRooms;
    boolean has24HourLobby;
    boolean hasMovieNight;

    public Hotel() {
        super();
        vacancies = 0;
        numberOfRooms = 0;
        has24HourLobby = false;
        hasMovieNight = false;
    }

    public Hotel(int idNumber, String name, String description, int maxOccupants, double basePricePerNight, double parkingFee, boolean hasFreeBreakfast,
            int vacancies, int numberOfRooms, boolean has24HourLobby, boolean hasMovieNight) {
        super(idNumber, name, description, maxOccupants, basePricePerNight, parkingFee, hasFreeBreakfast);
        this.vacancies = vacancies;
        this.numberOfRooms = numberOfRooms;
        this.has24HourLobby = has24HourLobby;
        this.hasMovieNight = hasMovieNight;
    }

    @Override
    public void display() {
        super.display();
        System.out.println("Vacancies: " + vacancies);
        System.out.println("Number of rooms: " + numberOfRooms);
        System.out.println("Has 24 hour lobby: " + has24HourLobby);
        System.out.println("Has movie night: " + hasMovieNight);
    }
}
