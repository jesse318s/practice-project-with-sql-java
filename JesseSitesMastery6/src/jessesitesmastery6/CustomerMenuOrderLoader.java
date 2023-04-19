/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jessesitesmastery6;

/**
 *
 * @author jesse
 */
public class CustomerMenuOrderLoader implements Runnable {

    Thread t;
    String startDate;
    String endDate;

    CustomerMenuOrderLoader(String startDate, String endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        try {
            CustomerMenu.printReport(startDate, endDate);
        } catch (Exception x) {
            x.printStackTrace();
        }
    }
}
