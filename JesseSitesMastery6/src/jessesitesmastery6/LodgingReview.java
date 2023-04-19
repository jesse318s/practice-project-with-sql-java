/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jessesitesmastery6;

/**
 *
 * @author jesse
 */
public class LodgingReview {

    int idNumber;
    int rating;
    String comments;

    public LodgingReview() {
        idNumber = 0;
        rating = 0;
        comments = "";
    }

    public LodgingReview(int idNumber, int rating, String comments) {
        this.idNumber = idNumber;
        this.rating = rating;
        this.comments = comments;
    }

    public void display() {
        System.out.println("ID: " + idNumber);
        System.out.println("Rating: " + rating);
        System.out.println("Comments: " + comments);
    }
}
