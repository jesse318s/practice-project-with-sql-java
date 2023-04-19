/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jessesitesmastery6;

/**
 *
 * @author jesse
 */
public class EmployeeMenuOrderLoader implements Runnable {

    Thread t;
    String startDate;
    String endDate;

    EmployeeMenuOrderLoader(String startDate, String endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        try {
            EmployeeMenu.printReport(startDate, endDate);
        } catch (Exception x) {
            x.printStackTrace();
        }
    }
}
