/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jessesitesmastery6;

/**
 *
 * @author jesse
 */
public class CustomerMenuOrderCreator implements Runnable {

    Thread t;
    static boolean isLoading = false;
    String startDate;
    String endDate;

    CustomerMenuOrderCreator(String startDate, String endDate) {
        this.startDate = startDate;
        this.endDate = endDate;
        t = new Thread(this);
        t.start();
    }

    @Override
    public void run() {
        try {
            if (!isLoading) {
                isLoading = true;
                CustomerMenu.confirmOrder(startDate, endDate);
                isLoading = false;
            }
        } catch (Exception x) {
            x.printStackTrace();
        }
    }
}
